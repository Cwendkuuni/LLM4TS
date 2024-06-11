import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, 0);
    nodeForIn.setProperty("getType", FOR);
    nodeForIn.setProperty("getChildCount", 3);

    Node nodeNotForIn = new Node(Token.IF, 0);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Script, 0);
    Node nodeNotStatement = new Node(Token.IF, 0);

    Node parentStatement = new Node(Block, 0);
    parentStatement.setProperty("getType", Block);
    parentStatement.setProperty("getParent", null);

    Node parentNotStatement = new Node(Token.IF, 0);
    parentNotStatement.setProperty("getType", Token.IF);
    parentNotStatement.setProperty("getParent", new Node(Token.SCRIPT, 0));

    Node nodeChildStatement = new Node(Token.IDENT, 0);
    nodeChildStatement.setProperty("getType", Ident);
    nodeChildStatement.setProperty("getParent", nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeChildStatement));
    assertTrue(NodeUtil.isStatement(parentStatement));

    assertFalse(NodeUtil.isStatement(nodeNotStatement));
    assertFalse(NodeUtil.isStatement(new Node(Token.VAR, 0)));
    assertFalse(NodeUtil.isStatement(new Node(Token.RETURN, 0)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Function, 0);
    nodeFunctionDeclaration.setProperty("getType", Function);
    nodeFunctionDeclaration.setProperty("getChildCount", 2);
    nodeFunctionDeclaration.setProperty("getParent", new Node(Block, 0));
    nodeFunctionDeclaration.setProperty("getParent().getType", Block);

    Node nodeFunctionExpression = new Node(Function, 0);
    nodeFunctionExpression.setProperty("getType", Function);
    nodeFunctionExpression.setProperty("getChildCount", 2);
    nodeFunctionExpression.setProperty("getParent", new Node(Token.EXPR_Statement, 0));

    Node nodeIdentifier = new Node(Token.IDENT, 0);
    nodeIdentifier.setProperty("getType", Ident);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIdentifier));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckState() {
    Node node = new Node(Token.SCRIPT, 0);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Function, 0);
    nodeFunctionDeclaration.setProperty("getType", Function);
    nodeFunctionDeclaration.setProperty("getChildCount", 2);
    nodeFunctionDeclaration.setProperty("getParent", new Node(Block, 0));
    nodeFunctionDeclaration.setProperty("getParent().getType", Block);

    Node parentFunction = new Node(Function, 0);
    parentFunction.setProperty("getType", Function);
    parentFunction.setProperty("getChildCount", 2);

    Node nodeRoot = new Node(Script, 0);
    nodeRoot.setProperty("getType", Script);
    nodeRoot.setProperty("getParent", null);

    Node nodeFunctionInFunction = new Node(Function, 0);
    nodeFunctionInFunction.setProperty("getType", Function);
    nodeFunctionInFunction.setProperty("getChildCount", 2);
    nodeFunctionInFunction.setProperty("getParent", nodeFunctionDeclaration);

    Node nodeFunctionInScript = new Node(Function, 0);
    nodeFunctionInScript.setProperty("getType", Function);
    nodeFunctionInScript.setProperty("getChildCount", 2);
    nodeFunctionInScript.setProperty("getParent", nodeRoot);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }
}