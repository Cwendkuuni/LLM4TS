import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  // Basic test cases

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
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    n.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    n.setParent(parent2);
    parent2.setParent(parent1);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  // Edge cases and exception handling test cases

  @Test
  public void testIsForIn_NullNode() {
    assertFalse(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsForIn_NotForToken() {
    Node n = new Node(Token.EMPTY);
    
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_NotThreeChildren() {
    Node n = new Node(Token.FOR);
    Node child = new Node(Token.EMPTY);
    n.addChildToBack(child);
    
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement_NullNode() {
    assertFalse(NodeUtil.isStatement(null));
  }

  @Test
  public void testIsStatement_NullParent() {
    Node n = new Node(Token.SCRIPT);
    n.setParent(null);
    
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_NotScriptBlockOrLabel() {
    Node n = new Node(Token.FOR);
    
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration_NullNode() {
    assertFalse(NodeUtil.isFunctionDeclaration(null));
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionToken() {
    Node n = new Node(Token.EMPTY);
    
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_NullParent() {
    Node n = new Node(Token.FUNCTION);
    n.setParent(null);
    
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NullNode() {
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotFunctionToken() {
    Node n = new Node(Token.EMPTY);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NullParent1() {
    Node n = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.FUNCTION);
    n.setParent(parent2);
    parent2.setParent(null);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NullParent2() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    n.setParent(parent1);
    parent1.setParent(null);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotScriptOrFunctionParent() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.BLOCK);
    Node parent2 = new Node(Token.IF);
    n.setParent(parent2);
    parent2.setParent(parent1);
    parent1.setParent(null);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}