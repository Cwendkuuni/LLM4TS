import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.MethodCompilerPass.GetExternMethods;
import com.google.javascript.jscomp.MethodCompilerPass.GatherSignatures;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.google.common.truth.Truth.assertThat;

public class MethodCompilerPassTest {

 private MethodCompilerPass methodCompilerPass;
 private AbstractCompiler compilerMock;
 private SignatureStore signatureStoreMock;

 @Before
 public void setUp() {
 compilerMock = mock(AbstractCompiler.class);
 signatureStoreMock = mock(MethodCompilerPass.SignatureStore.class);
 methodCompilerPass = new MethodCompilerPass(compilerMock) {
 @Override
 Callback getActingCallback() {
 return new DummyCallback();
 }

 @Override
 SignatureStore getSignatureStore() {
 return signatureStoreMock;
 }
 };
 }

 @Test
 public void testGetExternMethods() {
 Node externs = createMockNode("{\n" +
 "  foo: function() {},\n" +
 "  bar: function() {}\n" +
 "}");
 methodCompilerPass.process(externs, null);

 // Check that method signatures were correctly extracted from externs
 verify(signatureStoreMock, times(2)).addSignature(
 "foo", anyNode(), anyString());
 verify(signatureStoreMock, times(1)).addSignature(
 "bar", anyNode(), anyString());

 // Check that non-method properties were correctly identified
 assertThat(methodCompilerPass.nonMethodProperties).containsExactly("foo");
 }

 @Test
 public void testGatherSignatures() {
 Node root = createMockNode("function Foo() {\n" +
 "  this.foo = function() {};\n" +
 "  this.bar = function() {};\n" +
 "}\n");
 methodCompilerPass.process(null, root);

 // Check that method signatures were correctly extracted from source
 verify(signatureStoreMock, times(2)).addSignature(
 "foo", anyNode(), anyString());
 verify(signatureStoreMock, times(1)).addSignature(
 "bar", anyNode(), anyString());
 }

 @Test
 public void testProcessWithExternsAndSource() {
 Node externs = createMockNode("{\n" +
 "  foo: function() {},\n" +
 "  bar: function(int a) {}\n" +
 "}");
 Node root = createMockNode("function Foo() {\n" +
 "  this.foo = function() {};\n" +
 "  this.bar = function(int a) {};\n" +
 "}");
 methodCompilerPass.process(externs, root);

 // Check that method signatures were correctly extracted from both externs and source
 verify(signatureStoreMock, times(2)).addSignature(
 "foo", anyNode(), anyString());
 verify(signatureStoreMock, times(1)).addSignature(
 "bar", anyNode(), anyString());

 // Check that function nodes are correctly associated with their names
 Node fooMethod = methodCompilerPass.methodDefinitions.get("foo").get();
 assertThat(fooMethod.getFirstChild().getString()).isEqualTo("function");
 Node barMethod = methodCompilerPass.methodDefinitions.get("bar").get();
 assertThat(barMethod.getFirstChild().getFirstChild().getString())
     .isEqualTo("function int");
 }

 @Test(expected = IllegalStateException.class)
 public void testErrorWhenNonMethodPropertyIsAssignedAFunction() {
 Node root = createMockNode("function Foo() {\n" +
 "  this.foo = function() {};\n" +
 "  this.foo = function() {};\n" +
 "}");
 methodCompilerPass.process(null, root);
 fail("Should have thrown an exception");
 }

 @Test
 public void testIgnoreExternMethodsWithoutSignatures() {
 Node externs = createMockNode("{\n" +
 "  foo: function() {},\n" +
 "  bar: function() {}\n" +
 "}");
 methodCompilerPass.process(externs, null);

 // Check that method signatures for extern methods without signatures were not added
 verifyNoMoreInteractions(signatureStoreMock);
 }

 private Node createMockNode(String nodeString) {
 Node node = Node.parse(nodeString);
 node.setType(Token.ROOT);
 return node;
 }
}

class DummyCallback implements MethodCompilerPass.Callback {}