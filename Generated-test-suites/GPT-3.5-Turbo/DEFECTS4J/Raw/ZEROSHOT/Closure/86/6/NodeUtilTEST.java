import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_notForInLoop() {
    Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.NAME, parent);
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_notStatement() {
    Node parent = new Node(Token.NAME);
    Node node = new Node(Token.NAME, parent);
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION, parent);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node parent = new Node(Token.NAME);
    Node node = new Node(Token.FUNCTION, parent);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION, script);
    Node node = new Node(Token.NAME, new Node(Token.FUNCTION, function));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    Node script = new Node(Token.SCRIPT);
    Node parent = new Node(Token.NAME, script);
    Node node = new Node(Token.NAME, parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}