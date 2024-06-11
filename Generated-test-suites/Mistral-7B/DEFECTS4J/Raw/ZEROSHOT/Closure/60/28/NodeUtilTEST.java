import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[] {1, 2, 3});
    Node parentScript = new Node(Token.SCRIPT, new int[] {1});
    parentScript.addChildToEnd(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.IF, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3});
    nodeFunctionDeclaration.setId("myFunction");
    Node parentBlock = new Node(Token.BLOCK, new int[] {1});
    parentBlock.addChildToEnd(nodeFunctionDeclaration);

    Node parentExpression = new Node(Token.IF, new int[] {1, 2, 3});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3});
    nodeFunctionExpression.setId("myFunction");
    parentExpression.addChildToEnd(nodeFunctionExpression);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3});
    nodeHoistedFunctionDeclaration.setId("myFunction");
    Node parentScript = new Node(Token.SCRIPT, new int[] {1});
    parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);

    Node nodeNonHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3});
    nodeNonHoistedFunctionDeclaration.setId("myFunction");
    Node parentBlock = new Node(Token.BLOCK, new int[] {1});
    parentBlock.addChildToEnd(nodeNonHoistedFunctionDeclaration);
    Node parentFunction = new Node(Token.FUNCTION, new int[] {1});
    parentFunction.addChildToEnd(parentBlock);
    parentScript.addChildToEnd(parentFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration));
  }
}