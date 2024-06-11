import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  // Test isForIn method
  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(n));
    
    n.removeChild(n.getFirstChild());
    assertFalse(NodeUtil.isForIn(n));
    
    n.addChildToFront(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(n));

    n.removeChild(n.getLastChild());
    assertFalse(NodeUtil.isForIn(n));
  }
  
  // Test isStatement method
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    n.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isStatement(n));
    
    n = new Node(Token.BLOCK);
    n.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isStatement(n));
    
    n = new Node(Token.LABEL);
    n.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isStatement(n));
    
    n = new Node(Token.IF);
    n.addChildToBack(new Node(Token.NAME));
    assertFalse(NodeUtil.isStatement(n));

    n = new Node(Token.NAME);
    Node parent = new Node(Token.LABEL);
    parent.addChildToBack(n);
    assertFalse(NodeUtil.isStatement(n));
  }
  
  // Test isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.BLOCK));
    assertTrue(NodeUtil.isFunctionDeclaration(n));
    
    n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.SCRIPT));
    assertFalse(NodeUtil.isFunctionDeclaration(n));

    n = new Node(Token.BLOCK);
    n.addChildToBack(new Node(Token.FUNCTION));
    assertFalse(NodeUtil.isFunctionDeclaration(n));
    
    n = new Node(Token.SCRIPT);
    n.addChildToBack(new Node(Token.FUNCTION));
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  // Test isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.BLOCK));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    
    n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.BLOCK));
    parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    
    n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.BLOCK));
    parent = new Node(Token.FUNCTION);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    
    n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.BLOCK));
    parent = new Node(Token.IF);
    parent.addChildToBack(n);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}