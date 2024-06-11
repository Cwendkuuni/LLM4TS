import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.STRING_LITERAL, new int[]{}, new int[]{0, 0, 0});
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 0, 0});
    Node functionName = Node.newNode(Token.IDENTIFIER, new int[]{0}, new int[]{0, 0, 0});
    Node parameterList = Node.newNode(Token.OPEN_PARENTHESIS, new int[]{}, new int[]{0, 0, 0});
    Node functionBody = Node.newNode(Token.BLOCK, new int[]{}, new int[]{0, 0, 0});
    nodeFunctionDeclaration.setChild(0, functionName);
    nodeFunctionDeclaration.setChild(1, parameterList);
    nodeFunctionDeclaration.setChild(2, functionBody);

    Node nodeNotFunctionDeclaration = Node.newNode(Token.VAR, new int[]{}, new int[]{0, 0, 0});
    assertFalse(isFunctionDeclaration(nodeNotFunctionDeclaration));

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 0, 0});
    Node functionName = Node.newNode(Token.IDENTIFIER, new int[]{0}, new int[]{0, 0, 0});
    Node parameterList = Node.newNode(Token.OPEN_PARENTHESIS, new int[]{}, new int[]{0, 0, 0});
    Node functionBody = Node.newNode(Token.BLOCK, new int[] {}, new int[] {0, 0, 0});
    Node script = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    nodeFunctionDeclaration.setChild(0, functionName);
    nodeFunctionDeclaration.setChild(1, parameterList);
    nodeFunctionDeclaration.setChild(2, functionBody);
    script.setChild(0, nodeFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 0, 0});
    Node functionNameExpression = Node.newNode(Token.IDENTIFIER, new int[]{0}, new int[]{0, 0, 0});
    Node parameterListExpression = Node.newNode(Token.OPEN_PARENTHESIS, new int[]{}, new int[]{0, 0, 0});
    Node functionBodyExpression = Node.newNode(Token.BLOCK, new int[]{}, new int[]{0, 0, 0});
    Node expression = Node.newNode(Token.ASSIGN, new int[]{}, new int[]{0, 0, 0});
    Node nodeLeftSide = Node.newNode(Token.IDENTIFIER, new int[]{0}, new int[]{0, 0, 0});
    Node nodeRightSide = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 0, 0});
    nodeFunctionExpression.setChild(0, functionNameExpression);
    nodeFunctionExpression.setChild(1, parameterListExpression);
    nodeFunctionExpression.setChild(2, functionBodyExpression);
    expression.setChild(0, nodeLeftSide);
    expression.setChild(1, nodeFunctionExpression);

    Node nodeScriptWithFunctionExpression = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    nodeScriptWithFunctionExpression.setChild(0, expression);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpression));
  }
}