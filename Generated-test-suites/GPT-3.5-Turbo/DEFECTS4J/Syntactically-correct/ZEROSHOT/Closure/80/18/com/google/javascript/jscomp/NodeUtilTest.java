import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

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
  public void testIsForIn_False() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.BLOCK);
    node.addChildToBack(new Node(Token.EXPR_RESULT));
    
    assertTrue(NodeUtil.isStatement(node.getFirstChild()));
  }
  
  @Test
  public void testIsStatement_False() {
    Node node = new Node(Token.BLOCK);
    node.addChildToBack(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isStatement(node.getFirstChild()));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_False() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = new Node(Token.FUNCTION);
    Node grandparent = new Node(Token.SCRIPT);
    Node parent = new Node(Token.BLOCK);
    grandparent.addChildToBack(parent);
    parent.addChildToBack(node);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}