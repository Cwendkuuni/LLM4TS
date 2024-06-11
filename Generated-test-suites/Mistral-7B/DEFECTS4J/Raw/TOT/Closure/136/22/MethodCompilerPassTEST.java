import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.google.common.truth.Truth.assertThat;

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
        return new DummyCallback();
      }
    };
  }

  @Test
  public void testExternsMethodsAreAddedToMethodDefinitions() {
    String sourceName = "test.js";

    Node externs = createNode("var foo = function() {};");
    methodCompilerPass.process(externs, null);

    verify(signatureStore, times(1)).addSignature("foo", anyNode(), sourceName);
    assertThat(methodCompilerPass.methodDefinitions.get("foo")).isInstanceOf(Node.class);
  }

  @Test
  public void testExternsMethodsWithoutSignaturesAreIgnored() {
    String sourceName = "test.js";

    Node externs = createNode("var foo;");
    methodCompilerPass.process(externs, null);

    verify(signatureStore, never()).addSignature("foo", anyNode(), sourceName);
    verify(signatureStore, never()).removeSignature("foo");
    assertThat(methodCompilerPass.nonMethodProperties).containsExactly("foo");
  }

  @Test
  public void testMethodsInObjectLiteralAreAddedToMethodDefinitions() {
    String sourceName = "test.js";

    Node root = createNode("var obj = {foo: function() {}};");
    methodCompilerPass.process(null, root);

    verify(signatureStore, times(1)).addSignature("foo", anyNode(), sourceName);
    assertThat(methodCompilerPass.methodDefinitions.get("foo")).isInstanceOf(Node.class);
  }

  @Test
  public void testMethodsInPrototypeAreAddedToMethodDefinitions() {
    String sourceName = "test.js";

    Node root = createNode("var Foo = function() {}; Foo.prototype.bar = function() {};");
    methodCompilerPass.process(null, root);

    verify(signatureStore, times(1)).addSignature("bar", anyNode(), sourceName);
    assertThat(methodCompilerPass.methodDefinitions.get("bar")).isInstanceOf(Node.class);
  }

  @Test
  public void testMethodsInParentPrototypeAreAddedToMethodDefinitions() {
    String sourceName = "test.js";

    Node root = createNode("var Foo = function() {}; Foo.prototype = {getBar: function() {}};");
    methodCompilerPass.process(null, root);

    verify(signatureStore, times(1)).addSignature("getBar", anyNode(), sourceName);
    assertThat(methodCompilerPass.methodDefinitions.get("getBar")).isInstanceOf(Node.class);
  }

  @Test
  public void testMethodsInPrototypeWithSameNameAsExternsAreReplaced() {
    String sourceName = "test.js";

    Node externs = createNode("var foo = function() {};");
    Node root = createNode("var Foo = function() {}; Foo.prototype.foo = function() {};");

    methodCompilerPass.process(externs, root);

    verify(signatureStore, times(1)).addSignature("foo", anyNode(), sourceName);
    assertThat(methodCompilerPass.methodDefinitions.get("foo")).isInstanceOf(Node.class);
    assertThat(methodCompilerPass.methodDefinitions.size()).isEqualTo(1);
  }

  @Test
  public void testMethodsInPrototypeWithDifferentNameThanExternsAreAdded() {
    String sourceName = "test.js";

    Node externs = createNode("var foo = function() {};");
    Node root = createNode("var Foo = function() {}; Foo.prototype.bar = function() {};");

    methodCompilerPass.process(externs, root);

    verify(signatureStore, times(1)).addSignature("bar", anyNode(), sourceName);
    assertThat(methodCompilerPass.methodDefinitions.get("bar")).isInstanceOf(Node.class);
    assertThat(methodCompilerPass.methodDefinitions.size()).isGreaterThan(1);
  }

  @Test
  public void testGetExternsMethodsCallbackIsCalled() {
    Node externs = createNode("var foo = function() {};");

    methodCompilerPass.process(externs, null);

    verify(compiler, times(1)).traverse(eq(externs), any(Callback.class));
  }

  @Test
  public void testGatherSignaturesCallbackIsCalled() {
    String sourceName = "test.js";
    Node root = createNode("var Foo = function() {}; Foo.prototype.bar = function() {};");

    methodCompilerPass.process(null, root);

    verify(compiler, times(1)).traverseRoots(
        eq(Arrays.asList(null, root)), any(List.class), any(Callback.class));
  }

  private Node createNode(String nodeContent) {
    RhinoEngine engine = new RhinoEngine();
    return engine.compile(nodeContent).getRootNode();
  }

  private static class DummyCallback implements Callback {}
}