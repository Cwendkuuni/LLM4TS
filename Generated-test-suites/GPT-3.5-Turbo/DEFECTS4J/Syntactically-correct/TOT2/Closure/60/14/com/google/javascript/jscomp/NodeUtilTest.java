import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.IF, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    Node statementNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(statementNode);
    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsNotStatement() {
    Node blockNode = new Node(Token.BLOCK, new Node(Token.EMPTY));
    Node expressionNode = new Node(Token.FUNCTION);
    blockNode.addChildToBack(expressionNode);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node blockNode = new Node(Token.BLOCK, new Node(Token.EMPTY));
    Node functionNode = new Node(Token.FUNCTION);
    blockNode.addChildToBack(functionNode);
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

}