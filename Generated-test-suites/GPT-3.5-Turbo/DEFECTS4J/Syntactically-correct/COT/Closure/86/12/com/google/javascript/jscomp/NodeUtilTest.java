
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.LET), new Node(Token.TRUE));
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsForIn_NotForIn() {
    Node n = new Node(Token.IF, new Node(Token.TRUE), new Node(Token.FALSE));
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsStatement_NotStatement() {
    Node n = new Node(Token.NAME);
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node n = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.getParent().setType(Token.BLOCK);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}
