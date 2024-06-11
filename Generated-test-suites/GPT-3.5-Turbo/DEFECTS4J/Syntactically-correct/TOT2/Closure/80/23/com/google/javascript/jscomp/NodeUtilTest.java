import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.FOR_IN), new Node(Token.BLOCK));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.FOR, new Node(Token.BLOCK));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    Node statementNode = new Node(Token.LABEL, new Node(Token.BLOCK));
    statementNode.addChildToBack(scriptNode);
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsNotStatement() {
    Node blockNode = new Node(Token.BLOCK);
    assertFalse(NodeUtil.isStatement(blockNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    Node statementNode = new Node(Token.LABEL, new Node(Token.BLOCK));
    statementNode.addChildToBack(functionNode);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertTrue(NodeUtil.isFunctionDeclaration(statementNode));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode.getParent()));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.FUNCTION, new Node(Token.NAME)));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getFirstChild()));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode.getParent()));
  }
}