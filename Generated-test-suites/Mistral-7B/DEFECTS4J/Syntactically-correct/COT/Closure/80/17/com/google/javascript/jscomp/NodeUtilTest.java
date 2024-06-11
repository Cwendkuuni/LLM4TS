import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(FOR, new int[] {0, 0, 1}, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(IF, new int[] {0, 0, 0}, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(IF, new int[] {0, 0, 0}, null);
    Node parentScript = Node.newNode(SCRIPT, new int[] {0}, null);
    parentScript.appendChild(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(ID, new int[] {0}, "x");
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        FUNCTION, new int[] {1, 0, 0}, new String[] {"arg1"});
    Node parentBlock = Node.newNode(BLOCK, new int[] {0}, null);
    parentBlock.appendChild(nodeFunctionDeclaration);

    Node nodeNotFunctionDeclaration = Node.newNode(ID, new int[] {0}, "x");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = Node.newNode(
        FUNCTION, new int[] {1, 0, 0}, new String[] {"arg1"});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(
        FUNCTION, new int[] {1, 0, 0}, new String[] {"arg1"});
    Node parentScript = Node.newNode(SCRIPT, new int[] {0}, null);
    Node functionDeclaration = Node.newNode(BLOCK, new int[] {0}, null);
    functionDeclaration.appendChild(nodeHoistedFunctionDeclaration);
    parentScript.appendChild(functionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newNode(
        FUNCTION, new int[] {1, 0, 0}, new String[] {"arg1"});
    Node parentBlock = Node.newNode(BLOCK, new int[] {0}, null);
    parentBlock.appendChild(nodeNotHoistedFunctionDeclaration);
    Node parentFunction = Node.newNode(FUNCTION, new int[] {0, 0, 0}, null);
    parentFunction.appendChild(parentBlock);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}