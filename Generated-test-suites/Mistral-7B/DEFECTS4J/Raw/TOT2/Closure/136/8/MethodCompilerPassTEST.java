import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.jscomp.NodeTraversal.Callback.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

public class MethodCompilerPassTest {
  private Compiler compiler;
  private MethodCompilerPass methodCompilerPass;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        // Implement a mock SignatureStore for testing
        return new SignatureStore() {
          Multimap<String, Node> signatures = HashMultimap.create();

          @Override
          public void reset() {}

          @Override
          public void addSignature(String functionName, Node functionNode, String sourceFile) {
            signatures.put(functionName, functionNode);
          }

          @Override
          public void removeSignature(String functionName) {
            signatures.remove(functionName);
          }

          public Node getFunctionByName(String functionName) {
            return signatures.get(functionName).get(0);
          }
        };
      }

      @Override
      Callback getActingCallback() {
        // Implement a no-op acting callback for testing
        return new Callback() {
          @Override
          public void process(Node externs, Node root) {}
        };
      }
    };
  }

  @Test
  public void testMethodCompilerPass_ExternMethods() {
    String sourceCode =
        "// externs file\n" +
        "function foo() {} \n" +
        "function bar() {} \n" +
        "function baz() {}";

    Node externs = Node.fromString(sourceCode, "Test", null);

    methodCompilerPass.process(externs, null);

    Node foo = methodCompilerPass.getSignatureStore().getFunctionByName("foo");
    Node bar = methodCompilerPass.getSignatureStore().getFunctionByName("bar");
    Node baz = methodCompilerPass.getSignatureStore().getFunctionByName("baz");

    assertNotNull("Expected foo method definition", foo);
    assertNotNull("Expected bar method definition", bar);
    assertNotNull("Expected baz method definition", baz);
  }

  @Test
  public void testMethodCompilerPass_FunctionDeclaration() {
    String sourceCode =
        "function foo(a, b, c) {\n" +
        "  // function body\n" +
        "}";

    Node root = Node.fromString(sourceCode, "Test", null);

    methodCompilerPass.process(null, root);

    Node foo = methodCompilerPass.getSignatureStore().getFunctionByName("foo");

    assertNotNull("Expected foo method definition", foo);
    assertEquals("Unexpected number of arguments in foo method definition", 3, foo.getNumChildren());
  }

  @Test
  public void testMethodCompilerPass_Prototype() {
    String sourceCode =
        "function Foo() {\n" +
        "  this.bar = function() {\n" +
        "    // function body\n" +
        "  };\n" +
        "}\n" +
        "Foo.prototype.getBar = function() {\n" +
        "  // function body\n" +
        "}";

    Node root = Node.fromString(sourceCode, "Test", null);

    methodCompilerPass.process(null, root);

    Node foo = methodCompilerPass.getSignatureStore().getFunctionByName("Foo");
    Node bar = methodCompilerPass.getSignatureStore().getFunctionByName("getBar");

    assertNotNull("Expected Foo constructor definition", foo);
    assertNotNull("Expected getBar method definition", bar);
  }

  @Test
  public void testMethodCompilerPass_FunctionExpression() {
    String sourceCode =
        "function foo() {\n" +
        "  // function body\n" +
        "}\n" +
        "var bar = function() {\n" +
        "  // function body\n" +
        "}";

    Node root = Node.fromString(sourceCode, "Test", null);

    methodCompilerPass.process(null, root);

    Node foo = methodCompilerPass.getSignatureStore().getFunctionByName("foo");
    Node bar = methodCompilerPass.getSignatureStore().getFunctionByName("bar");

    assertNotNull("Expected foo function definition", foo);
    assertNotNull("Expected bar function definition", bar);
  }

  @Test
  public void testMethodCompilerPass_FunctionCall() {
    String sourceCode =
        "function foo(a, b, c) {\n" +
        "  // function body\n" +
        "}\n" +
        "function bar() {\n" +
        "  foo(1, 2, 3);\n" +
        "}";

    Node root = Node.fromString(sourceCode, "Test", null);

    methodCompilerPass.process(null, root);

    Node foo = methodCompilerPass.getSignatureStore().getFunctionByName("foo");
    Node bar = methodCompilerPass.getSignatureStore().getFunctionByName("bar");

    assertNotNull("Expected foo method definition", foo);
    assertNotNull("Expected bar method definition", bar);
  }

  @Test(expected = IllegalStateException.class)
  public void testMethodCompilerPass_UndefinedFunction() {
    String sourceCode =
        "function foo() {\n" +
        "  // function body\n" +
        "}\n" +
        "function bar() {\n" +
        "  undefined.baz();\n" +
        "}";

    Node root = Node.fromString(sourceCode, "Test", null);

    methodCompilerPass.process(null, root);

    Node bar = methodCompilerPass.getSignatureStore().getFunctionByName("bar");

    assertNotNull("Expected bar method definition", bar);
  }

  @Test
  public void testMethodCompilerPass_IgnoredMethods() {
    String sourceCode =
        "function foo() {} \n" +
        "function bar() {} \n" +
        "function baz() {} \n" +
        "{\n" +
        "  foo();\n" +
        "  bar();\n" +
        "  baz();\n" +
        "}\n" +
        "function qux() {};";

    Node root = Node.fromString(sourceCode, "Test", null);

    methodCompilerPass.process(null, root);

    Set<String> ignoredMethods = methodCompilerPass.externMethodsWithoutSignatures;

    assertTrue("Expected ignoredMethods to contain qux", ignoredMethods.contains("qux"));
    assertFalse("Expected ignoredMethods not to contain foo", ignoredMethods.contains("foo"));
  }

  @Test
  public void testMethodCompilerPass_NonMethodProperties() {
    String sourceCode =
        "function foo() {\n" +
        "  // function body\n" +
        "}\n" +
        "var bar = 1;\n" +
        "var baz = {}";

    Node root = Node.fromString(sourceCode, "Test", null);

    methodCompilerPass.process(null, root);

    Set<String> nonMethodProperties = methodCompilerPass.nonMethodProperties;

    assertTrue("Expected nonMethodProperties to contain baz", nonMethodProperties.contains("baz"));
    assertFalse("Expected nonMethodProperties not to contain foo", nonMethodProperties.contains("foo"));
  }
}