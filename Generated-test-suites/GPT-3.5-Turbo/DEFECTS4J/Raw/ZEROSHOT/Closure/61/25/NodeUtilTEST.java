import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
    Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
    boolean result = NodeUtil.isForIn(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
    Node node = new Node(Token.NOT);
    boolean result = NodeUtil.isForIn(node);
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement_WhenNodeIsStatement_ReturnsTrue() {
    Node node = new Node(Token.SCRIPT);
    boolean result = NodeUtil.isStatement(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_WhenNodeIsNotStatement_ReturnsFalse() {
    Node node = new Node(Token.NOT);
    boolean result = NodeUtil.isStatement(node);
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
    Node node = new Node(Token.FUNCTION);
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.NOT);
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.NOT));
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result);
  }
}