import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TypeCheckerTestingUtils;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = TypeCheckerTestingUtils.createNode(Token.FOR, null, null, null, null, null, null, null, null);
    Node loopNode = TypeCheckerTestingUtils.createNode(Token.WHILE, null, null, null, null, null, null, null, null);

    assertTrue(isForIn(forNode));
    assertFalse(isForIn(loopNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node scriptNode = TypeCheckerTestingUtils.createNode(Token.SCRIPT, null, null, null, null, null, null, null, null);
    Node ifNode = TypeCheckerTestingUtils.createNode(Token.IF, null, null, null, null, null, null, null, null);
    Node expressionNode = TypeCheckerTestingUtils.createNode(Token.ASSIGN, null, null, null, null, null, null, null, null);

    assertTrue(isStatement(scriptNode));
    assertTrue(isStatement(ifNode));
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = TypeCheckerTestingUtils.createNode(Token.FUNCTION, "funcName", null, null, null, null, null, null, null);
    Node expressionFunctionNode = TypeCheckerTestingUtils.createNode(Token.FUNCTION, null, null, null, null, null, null, null, null);

    assertTrue(isFunctionDeclaration(functionNode));
    assertFalse(isFunctionDeclaration(expressionFunctionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = TypeCheckerTestingUtils.createNode(Token.FUNCTION, "funcName", null, null, null, null, null, null, null);
    Node scriptNode = TypeCheckerTestingUtils.createNode(Token.SCRIPT, null, null, null, null, null, functionNode, null, null);
    Node functionInBlockNode = TypeCheckerTestingUtils.createNode(Token.FUNCTION, "funcName", null, null, null, null, null, null, null);
    Node blockNode = TypeCheckerTestingUtils.createNode(Token.BLOCK, null, null, null, null, null, functionInBlockNode, null, null);

    assertTrue(isHoistedFunctionDeclaration(functionNode));
    assertFalse(isHoistedFunctionDeclaration(functionInBlockNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration_FunctionExpression() {
    Node functionExpressionNode = TypeCheckerTestingUtils.createNode(Token.FUNCTION, null, null, null, null, null, null, null, Token.FUNCTION);

    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration_VariableDeclarationFunction() {
    Node varNode = TypeCheckerTestingUtils.createNode(Token.VAR, "funcName", null, null, null, null, null, null, null);
    Node functionNode = TypeCheckerTestingUtils.createNode(Token.FUNCTION, null, null, null, null, null, null, null, null);

    assertFalse(isFunctionDeclaration(functionNode));
    assertFalse(isFunctionDeclaration(varNode));
  }
}