import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn_validForIn_returnsTrue() {
    Node n = new Node(Token.FOR, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsForIn_invalidForIn_returnsFalse() {
    Node n = new Node(Token.FOR, new Node(), new Node());
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement_validStatement_returnsTrue() {
    Node parent = new Node(Token.SCRIPT, new Node());
    Node n = new Node(Token.FUNCTION, parent, new Node());
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsStatement_invalidStatement_returnsFalse() {
    Node parent = new Node(Token.SCRIPT, new Node());
    Node n = new Node(Token.NUMBER, parent, new Node());
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_validFunctionDeclaration_returnsTrue() {
    Node parent = new Node(Token.BLOCK, new Node());
    Node n = new Node(Token.FUNCTION, parent, new Node());
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_invalidFunctionDeclaration_returnsFalse() {
    Node parent = new Node(Token.BLOCK, new Node());
    Node n = new Node(Token.NUMBER, parent, new Node());
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_validHoistedFunctionDeclaration_returnsTrue() {
    Node grandParent = new Node(Token.SCRIPT, new Node());
    Node parent = new Node(Token.FUNCTION, grandParent, new Node());
    Node n = new Node(Token.FUNCTION, parent, new Node());
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_invalidHoistedFunctionDeclaration_returnsFalse() {
    Node parent = new Node(Token.SCRIPT, new Node());
    Node n = new Node(Token.FUNCTION, parent, new Node());
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}