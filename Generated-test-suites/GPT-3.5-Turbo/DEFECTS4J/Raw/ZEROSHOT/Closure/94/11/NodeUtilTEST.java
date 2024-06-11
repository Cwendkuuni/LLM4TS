import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_withIncorrectNodeType() {
    Node node = new Node(Token.WHILE);
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_withIncorrectChildCount() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_withScriptParent() {
    Node node = new Node(Token.EMPTY);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_withBlockParent() {
    Node node = new Node(Token.EMPTY);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_withLabelParent() {
    Node node = new Node(Token.EMPTY);
    Node parent = new Node(Token.LABEL);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_withNonStatementParent() {
    Node node = new Node(Token.EMPTY);
    Node parent = new Node(Token.NOT);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_withNonStatementParent() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NOT);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_withScriptParent() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_withFunctionParent() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    Node grandparent = new Node(Token.SCRIPT);
    grandparent.addChildToBack(parent);
    parent.addChildToBack(node);
    node.setParent(parent);
    parent.setParent(grandparent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_withNonHoistedParent() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}