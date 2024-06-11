import org.junit.Test;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() {
    Compiler compiler = new Compiler();
    String logFunction = "function(warning, expr) {\n" +
        "  System.out.println('Warning: ' + warning + ': ' + expr);\n" +
        "}";

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);
    Node rootNode = Node.newText("function foo() {}", null, -1, -1);
    runtimeTypeCheck.process(null, rootNode);

    // Test normal case
    Node functionNode = NodeUtil.findNodeByName(rootNode, "foo");
    Node callNode = functionNode.getFirstChild();
    Node result = callNode.getLastChild();

    assert (result.getType() == Token.NULL_TYPE);

    // Test type checking a string
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("function bar() { return 'hello'; }", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "bar").getFirstChild();
    result = callNode.getLastChild();

    assert (result.getType() == Token.STRING_VALUE_TYPE);

    // Test type checking a number
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("function baz() { return 42; }", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "baz").getFirstChild();
    result = callNode.getLastChild();

    assert (result.getType() == Token.NUMBER_VALUE_TYPE);

    // Test type checking a boolean
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("function qux() { return true; }", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "qux").getFirstChild();
    result = callNode.getLastChild();

    assert (result.getType() == Token.BOOLEAN_VALUE_TYPE);

    // Test type checking a user-defined class
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("class Foo {}", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "new Foo").getFirstChild();
    result = callNode.getLastChild();

    assert (result.getType().isObjectType());

    // Test type checking an interface
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("interface Bar {}", null, -1, -1));
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("class Foo implements Bar {}", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "new Foo").getFirstChild();
    result = callNode.getLastChild();

    assert (result.getType().implementsInterface("Bar"));

    // Test type checking a user-defined constructor
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("class Foo {\n" +
            "  constructor(x: number) {}\n" +
            "}", null, -1, -1));
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("function test() { new Foo(42); }", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "test").getFirstChild();
    result = callNode.getLastChild();

    assert (result.getType().isInstanceType());

    // Test type checking a null value
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("function test() { return null; }", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "test").getFirstChild();
    result = callNode.getLastChild();

    assert (result.getType() == Token.NULL_TYPE);

    // Test type checking a void value
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("function test() { return; }", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "test").getFirstChild();
    result = callNode.getLastChild();

    assert (result.getType() == Token.VOID_TYPE);

    // Test error cases: user-defined type not found
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("class NotFound {}", null, -1, -1));
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("function test() { new NotFound(); }", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "test").getFirstChild();
    try {
      result = callNode.getLastChild();
      fail("Expected a type error");
    } catch (AssertionError e) {
      // Expected
    }

    // Test error cases: unknown type
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("function test() { return unknown; }", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "test").getFirstChild();
    try {
      result = callNode.getLastChild();
      fail("Expected a type error");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test(expected = AssertionError.class)
  public void testErrorCases() {
    Compiler compiler = new Compiler();
    String logFunction = "function(warning, expr) {\n" +
        "  throw new Error('Error: ' + warning + ': ' + expr);\n" +
        "}";

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);
    Node rootNode = Node.newText("function foo() {}", null, -1, -1);
    runtimeTypeCheck.process(null, rootNode);

    Node functionNode = NodeUtil.findNodeByName(rootNode, "foo");
    Node callNode = functionNode.getFirstChild();
    Node result = callNode.getLastChild();

    // This should trigger an error due to an unrecognized type
    rootNode.replaceChild(NodeUtil.getLastChild(rootNode),
        Node.newText("function bar() { return [1, 2]; }", null, -1, -1));
    runtimeTypeCheck.process(null, rootNode);

    callNode = NodeUtil.findNodeByName(rootNode, "bar").getFirstChild();
    result = callNode.getLastChild();
  }
}