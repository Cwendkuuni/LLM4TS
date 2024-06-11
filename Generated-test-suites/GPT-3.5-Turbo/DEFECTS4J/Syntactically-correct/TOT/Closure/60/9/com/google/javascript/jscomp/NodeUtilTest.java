import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_withForInLoop_shouldReturnTrue() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.IN));
    n.addChildToFront(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_withNonForInLoop_shouldReturnFalse() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.EMPTY));
    
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement_withStatementNode_shouldReturnTrue() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.SCRIPT));
    
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_withNonStatementNode_shouldReturnFalse() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration_withFunctionDeclarationNode_shouldReturnTrue() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToFront(new Node(Token.BLOCK));
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_withNonFunctionDeclarationNode_shouldReturnFalse() {
    Node n = new Node(Token.NAME);
    
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withHoistedFunctionDeclarationNode_shouldReturnTrue() {
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(new Node(Token.FUNCTION));
    parent.addChildToFront(new Node(Token.ASSIGN));
    
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.setParent(parent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withNonHoistedFunctionDeclarationNode_shouldReturnFalse() {
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(new Node(Token.VAR));
    
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.setParent(parent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}