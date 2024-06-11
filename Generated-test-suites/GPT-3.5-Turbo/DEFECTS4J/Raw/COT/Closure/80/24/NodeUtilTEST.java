
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isForIn(node));
    
    Node node2 = new Node(Token.FOR);
    node2.addChildToFront(new Node(Token.NAME));
    node2.addChildToFront(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isForIn(node2));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(node));
    
    Node node2 = new Node(Token.NAME);
    Node parent2 = new Node(Token.ARRAYLIT);
    parent2.addChildToFront(node2);
    node2.setParent(parent2);
    
    assertFalse(NodeUtil.isStatement(node2));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    Node node2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.EXPR_RESULT);
    parent2.addChildToFront(node2);
    node2.setParent(parent2);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
  
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(functionNode);
    functionNode.setParent(parent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    
    Node functionNode2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToFront(functionNode2);
    functionNode2.setParent(parent2);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
    
    Node functionNode3 = new Node(Token.FUNCTION);
    Node parent3 = new Node(Token.IF);
    parent3.addChildToFront(functionNode3);
    functionNode3.setParent(parent3);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode3));
  }
  
}
