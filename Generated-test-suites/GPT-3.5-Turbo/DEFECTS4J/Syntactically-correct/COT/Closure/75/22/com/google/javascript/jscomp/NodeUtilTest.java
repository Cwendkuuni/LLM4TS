
import static org.junit.Assert.*;

import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_false() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.FOR);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_false() {
    Node node = new Node(Token.FOR);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
}