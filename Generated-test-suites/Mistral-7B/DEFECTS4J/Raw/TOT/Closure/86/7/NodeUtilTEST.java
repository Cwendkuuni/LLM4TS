import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Context.getCurrentContext().newNode("for (var i in array) {}", Token.FOR);
    assertTrue(isForIn(node));

    node.setType(Token.IF);
    assertFalse(isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = Context.getCurrentContext().newNode("1 + 1;", Token.EXPRESSION_STATEMENT);
    assertTrue(isStatement(node));

    Node parent = Context.getCurrentContext().newNode("if (true) {}", Token.IF);
    parent.addChild(node);
    assertFalse(isStatement(node)); // 1 + 1; is an expression statement inside an if statement
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node node = Context.getCurrentContext().newNode("function foo() {}", Token.FUNCTION);
    assertTrue(isFunctionDeclaration(node));

    Node parent = Context.getCurrentContext().newNode("1 + 1;", Token.EXPRESSION_STATEMENT);
    parent.addChild(node);
    assertFalse(isFunctionDeclaration(node)); // function foo() {} is a function declaration but not a statement
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Context.getCurrentContext().newNode("function foo() {}", Token.SCRIPT);
    Node functionNode = scriptNode.getChild(0);
    assertTrue(isHoistedFunctionDeclaration(functionNode));

    Node functionExpressionNode = Context.getCurrentContext().newNode("function bar() {}", Token.FUNCTION, Token.FUNCTION_EXPRESSION);
    Node parent = Context.getCurrentContext().newNode("1 + 1;", Token.EXPRESSION_STATEMENT);
    parent.addChild(functionExpressionNode);
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode)); // function bar() {} is a function expression, not a declaration

    Node hoistedFunctionExpressionNode = Context.getCurrentContext().newNode("function hoistedBar() {}", Token.FUNCTION, Token.FUNCTION_EXPRESSION);
    scriptNode = Context.getCurrentContext().newNode("hoistedBar = function hoistedBar() {}", Token.SCRIPT);
    parent = scriptNode.getChild(0);
    parent.setFirstChild(hoistedFunctionExpressionNode);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionExpressionNode)); // hoistedBar = function hoistedBar() {} is hoisted function expression
  }
}