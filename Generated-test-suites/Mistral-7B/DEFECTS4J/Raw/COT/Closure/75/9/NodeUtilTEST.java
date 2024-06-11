import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChild(0, new Node(Token.IDENT, "forVar"));
    nodeForIn.setChild(1, new Node(Token.KEYWORD, "in"));
    nodeForIn.setChild(2, new Node(Token.IDENT, "coll"));
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, new Node("message"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.KEYWORD, "function");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    Node nodeNull = null;
    NodeUtil.isStatement(nodeNull);
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node("name"), null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new Node("name"), null, new Node("expression"));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node nodeNull = null;
    NodeUtil.isFunctionDeclaration(nodeNull);
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new Node("name"), null, null);
    nodeHoistedFunctionDeclaration.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new Node("name"), null, null);
    Node parentFunction = new Node(Token.FUNCTION, null, null, null);
    nodeFunctionDeclarationInFunction.setParent(parentFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node nodeNull = null;
    NodeUtil.isHoistedFunctionDeclaration(nodeNull);
  }
}