import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Context.getCurrentContext().newNode("for (var i in array) {}", Token.FOR);
    assertTrue(isForIn(node));

    Node invalidNode = Context.getCurrentContext().newNode("if (true) {}", Token.IF);
    assertFalse(isForIn(invalidNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Context.getCurrentContext().newNode("print('Hello World');", Token.PRINT);
    assertTrue(isStatement(statementNode));

    Node expressionNode = Context.getCurrentContext().newNode("5 + 3", Token.ADD);
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Context.getCurrentContext().newNode(
        "function foo() { print('Hello World'); }", Token.FUNCTION);
    assertTrue(isFunctionDeclaration(functionDeclaration));

    Node functionExpression = Context.getCurrentContext().newNode("function bar() {}", Token.FUNCTION);
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = Context.getCurrentContext().newNode(
        "function foo() { print('Hello World'); }", Token.FUNCTION);
    Node scriptNode = Context.getCurrentContext().newNode("//script", Token.SCRIPT);
    hoistedFunctionDeclaration.setParent(scriptNode);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node notHoistedFunctionDeclaration = Context.getCurrentContext().newNode(
        "function foo() { print('Hello World'); }", Token.FUNCTION);
    Node functionNode = Context.getCurrentContext().newNode("function bar() {}", Token.FUNCTION);
    notHoistedFunctionDeclaration.setParent(functionNode);
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
  }
}