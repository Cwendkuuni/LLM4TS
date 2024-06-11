import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EXPR_RESULT));
    node.addChildToBack(new Node(Token.IN));
    node.addChildToBack(new Node(Token.EMPTY));
    
    boolean isForIn = NodeUtil.isForIn(node);
    
    assertTrue(isForIn);
  }
  
  @Test
  public void testIsForIn_NotForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    
    boolean isForIn = NodeUtil.isForIn(node);
    
    assertFalse(isForIn);
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.BLOCK);
    
    boolean isStatement = NodeUtil.isStatement(node);
    
    assertTrue(isStatement);
  }
  
  @Test
  public void testIsStatement_NotStatement() {
    Node node = new Node(Token.EMPTY);
    
    boolean isStatement = NodeUtil.isStatement(node);
    
    assertFalse(isStatement);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.EMPTY));
    
    boolean isFunctionDeclaration = NodeUtil.isFunctionDeclaration(node);
    
    assertTrue(isFunctionDeclaration);
  }
  
  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node node = new Node(Token.EMPTY);
    
    boolean isFunctionDeclaration = NodeUtil.isFunctionDeclaration(node);
    
    assertFalse(isFunctionDeclaration);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.EMPTY));

    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToFront(node);

    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToFront(node);
    
    boolean isHoistedFunctionDeclaration1 = NodeUtil.isHoistedFunctionDeclaration(node);
    boolean isHoistedFunctionDeclaration2 = NodeUtil.isHoistedFunctionDeclaration(parent1);
    boolean isHoistedFunctionDeclaration3 = NodeUtil.isHoistedFunctionDeclaration(parent2);
    
    assertTrue(isHoistedFunctionDeclaration1);
    assertTrue(isHoistedFunctionDeclaration2);
    assertTrue(isHoistedFunctionDeclaration3);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.EMPTY));
    
    boolean isHoistedFunctionDeclaration = NodeUtil.isHoistedFunctionDeclaration(node);
    
    assertFalse(isHoistedFunctionDeclaration);
  }

}