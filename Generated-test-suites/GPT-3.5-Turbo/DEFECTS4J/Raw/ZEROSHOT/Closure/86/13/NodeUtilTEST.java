import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node n1 = new Node(Token.FOR);
    n1.addChildToFront(new Node(Token.BLOCK));
    n1.addChildToFront(new Node(Token.BLOCK));
    n1.addChildToFront(new Node(Token.BLOCK));
    
    Node n2 = new Node(Token.BLOCK);
    n2.addChildToFront(new Node(Token.FOR));

    assertTrue(NodeUtil.isForIn(n1));
    assertFalse(NodeUtil.isForIn(n2));
  }
  
  @Test
  public void testIsStatement() {
    Node n1 = new Node(Token.SCRIPT);
    Node n2 = new Node(Token.BLOCK);
    Node n3 = new Node(Token.LABEL);
    Node n4 = new Node(Token.FUNCTION);

    assertTrue(NodeUtil.isStatement(n1));
    assertTrue(NodeUtil.isStatement(n2));
    assertTrue(NodeUtil.isStatement(n3));
    assertFalse(NodeUtil.isStatement(n4));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    n1.addChildToFront(new Node(Token.BLOCK));
    
    Node n2 = new Node(Token.SCRIPT);
    n2.addChildToFront(new Node(Token.FUNCTION));

    assertTrue(NodeUtil.isFunctionDeclaration(n1));
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n1 = new Node(Token.FUNCTION);
    n1.addChildToFront(new Node(Token.BLOCK));
    Node n2 = new Node(Token.FUNCTION);
    n2.addChildToFront(new Node(Token.SCRIPT));
    n2.getParent().addChildToFront(new Node(Token.FUNCTION));
    
    Node n3 = new Node(Token.SCRIPT);
    n3.addChildToFront(new Node(Token.FUNCTION));
    Node n4 = new Node(Token.FUNCTION);
    n4.addChildToFront(new Node(Token.FUNCTION));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n3));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n4));
  }
}