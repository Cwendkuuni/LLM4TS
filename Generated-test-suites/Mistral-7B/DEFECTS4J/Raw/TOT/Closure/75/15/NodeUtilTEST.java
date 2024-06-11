import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Context.getCurrentContext().newNode(
        "for (var i in [1, 2, 3]) { }");
    assertTrue(isForIn(node));

    node = Context.getCurrentContext().newNode("for (var i = 0; i < 3; i++) { }");
    assertFalse(isForIn(node));

    node = Context.getCurrentContext().newNode("for (var i = 0; i < 3; ) { }"); // missing condition
    assertFalse(isForIn(node));

    node = Context.getCurrentContext().newNode("for (; i < 3; i++) { }"); // empty for statement
    assertFalse(isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = Context.getCurrentContext().newNode("if (true) { }");
    assertTrue(isStatement(node));

    node = Context.getCurrentContext().newNode("let x = 5;");
    assertTrue(isStatement(node));

    node = Context.getCurrentContext().newNode("5 + 3");
    assertFalse(isStatement(node));

    node = Context.getCurrentContext().newNode("(function() {})();"); // IIFE
    assertFalse(isStatement(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node node = Context.getCurrentContext().newNode("function foo() { }");
    assertTrue(isFunctionDeclaration(node));

    node = Context.getCurrentContext().newNode("function bar(x) { return x * x; }");
    assertTrue(isFunctionDeclaration(node));

    node = Context.getCurrentContext().newNode("(function() {})"); // IIFE
    assertFalse(isFunctionDeclaration(node));

    node = Context.getCurrentContext().newNode("let foo = function() { }"); // ES6 arrow function declaration
    assertFalse(isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Context.getCurrentContext().newNode(
        "function foo() { }" +
        "var x = 5;");
    Node functionDeclaration = scriptNode.getChild(0);
    assertTrue(isHoistedFunctionDeclaration(functionDeclaration));

    Node functionExpressionNode = Context.getCurrentContext().newNode(
        "var foo = function() { }" +
        "var x = 5;");
    Node functionExpression = functionExpressionNode.getChild(0);
    assertFalse(isHoistedFunctionDeclaration(functionExpression));

    Node functionDeclarationInFunctionNode = Context.getCurrentContext().newNode(
        "function bar(x) {" +
        "  function foo() { }" +
        "  return x * x;" +
        "}" +
        "var x = 5;");
    Node functionDeclarationInFunction = functionDeclarationInFunctionNode.getChild(0);
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationInFunction));

    Node scriptInFunctionNode = Context.getCurrentContext().newNode(
        "(function() {" +
        "  function foo() { }" +
        "  var x = 5;" +
        "})()");
    Node functionDeclarationInScript = scriptInFunctionNode.getChild(0).getChild(0);
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInScript));
  }
}