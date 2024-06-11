import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Context.getCurrentContext().makeNode(
        "for (var i in array) {}", Token.FOR, null);
    assertTrue(isForIn(nodeForIn));

    Node nodeFor = Context.getCurrentContext().makeNode(
        "for (var i = 0; i < array.length; i++) {}", Token.FOR, null);
    assertFalse(isForIn(nodeFor));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Context.getCurrentContext().makeNode(
        "if (condition) {}", Token.IF, null);
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Context.getCurrentContext().makeNode("condition");
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Context.getCurrentContext().makeNode(
        "function foo() {}", Token.FUNCTION, "foo");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Context.getCurrentContext().makeNode(
        "function bar() {}", Token.FUNCTION, null);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Context.getCurrentContext().makeNode(
        "function foo() {}", Token.FUNCTION, "foo");
    Node scriptNode = Context.getCurrentContext().makeNode("", Token.SCRIPT, null);
    scriptNode.addChild(nodeHoistedFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Context.getCurrentContext().makeNode(
        "function foo() {}", Token.FUNCTION, "foo");
    Node functionNode = Context.getCurrentContext().makeNode(
        "function bar() {}", Token.FUNCTION, "bar");
    functionNode.addChild(nodeFunctionDeclarationInFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}