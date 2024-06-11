import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    scriptNode.addChildToBack(blockNode);
    
    assertTrue(NodeUtil.isStatement(blockNode));
    assertFalse(NodeUtil.isStatement(scriptNode));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(functionNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}