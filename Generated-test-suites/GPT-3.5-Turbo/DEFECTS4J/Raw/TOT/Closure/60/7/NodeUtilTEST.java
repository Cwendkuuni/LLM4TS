import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn_validInput_true() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(null));
    n.addChildToFront(new Node(null));
    n.addChildToFront(new Node(null));
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsForIn_invalidInput_false() {
    Node n = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement_validInput_true() {
    Node n = new Node(Token.LABEL);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsStatement_invalidInput_false() {
    Node n = new Node(Token.LABEL);
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_validInput_true() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_invalidInput_false() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.LABEL);
    parent.addChildToFront(n);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_validInput_true() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    parent1.addChildToFront(n);
    parent2.addChildToFront(parent1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_invalidInput_false() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.LABEL);
    Node parent2 = new Node(Token.SCRIPT);
    parent1.addChildToFront(n);
    parent2.addChildToFront(parent1);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}