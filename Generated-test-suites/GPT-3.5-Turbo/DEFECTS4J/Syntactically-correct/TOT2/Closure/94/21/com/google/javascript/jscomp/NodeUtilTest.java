import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.BLOCK), new Node(Token.BLOCK));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT, new Node(Token.BLOCK), new Node(Token.BLOCK));
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(Token.BLOCK));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.BLOCK));
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    functionNode.setParent(scriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test(expected = NullPointerException.class)
  public void testIsForIn_NullNode() {
    NodeUtil.isForIn(null);
  }

  @Test
  public void testIsForIn_NotForInNode() {
    Node node = new Node(Token.BLOCK);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_NotScriptBlockLabel() {
    Node node = new Node(Token.FUNCTION, new Node(Token.BLOCK));
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_NotFunction() {
    Node node = new Node(Token.SCRIPT, new Node(Token.BLOCK), new Node(Token.BLOCK));
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoisted() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.BLOCK));
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    Node blockNode = new Node(Token.BLOCK);
    functionNode.setParent(blockNode);
    blockNode.setParent(scriptNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

}