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
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IDENT, "ident");
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    Node nodeNull = null;
    isStatement(nodeNull);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);
    nodeFunctionDeclaration.setChildCount(1);
    nodeFunctionDeclaration.getChild(0).setName("functionName");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null);
    nodeFunctionExpression.setChildCount(1);
    nodeFunctionExpression.getChild(0).setName("functionName");
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node nodeNull = null;
    isFunctionDeclaration(nodeNull);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);
    nodeFunctionDeclaration.setChildCount(1);
    nodeFunctionDeclaration.getChild(0).setName("functionName");

    Node nodeScript = new Node(Token.SCRIPT, null);
    nodeScript.setChild(nodeFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunction = new Node(Token.FUNCTION, null);
    nodeFunction.setChildCount(1);
    nodeFunction.getChild(0).setName("functionName");
    Node parentBlock = new Node(Token.BLOCK, null);
    parentBlock.setChild(nodeFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInFunction = new Node(Token.FUNCTION, null);
    nodeFunctionInFunction.setChildCount(1);
    nodeFunctionInFunction.getChild(0).setName("functionName");
    Node parentFunction = new Node(Token.FUNCTION, null);
    parentFunction.setChild(nodeFunctionInFunction);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node nodeNull = null;
    isHoistedFunctionDeclaration(nodeNull);
  }
}