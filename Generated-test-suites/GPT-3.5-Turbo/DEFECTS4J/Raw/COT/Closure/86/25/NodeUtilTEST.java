import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_notForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.BLOCK);
    node.addChildToBack(new Node());
    
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_notStatement() {
    Node node = new Node(Token.IF);
    node.addChildToBack(new Node());
    
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node());
    
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node());
    
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(functionNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
}