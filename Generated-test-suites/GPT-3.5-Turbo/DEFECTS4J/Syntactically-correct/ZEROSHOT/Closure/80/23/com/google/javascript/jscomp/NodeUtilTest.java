import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(node));
    
    node = new Node(Token.NAME);
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node));
    
    node = new Node(Token.CALL);
    node.setParent(new Node(Token.LABEL));
    assertTrue(NodeUtil.isStatement(node));
    
    node = new Node(Token.NAME);
    node.setParent(new Node(Token.ASSIGN));
    assertTrue(NodeUtil.isStatement(node));
    
    node = new Node(Token.NAME);
    node.setParent(new Node(Token.IF));
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isFunctionDeclaration(node));
    
    node = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    parent.setParent(new Node(Token.SCRIPT));
    node.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    
    node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.BLOCK));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    
    node = new Node(Token.NAME);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}