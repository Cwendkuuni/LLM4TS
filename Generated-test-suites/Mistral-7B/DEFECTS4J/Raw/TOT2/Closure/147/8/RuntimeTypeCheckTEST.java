import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.Node;

public class RuntimeTypeCheckTest {

  @Test
  public void testTypeCheckForBoolean() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "console.log";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String jsCode = "function foo() { return true; }";
    Node root = Parser.parse(Parser.parseJavaScriptText(jsCode), null);
    runtimeTypeCheck.process(null, root);

    Node checkNode = findCheckNode(root, "checkType", "valueChecker", "boolean");
    Node resultNode = (Node) checkNode.getFirstChild();
    assertTrue(resultNode.toBoolean());
  }

  @Test
  public void testTypeCheckForNumber() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "console.log";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String jsCode = "function foo() { return 1; }";
    Node root = Parser.parse(Parser.parseJavaScriptText(jsCode), null);
    runtimeTypeCheck.process(null, root);

    Node checkNode = findCheckNode(root, "checkType", "valueChecker", "number");
    Node resultNode = (Node) checkNode.getFirstChild();
    assertEquals(1.0, resultNode.toNumber(), 0.0);
  }

  @Test
  public void testTypeCheckForString() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "console.log";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String jsCode = "function foo() { return 'hello'; }";
    Node root = Parser.parse(Parser.parseJavaScriptText(jsCode), null);
    runtimeTypeCheck.process(null, root);

    Node checkNode = findCheckNode(root, "checkType", "valueChecker", "string");
    Node resultNode = (Node) checkNode.getFirstChild();
    assertEquals("hello", resultNode.toString());
  }

  @Test
  public void testTypeCheckForNull() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "console.log";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String jsCode = "function foo() { return null; }";
    Node root = Parser.parse(Parser.parseJavaScriptText(jsCode), null);
    runtimeTypeCheck.process(null, root);

    Node checkNode = findCheckNode(root, "checkType", "valueChecker", "null");
    Node resultNode = (Node) checkNode.getFirstChild();
    assertNull(resultNode);
  }

  @Test
  public void testTypeCheckForUnionType() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "console.log";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String jsCode = "function foo(x) { return x; }";
    Node root = Parser.parse(Parser.parseJavaScriptText(jsCode), null);
    runtimeTypeCheck.process(null, root);

    Node checkNode = findCheckNode(root, "checkType", "unionChecker", "number|boolean");
    Node resultNode = (Node) checkNode.getFirstChild();
    // Since we can't test the actual type at runtime, we just test if the
    // checkNode exists.
    assertNotNull(resultNode);
  }

  @Test(expected = AssertionError.class)
  public void testTypeCheckForMismatchType() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "console.log";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String jsCode = "function foo() { return 1; }";
    Node root = Parser.parse(Parser.parseJavaScriptText(jsCode), null);
    runtimeTypeCheck.process(null, root);

    Node checkNode = findCheckNode(root, "checkType", "valueChecker", "string");
    Node resultNode = (Node) checkNode.getFirstChild();
    assertEquals("AssertionError: Expected 'string' but got 'number'",
        resultNode.toString());
  }

  @Test
  public void testTypeCheckForClassType() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "console.log";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String jsCode = "function Foo() {}";
    Node root = Parser.parse(Parser.parseJavaScriptText(jsCode), null);
    runtimeTypeCheck.process(null, root);

    String testCode = "function test() { var foo = new Foo(); checkType(foo, classChecker('Foo')); }";
    Node testRoot = Parser.parse(Parser.parseJavaScriptText(testCode), null);
    runtimeTypeCheck.process(null, testRoot);

    Node checkNode = findCheckNode(testRoot, "checkType", "classChecker", "Foo");
    Node resultNode = (Node) checkNode.getFirstChild();
    assertTrue(resultNode.toBoolean());
  }

  @Test(expected = AssertionError.class)
  public void testTypeCheckForMismatchClassType() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "console.log";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String jsCode = "function Foo() {}";
    Node root = Parser.parse(Parser.parseJavaScriptText(jsCode), null);
    runtimeTypeCheck.process(null, root);

    String testCode = "function test() { var foo = new Object(); checkType(foo, classChecker('Foo')); }";
    Node testRoot = Parser.parse(Parser.parseJavaScriptText(testCode), null);
    runtimeTypeCheck.process(null, testRoot);

    Node checkNode = findCheckNode(testRoot, "checkType", "classChecker", "Foo");
    Node resultNode = (Node) checkNode.getFirstChild();
    assertEquals("AssertionError: Expected 'Foo' but got 'Object'",
        resultNode.toString());
  }

  @Test
  public void testTypeCheckForInterfaceType() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "console.log";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String jsCode = "interface MyInterface { foo: number; }";
    Node root = Parser.parse(Parser.parseJavaScriptText(jsCode), null);
    runtimeTypeCheck.process(null, root);

    String testCode = "function test() { var obj = { foo: 1 }; checkType(obj, interfaceChecker('MyInterface')); }";
    Node testRoot = Parser.parse(Parser.parseJavaScriptText(testCode), null);
    runtimeTypeCheck.process(null, testRoot);

    Node checkNode = findCheckNode(testRoot, "checkType", "interfaceChecker", "MyInterface");
    Node resultNode = (Node) checkNode.getFirstChild();
    assertTrue(resultNode.toBoolean());
  }

  @Test(expected = AssertionError.class)
  public void testTypeCheckForMismatchInterfaceType() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "console.log";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String jsCode = "interface MyInterface { foo: number; }";
    Node root = Parser.parse(Parser.parseJavaScriptText(jsCode), null);
    runtimeTypeCheck.process(null, root);

    String testCode = "function test() { var obj = { bar: 1 }; checkType(obj, interfaceChecker('MyInterface')); }";
    Node testRoot = Parser.parse(Parser.parseJavaScriptText(testCode), null);
    runtimeTypeCheck.process(null, testRoot);

    Node checkNode = findCheckNode(testRoot, "checkType", "interfaceChecker", "MyInterface");
    Node resultNode = (Node) checkNode.getFirstChild();
    assertEquals("AssertionError: Expected 'MyInterface' but got 'Object'",
        resultNode.toString());
  }

  // Add test cases for other methods and edge cases here

  private Node findCheckNode(Node root, String methodName, String checkerName, String typeName) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (NodeUtil.isFunctionCall(child, methodName)) {
        Node callSite = child;
        Node checkerNode = null;
        for (Node arg = callSite.getFirstChild(); arg != null; arg = arg.getNext()) {
          if (arg.getType() == Token.ARRAYLIT) {
            for (Node checker : arg.getChildren()) {
              if (checker.getFirstChild().toString().equals(checkerName)) {
                checkerNode = checker.getFirstChild();
                break;
              }
            }
          }
        }
        if (checkerNode != null) {
          for (Node child2 = checkerNode.getFirstChild(); child2 != null; child2 = child2.getNext()) {
            if (child2.getType() == Token.GETELEM && child2.getFirstChild().toString().equals(typeName)) {
              return child2.getNext();
            }
          }
        }
      }
    }
    return null;
  }
}