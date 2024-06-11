import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.EMPTY);
    n.setParent(parent);
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsNotStatement() {
    Node parent = new Node(Token.FUNCTION);
    Node n = new Node(Token.FUNCTION);
    n.setParent(parent);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    Node n = new Node(Token.FUNCTION);
    n.setParent(parent);
    parent.setParent(grandParent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  // Add additional test cases for edge cases and exceptions if needed

}