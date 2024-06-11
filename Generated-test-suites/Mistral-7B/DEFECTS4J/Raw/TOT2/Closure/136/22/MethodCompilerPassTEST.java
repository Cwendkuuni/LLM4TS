import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MethodCompilerPassTest {
  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    signatureStore = mock(MethodCompilerPass.SignatureStore.class);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        // No operation in this test
        return new Callback() {
          @Override
          public void call(Node node) {}
        };
      }
    };
  }

  @Test
  public void testSimpleMethodDeclaration() {
    Node node = NodeOperations.newTextNode("function foo() {}");
    methodCompilerPass.process(null, node);
    verify(signatureStore, times(1)).addSignature(eq("foo"), eq(node), anyString());
  }

  @Test
  public void testMethodDeclarationWithParameters() {
    Node node = NodeOperations.newTextNode("function foo(a, b) {}");
    methodCompilerPass.process(null, node);
    verify(signatureStore, times(1)).addSignature(eq("foo"), eq(node), anyString());
  }

  @Test
  public void testMethodDeclarationWithEmptyParentheses() {
    Node node = NodeOperations.newTextNode("function foo() {}");
    methodCompilerPass.process(null, node);
    verify(signatureStore, times(1)).addSignature(eq("foo"), eq(node), anyString());
  }

  @Test
  public void testMethodDeclarationWithExternName() {
    Node externs = NodeOperations.newTextNode("function bar() {}");
    methodCompilerPass.process(externs, null);
    assertEquals(Sets.newHashSet("bar"), methodCompilerPass.externMethods);
  }

  @Test
  public void testMethodDeclarationWithoutSignature() {
    Node externs = NodeOperations.newTextNode("foo");
    methodCompilerPass.process(externs, null);
    assertEquals(Sets.newHashSet("foo"), methodCompilerPass.externMethodsWithoutSignatures);
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedMethod() {
    Node node = NodeOperations.newTextNode("undefined.foo()");
    methodCompilerPass.process(null, node);
  }

  @Test
  public void testMethodDeclarationWithPrototype() {
    Node node = NodeOperations.newTextNode("function Foo() {} Foo.prototype.bar = function() {}");
    methodCompilerPass.process(null, node);
    verify(signatureStore, times(1)).addSignature(eq("Foo.bar"), eq(anyNode()), anyString());
  }

  @Test
  public void testMethodDeclarationWithPrototypeAndExtern() {
    Node externs = NodeOperations.newTextNode("function Foo() {} Foo.prototype.bar = function() {}");
    methodCompilerPass.process(externs, null);
    assertEquals(Sets.newHashSet("Foo.bar"), methodCompilerPass.externMethods);
  }

  @Test
  public void testMethodDeclarationWithPrototypeAndExternWithoutSignature() {
    Node externs = NodeOperations.newTextNode("function Foo() {} Foo.prototype.bar = function() {}");
    methodCompilerPass.process(externs, null);
    assertEquals(Sets.newHashSet("Foo.bar"), methodCompilerPass.externMethodsWithoutSignatures);
  }
}