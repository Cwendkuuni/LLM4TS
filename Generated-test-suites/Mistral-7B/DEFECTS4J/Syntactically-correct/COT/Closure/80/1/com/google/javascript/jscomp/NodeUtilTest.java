import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(Token.FOR, null, createNode(Token.IDENT, "for"), createNode(Token.LPAREN, null), createNode(Token.IDENT, "i"), createNode(Token.RPAREN, null), createNode(Token.LPAREN, null), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null));
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(Token.VAR, null, createNode(Token.IDENT, "i"), createNode(Token.EQUALS, null, createNode(Token.NUMBER, null, 10), createNode(Token.SEMICOLON, null)));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(Token.IF, null, createNode(Token.LPAREN, null), createNode(Token.IDENT, "condition"), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = createNode(Token.IDENT, "name");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementNullParent() {
    Node node = createNode(Token.IDENT, "name");
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "functionName"), createNode(Token.LPAREN, null), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = createNode(Token.IDENT, "functionName");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationNullParent() {
    Node node = createNode(Token.IDENT, "functionName");
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isFunctionDeclaration(node);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "hoistedFunctionName"), createNode(Token.LPAREN, null), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null), createNode(Token.SCRIPT, null));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "notHoistedFunctionName"), createNode(Token.LPAREN, null), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null), createNode(Token.BLOCK, null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node node = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "functionName"), createNode(Token.LPAREN, null), createNode(Token.RPAREN, null), createNode(Token.BLOCK, null));
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isHoistedFunctionDeclaration(node);
  }
}