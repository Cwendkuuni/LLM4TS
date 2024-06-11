import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null);
    nodeForIn.setChildCount(3);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IDENT, "statement");
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.ADD, null);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testIsStatementWithNullParent() {
    Node nodeStatement = new Node(Token.IDENT, "statement");
    Node nullParent = null;
    nodeStatement.setParent(nullParent);
    NodeUtil.isStatement(nodeStatement);
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    functionDeclaration.setChild(0, new Node(Token.IDENT, "functionName"));
    functionDeclaration.setChildCount(1);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = new Node(Token.FUNCTION, null);
    functionExpression.setChild(0, new Node(Token.IDENT, "functionName"));
    functionExpression.setChildCount(1);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, null);
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    functionDeclaration.setChild(0, new Node(Token.IDENT, "functionName"));
    functionDeclaration.setChildCount(1);
    scriptNode.setChild(0, functionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));

    Node functionDeclarationInFunction = new Node(Token.FUNCTION, null);
    Node functionInFunction = new Node(Token.FUNCTION, null);
    functionInFunction.setChild(0, new Node(Token.IDENT, "functionInFunction"));
    functionInFunction.setChildCount(1);
    functionDeclarationInFunction.setChild(0, functionInFunction);
    functionDeclarationInFunction.setChild(0, new Node(Token.IDENT, "functionName"));
    functionDeclarationInFunction.setChildCount(2);
    scriptNode.setChild(0, functionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }
}