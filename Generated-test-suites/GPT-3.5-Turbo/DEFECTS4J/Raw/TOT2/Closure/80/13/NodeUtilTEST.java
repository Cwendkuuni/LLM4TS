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
    Node blockNode = new Node(Token.BLOCK);
    Node scriptNode = new Node(Token.SCRIPT, blockNode);
    blockNode.setParent(scriptNode);
    assertTrue(NodeUtil.isStatement(blockNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT, functionNode);
    functionNode.setParent(scriptNode);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.FUNCTION, functionNode));
    functionNode.setParent(scriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}