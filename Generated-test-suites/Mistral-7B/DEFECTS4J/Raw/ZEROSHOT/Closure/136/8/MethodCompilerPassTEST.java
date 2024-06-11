import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

public class MethodCompilerPassTest {

  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    signatureStore = mock(SignatureStore.class);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        return Mockito.mock(Callback.class);
      }
    };
  }

  @Test
  public void testExternMethodsClear() {
    methodCompilerPass.process(null, null);
    assertEquals(Sets.newHashSet(), methodCompilerPass.externMethods);
  }

  @Test
  public void testExternMethodsAdd() {
    Node externs = createNode("'Foo.bar': function() {}", "externs.js");
    methodCompilerPass.process(externs, null);
    assertEquals(Sets.newHashSet("Foo.bar"), methodCompilerPass.externMethods);
  }

  @Test
  public void testExternMethodsAddWithSignature() {
    Node externs = createNode("'Foo.bar': function(x, y) {}", "externs.js");
    methodCompilerPass.process(externs, null);
    assertEquals(Sets.newHashSet("Foo.bar"), methodCompilerPass.externMethods);
    assertSame(methodDefinitions.get("Foo.bar"), methodCompilerPass.methodDefinitions.get("Foo.bar"));
  }

  @Test
  public void testExternMethodsAddWithoutSignature() {
    Node externs = createNode("'Foo.bar'", "externs.js");
    methodCompilerPass.process(externs, null);
    assertEquals(Sets.newHashSet("Foo.bar"), methodCompilerPass.externMethods);
    assertFalse(methodCompilerPass.methodDefinitions.containsKey("Foo.bar"));
  }

  @Test
  public void testExternMethodsAddMultiple() {
    Node externs = createNode("'Foo.bar': function() {}, 'Foo.baz': function() {}", "externs.js");
    methodCompilerPass.process(externs, null);
    assertEquals(Sets.newHashSet("Foo.bar", "Foo.baz"), methodCompilerPass.externMethods);
  }

  @Test
  public void testExternMethodsIgnoreWithoutSignature() {
    Node externs = createNode("'Foo.bar'", "externs.js");
    methodCompilerPass.process(externs, null);
    assertEquals(Sets.newHashSet("Foo.bar"), methodCompilerPass.externMethodsWithoutSignatures);
    assertFalse(methodCompilerPass.methodDefinitions.containsKey("Foo.bar"));
  }

  @Test
  public void testExternMethodsIgnoreMultipleWithoutSignature() {
    Node externs = createNode("'Foo.bar'", "'Foo.baz'");
    methodCompilerPass.process(externs, null);
    assertEquals(Sets.newHashSet("Foo.bar", "Foo.baz"), methodCompilerPass.externMethodsWithoutSignatures);
    assertFalse(methodCompilerPass.methodDefinitions.containsKey("Foo.bar"));
    assertFalse(methodCompilerPass.methodDefinitions.containsKey("Foo.baz"));
  }

  @Test
  public void testNonMethodPropertiesClear() {
    methodCompilerPass.process(null, null);
    assertEquals(Sets.newHashSet(), methodCompilerPass.nonMethodProperties);
  }

  @Test
  public void testNonMethodPropertiesAdd() {
    Node root = createNode("foo: 42", null);
    methodCompilerPass.process(null, root);
    assertEquals(Sets.newHashSet("foo"), methodCompilerPass.nonMethodProperties);
  }

  @Test
  public void testNonMethodPropertiesAddMultiple() {
    Node root = createNode("foo: 42", "bar: 42");
    methodCompilerPass.process(null, root);
    assertEquals(Sets.newHashSet("foo", "bar"), methodCompilerPass.nonMethodProperties);
  }

  @Test
  public void testMethodDefinitionsClear() {
    methodCompilerPass.process(null, null);
    assertEquals(Multimap.of(), methodCompilerPass.methodDefinitions);
  }

  @Test
  public void testMethodDefinitionsAdd() {
    Node root = createNode("Foo.bar = function() {}", null);
    methodCompilerPass.process(null, root);
    Node expected = createNode("function() {}", null);
    assertSame(expected, methodCompilerPass.methodDefinitions.get("Foo.bar"));
  }

  @Test
  public void testMethodDefinitionsAddSignature() {
    Node root = createNode("Foo.bar = function(x, y) {}", null);
    methodCompilerPass.process(null, root);
    Node expected = createNode("function(x, y)", null);
    assertSame(expected, methodCompilerPass.methodDefinitions.get("Foo.bar"));
  }

  @Test
  public void testSignatureStoreReset() {
    when(signatureStore.reset()).thenReturn(void.class);
    methodCompilerPass.process(null, null);
    methodCompilerPass.getSignatureStore().reset();
    assertNull(methodCompilerPass.getSignatureStore().getSignatures());
  }

  @Test
  public void testSignatureStoreAddSignature() {
    when(signatureStore.addSignature("Foo.bar", Mockito.anyNode(), Mockito.anyString()))
        .thenAnswer(invocation -> null);
    Node root = createNode("Foo.bar = function() {}", null);
    methodCompilerPass.process(null, root);
    methodCompilerPass.getSignatureStore().addSignature("Foo.bar", createNode("function() {}", null), "test.js");
    Node expected = createNode("function() {}", null);
    assertSame(expected, methodCompilerPass.methodDefinitions.get("Foo.bar"));
  }

  @Test(expected = IllegalStateException.class)
  public void testSignatureStoreAddSignatureTwice() {
    Node root = createNode("Foo.bar = function() {}", null);
    methodCompilerPass.process(null, root);
    methodCompilerPass.getSignatureStore().addSignature("Foo.bar", createNode("function() {}", null), "test.js");
    methodCompilerPass.getSignatureStore().addSignature("Foo.bar", createNode("function() {}", null), "test.js");
  }

  @Test
  public void testSignatureStoreRemoveSignature() {
    when(signatureStore.removeSignature("Foo.bar")).thenReturn(void.class);
    Node root = createNode("Foo.bar = function() {}", null);
    methodCompilerPass.process(null, root);
    methodCompilerPass.getSignatureStore().removeSignature("Foo.bar");
    assertNull(methodCompilerPass.methodDefinitions.get("Foo.bar"));
  }

  private Node createNode(String code, String sourceFile) {
    Node node = new Node();
    node.setSourceFile(sourceFile);
    node.setCode(code);
    node.setType(Token.PROGRAM);
    return node;
  }

  private Node createNode(String code, Node parent) {
    Node node = new Node();
    node.setCode(code);
    node.setParent(parent);
    return node;
  }

  private Node createNode(String code) {
    return createNode(code, null);
  }
}