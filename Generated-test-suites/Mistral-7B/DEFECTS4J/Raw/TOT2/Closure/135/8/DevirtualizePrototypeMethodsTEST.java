import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;

public class DevirtualizePrototypeMethodsTest {
  @Test
  public void testDevirtualizePrototypeMethods() {
    Compiler compiler = new Compiler();
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    // Create test code
    String testCode = "var a = {};\n" +
        "a.prototype.add = function(x, y) {\n" +
        "  return x + y;\n" +
        "};\n" +
        "var result = a.add(1, 2);\n";

    // Compile test code
    Node root = new Node(Node.SCOPE, null);
    Node externs = new Node(Node.SCOPE, null);
    devirtualizePrototypeMethods.process(externs, root);
    compiler.compile(externs, root);

    // Run test and check result
    Node testResult = root.getFirstChild();
    Node testCodeNode = testResult.getFirstChild();
    Node testCodeScope = testCodeNode.getFirstChild();
    Node testFunctionCallNode = testCodeScope.getLastChild();
    Object result = compiler.runScriptlet(testFunctionCallNode);
    assertEquals(3, result);
  }

  @Test
  public void testDevirtualizePrototypeMethods_EdgeCase() {
    Compiler compiler = new Compiler();
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    // Create test code
    String testCode = "var a = {};\n" +
        "a.prototype.add = function(x, y) {\n" +
        "  return x + y * 2;\n" +
        "};\n" +
        "var result = a.add(1, 2);\n";

    // Compile test code
    Node root = new Node(Node.SCOPE, null);
    Node externs = new Node(Node.SCOPE, null);
    devirtualizePrototypeMethods.process(externs, root);
    compiler.compile(externs, root);

    // Run test and check result
    Node testResult = root.getFirstChild();
    Node testCodeNode = testResult.getFirstChild();
    Node testCodeScope = testCodeNode.getFirstChild();
    Node testFunctionCallNode = testCodeScope.getLastChild();
    Object result = compiler.runScriptlet(testFunctionCallNode);
    assertEquals(7, result);
  }

  @Test(expected = RuntimeException.class)
  public void testDevirtualizePrototypeMethods_Exception() {
    Compiler compiler = new Compiler();
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    // Create test code with error
    String testCode = "var a = {};\n" +
        "a.prototype.add = function(x, y) {\n" +
        "  throw new Error('Error message');\n" +
        "};\n" +
        "a.add(1, 2);\n";

    // Compile test code
    Node root = new Node(Node.SCOPE, null);
    Node externs = new Node(Node.SCOPE, null);
    devirtualizePrototypeMethods.process(externs, root);
    compiler.compile(externs, root);
  }
}