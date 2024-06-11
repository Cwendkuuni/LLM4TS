import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node();
    Node child2 = new Node();
    Node child3 = new Node();

    n.addChildToFront(child1);
    n.addChildToFront(child2);
    n.addChildToFront(child3);

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_NotForInLoop() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node();
    Node child2 = new Node();
    Node child3 = new Node();
    
    n.addChildToFront(child1);
    n.addChildToFront(child2);
    
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.STRING);
    Node parent = new Node(Token.SCRIPT);
    
    parent.addChildToFront(n);
    n.setParent(parent);

    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node n = new Node(Token.STRING);
    Node parent = new Node();

    parent.addChildToFront(n);
    n.setParent(parent);

    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    parent.addChildToFront(n);
    n.setParent(parent);

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();

    parent.addChildToFront(n);
    n.setParent(parent);

    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);

    parent2.addChildToFront(n);
    parent1.addChildToFront(parent2);
    n.setParent(parent2);
    parent2.setParent(parent1);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    parent.addChildToFront(n);
    n.setParent(parent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}