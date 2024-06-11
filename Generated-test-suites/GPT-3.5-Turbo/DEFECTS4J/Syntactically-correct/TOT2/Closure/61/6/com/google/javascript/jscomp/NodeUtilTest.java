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
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    Node labelNode = new Node(Token.LABEL, new Node(Token.BLOCK));
    
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertFalse(NodeUtil.isStatement(labelNode));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.BLOCK));
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.FUNCTION));
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.BLOCK));
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}