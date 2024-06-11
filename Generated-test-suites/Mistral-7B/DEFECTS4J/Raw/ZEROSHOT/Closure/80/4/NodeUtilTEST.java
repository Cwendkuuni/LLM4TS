import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null);
    nodeForIn.setChildCount(3);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, null);
    Node parentScript = new Node(Token.SCRIPT, null);
    parentScript.setChild(0, nodeStatement);

    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, nodeStatement);
    Node parentBlock = new Node(Token.BLOCK, null);
    parentBlock.setChild(0, nodeNotStatement);

    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "funcName");
    Node parentScript = new Node(Token.SCRIPT, null);
    parentScript.setChild(0, nodeFunctionDeclaration);

    parentScript.setChildCount(1);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, "funcName", null);
    Node parentBlock = new Node(Token.BLOCK, null);
    parentBlock.setChild(0, nodeFunctionExpression);

    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, "funcName", null);
    Node parentScript = new Node(Token.SCRIPT, null);
    parentScript.setChild(0, nodeHoistedFunctionDeclaration);

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, "outerFuncName", null);
    Node hoistedNode = new Node(Token.FUNCTION, "funcName", null);
    nodeFunctionDeclarationInFunction.setChild(0, hoistedNode);
    Node parentFunction = new Node(Token.FUNCTION, "outerFuncName", null);
    parentFunction.setChild(0, nodeFunctionDeclarationInFunction);

    assertTrue(isHoistedFunctionDeclaration(hoistedNode));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateFailure() {
    Node node = new Node(Token.FOR, null);
    Preconditions.checkState(node.getParent() == null);
  }
}