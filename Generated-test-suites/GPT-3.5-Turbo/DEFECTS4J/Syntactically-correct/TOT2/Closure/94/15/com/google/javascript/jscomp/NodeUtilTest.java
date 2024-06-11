import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(n));
    
    n = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement() {
    Node script = new Node(Token.SCRIPT);
    Node block = new Node(Token.BLOCK);
    Node label = new Node(Token.LABEL);
    Node function = new Node(Token.FUNCTION);
    
    Node n = new Node(Token.NAME);
    n.addChildToBack(script);
    assertTrue(NodeUtil.isStatement(n));
    
    n.removeChild(script);
    n.addChildToBack(block);
    assertTrue(NodeUtil.isStatement(n));
    
    n.removeChild(block);
    n.addChildToBack(label);
    assertTrue(NodeUtil.isStatement(n));
    
    n.removeChild(label);
    n.addChildToBack(function);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    
    Node n = new Node(Token.NAME);
    n.addChildToBack(function);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
    
    n.removeChild(function);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    
    Node n = new Node(Token.NAME);
    n.addChildToBack(script);
    n.addChildToBack(function);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    
    n.removeChild(script);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}