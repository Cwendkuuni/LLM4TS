import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    
    boolean result = NodeUtil.isForIn(n);
    
    assert(result == true);
  }
  
  @Test
  public void testIsForIn_NotForInLoop() {
    Node n = new Node(Token.NAME);
    
    boolean result = NodeUtil.isForIn(n);
    
    assert(result == false);
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    
    assert(result == true);
  }
  
  @Test
  public void testIsStatement_NotStatement() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.LP);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    
    assert(result == false);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    assert(result == true);
  }
  
  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    assert(result == false);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    assert(result == true);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_NotHoisted() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandparent = new Node(Token.SCRIPT);
    grandparent.addChildToBack(parent);
    parent.addChildToBack(n);
    n.setParent(parent);
    parent.setParent(grandparent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    assert(result == false);
  }
}