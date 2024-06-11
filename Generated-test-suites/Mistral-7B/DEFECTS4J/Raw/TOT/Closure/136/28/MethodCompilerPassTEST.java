import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodCompilerPassTest {

  @Test
  public void testSimpleMethodDeclaration() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SignatureStore signatureStore = mock(SignatureStore.class);
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node) {
            // No-op
          }
        };
      }
    };

    Node externs = createNode("var foo = function() {};");
    Node root = createNode("function bar(x, y) {}");

    methodCompilerPass.process(externs, root);

    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("bar", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertNotNull(methodCompilerPass.methodDefinitions.get("bar"));
  }

  @Test
  public void testMethodDeclarationInExterns() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SignatureStore signatureStore = mock(SignatureStore.class);
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node) {
            // No-op
          }
        };
      }
    };

    Node externs = createNode("var foo = function(x, y) {};");
    Node root = createNode("function bar(x, y) {}");

    methodCompilerPass.process(externs, root);

    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("foo", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertNotNull(methodCompilerPass.methodDefinitions.get("foo"));
  }

  @Test
  public void testMethodDeclarationWithoutSignature() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SignatureStore signatureStore = mock(SignatureStore.class);
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node) {
            // No-op
          }
        };
      }
    };

    Node externs = createNode("var foo;");
    Node root = createNode("function bar(x, y) {}");

    methodCompilerPass.process(externs, root);

    assertEquals(0, methodCompilerPass.methodDefinitions.size());
    assertFalse(methodCompilerPass.externMethods.contains("foo"));
  }

  @Test
  public void testNonMethodProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SignatureStore signatureStore = mock(SignatureStore.class);
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node) {
            // No-op
          }
        };
      }
    };

    Node externs = createNode("var foo = 42;");
    Node root = createNode("function bar(x, y) {}");

    methodCompilerPass.process(externs, root);

    assertEquals(0, methodCompilerPass.methodDefinitions.size());
    assertFalse(methodCompilerPass.externMethods.contains("foo"));
    assertFalse(methodCompilerPass.nonMethodProperties.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.contains("foo"));
  }

  @Test
  public void testExternMethodWithSignature() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SignatureStore signatureStore = mock(SignatureStore.class);
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node) {
            // No-op
          }
        };
      }
    };

    Node externs = createNode("var foo = function(x, y) {};");
    Node root = createNode("function bar(x, y) {}");

    methodCompilerPass.process(externs, root);

    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("foo", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertNotNull(methodCompilerPass.methodDefinitions.get("foo"));
    assertFalse(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethods.contains("foo"));
  }

  @Test
  public void testExternMethodWithoutSignature() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    SignatureStore signatureStore = mock(SignatureStore.class);
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node) {
            // No-op
          }
        };
      }
    };

    Node externs = createNode("var foo;");
    Node root = createNode("function bar(x, y) {}");

    methodCompilerPass.process(externs, root);

    assertEquals(0, methodCompilerPass.methodDefinitions.size());
    assertFalse(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("foo"));
  }

  private Node createNode(String nodeContent) {
    RhinoCompilerOptions options = new RhinoCompilerOptions();
    RhinoCompiler compiler = new RhinoCompiler(options);
    NodeFactory nodeFactory = new NodeFactory();
    Node node = compiler.compile(nodeContent);
    return nodeFactory.makeTreeFromString(nodeContent, null, node);
  }
}