import static org.junit.Assert.*;

import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import org.junit.Test;

public class MethodCompilerPassTest {
  

  @Test
  public void testProcess_NullExternsAndRoot() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }
    };

    methodCompilerPass.process(null, null);
  }

  @Test
  public void testProcess_NullExterns() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // Empty implementation
      }
    };

    methodCompilerPass.process(null, new Node("root"));
  }

  @Test
  public void testProcess_NullRoot() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // Empty implementation
      }
    };

    methodCompilerPass.process(new Node("externs"), null);
  }

  @Test
  public void testProcess_EmptyExternsAndRoot() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // Empty implementation
      }
    };

    methodCompilerPass.process(new Node("externs"), new Node("root"));
  }

  @Test
  public void testProcess_ExternsWithMethods() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          Set<String> functionNames = Sets.newHashSet();

          @Override
          public void reset() {
            functionNames.clear();
          }

          @Override
          public void addSignature(String functionName, Node functionNode, String sourceFile) {
            functionNames.add(functionName);
          }

          @Override
          public void removeSignature(String functionName) {
            // Empty implementation
          }
        };
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // Empty implementation
      }
    };

    Node externs = new Node("externs", "{\n" +
        "  Foo.prototype.bar = function() {}\n" +
        "  Foo.qux = function() {}\n" +
        "  Foo.prototype.qux = function() {}\n" +
        "}");

    methodCompilerPass.process(externs, new Node("root"));

    Set<String> expectedFunctionNames = Sets.newHashSet("bar", "qux");
    assertEquals(expectedFunctionNames, methodCompilerPass.getSignatureStore().functionNames);
  }

  @Test
  public void testProcess_RootWithMethods() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          Set<String> functionNames = Sets.newHashSet();

          @Override
          public void reset() {
            functionNames.clear();
          }

          @Override
          public void addSignature(String functionName, Node functionNode, String sourceFile) {
            functionNames.add(functionName);
          }

          @Override
          public void removeSignature(String functionName) {
            // Empty implementation
          }
        };
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // Empty implementation
      }
    };

    Node root = new Node("root", "function Foo() {\n" +
        "  this.bar = function() {}\n" +
        "  this.qux = function() {}\n" +
        "}");

    methodCompilerPass.process(null, root);

    Set<String> expectedFunctionNames = Sets.newHashSet("bar", "qux");
    assertEquals(expectedFunctionNames, methodCompilerPass.getSignatureStore().functionNames);
  }

  @Test
  public void testProcess_ExternsAndRootWithMethods() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          Set<String> functionNames = Sets.newHashSet();

          @Override
          public void reset() {
            functionNames.clear();
          }

          @Override
          public void addSignature(String functionName, Node functionNode, String sourceFile) {
            functionNames.add(functionName);
          }

          @Override
          public void removeSignature(String functionName) {
            // Empty implementation
          }
        };
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // Empty implementation
      }
    };

    Node externs = new Node("externs", "{\n" +
        "  Foo.prototype.bar = function() {}\n" +
        "  Foo.qux = function() {}\n" +
        "}");

    Node root = new Node("root", "function Foo() {\n" +
        "  this.bar = function() {}\n" +
        "  this.qux = function() {}\n" +
        "}");

    methodCompilerPass.process(externs, root);

    Set<String> expectedFunctionNames = Sets.newHashSet("bar", "qux");
    assertEquals(expectedFunctionNames, methodCompilerPass.getSignatureStore().functionNames);
  }

  
}

class SignatureStore {
  Set<String> functionNames = Sets.newHashSet();
}