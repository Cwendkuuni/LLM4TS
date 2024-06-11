import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node(Token.EMPTY);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.EMPTY);
    
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    n.addChildToBack(child3);
    
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.WHILE);
    Node child1 = new Node(Token.EMPTY);
    Node child2 = new Node(Token.EMPTY);
    
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.BLOCK);
    n.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.EXPR_RESULT);
    n.addChildToBack(new Node(Token.EMPTY));
    
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.VAR);
    
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    
    n.addChildToBack(new Node(Token.EMPTY));
    parent2.addChildToBack(n);
    parent1.addChildToBack(parent2);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.VAR);
    
    n.addChildToBack(new Node(Token.EMPTY));
    parent2.addChildToBack(n);
    parent1.addChildToBack(parent2);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}