import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import com.google.javascript.jscomp.NodeUtil;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, new int[]{0, 1, 2});
    Node notForINode = createNode(Token.FOR, new int[]{0, 1});
    Node notANode = createNode(Token.IF, new int[]{0});

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForINode));
    assertFalse(NodeUtil.isForIn(notANode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = createNode(Token.IF, new int[]{0});
    Node expressionNode = createNode(Token.ADD, new int[]{0, 1});

    Node scriptNode = createNode(Token.SCRIPT, new int[0]);
    Node blockNode = createNode(Token.BLOCK, new int[0]);
    Node labelNode = createNode(Token.LABEL, new int[]{"label"});

    Node functionNode = createNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0});
    Node functionExpressionNode = createNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0}, createNode(Token.IDENT, new int[]{"func"}));

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(functionNode));
    assertTrue(NodeUtil.isStatement(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0});
    Node functionExpressionNode = createNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0}, createNode(Token.IDENT, new int[]{"func"}));

    Node statementFunctionNode = createNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0}, createNode(Token.IDENT, new int[]{"func"}));
    Node expressionFunctionNode = createNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0}, createNode(Token.IDENT, new int[]{"func"}), createNode(Token.ASSIGN, new int[]{1, 1}));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(statementFunctionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(expressionFunctionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = createNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0});
    Node notHoistedFunctionDeclarationNode = createNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0});
    Node scriptNode = createNode(Token.SCRIPT, new int[0]);
    Node functionNode = createNode(Token.FUNCTION, new int[]{0, 1}, new int[]{0}, createNode(Token.IDENT, new int[]{"func"}));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}