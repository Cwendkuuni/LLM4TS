
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.STRING), new Node(Token.BLOCK));
    boolean result = NodeUtil.isForIn(n);
    assertEquals(true, result);
  }
  
  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.STRING), new Node(Token.BLOCK));
    boolean result = NodeUtil.isForIn(n);
    assertEquals(false, result);
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    boolean result = NodeUtil.isStatement(n);
    assertEquals(true, result);
  }
  
  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.EXPR_RESULT);
    boolean result = NodeUtil.isStatement(n);
    assertEquals(false, result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    boolean result = NodeUtil.isFunctionDeclaration(n);
    assertEquals(true, result);
  }
  
  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.VAR);
    boolean result = NodeUtil.isFunctionDeclaration(n);
    assertEquals(false, result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertEquals(true, result);
  }
  
  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(n);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    assertEquals(false, result);
  }
}
