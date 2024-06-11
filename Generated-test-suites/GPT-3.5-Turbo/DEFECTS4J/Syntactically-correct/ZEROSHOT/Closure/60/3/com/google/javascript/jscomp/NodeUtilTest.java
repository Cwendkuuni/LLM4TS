import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.ASSIGN));
    node1.addChildToBack(new Node(Token.TRUE));
    node1.addChildToBack(new Node(Token.TRUE));
    
    Node node2 = new Node(Token.FOR);
    node2.addChildToBack(new Node(Token.ASSIGN));
    node2.addChildToBack(new Node(Token.TRUE));
    
    assertTrue(NodeUtil.isForIn(node1));
    assertFalse(NodeUtil.isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node node1 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.BLOCK);
    Node node3 = new Node(Token.LABEL);
    Node node4 = new Node(Token.IF);
    
    Node node = new Node(Token.ADD);
    node1.addChildToBack(node);
    
    assertFalse(NodeUtil.isStatement(node));
    assertTrue(NodeUtil.isStatement(node1));
    assertTrue(NodeUtil.isStatement(node2));
    assertTrue(NodeUtil.isStatement(node3));
    assertFalse(NodeUtil.isStatement(node4));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NAME));
    Node node2 = new Node(Token.FUNCTION);
    Node node3 = new Node(Token.ASSIGN);
    node3.addChildToBack(new Node(Token.FUNCTION));
    
    Node node = new Node(Token.ADD);
    node1.addChildToBack(node);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    assertFalse(NodeUtil.isFunctionDeclaration(node2));
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = new Node(Token.FUNCTION);
    node1.addChildToFront(new Node(Token.NAME));
    Node node2 = new Node(Token.FUNCTION);
    node2.addChildToFront(new Node(Token.NAME));
    node2.getParent().addChildToFront(node2);
    Node node3 = new Node(Token.ASSIGN);
    node3.addChildToBack(new Node(Token.FUNCTION));
    Node node4 = new Node(Token.FUNCTION);
    node4.addChildToBack(new Node(Token.FUNCTION));
    
    Node node = new Node(Token.ADD);
    node1.addChildToBack(node);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
  }

}