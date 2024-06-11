import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_notForIn() {
    Node n = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.EXPR_RESULT);
    n.setParent(parent);
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_notStatement() {
    Node parent = new Node(Token.FUNCTION);
    Node n = new Node(Token.EXPR_RESULT);
    n.setParent(parent);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.FUNCTION);
    n.setParent(parent);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_notDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.FUNCTION_EXPR);
    n.setParent(parent);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.FUNCTION);
    Node grandparent = new Node(Token.FUNCTION);
    grandparent.addChildToFront(parent);
    n.setParent(grandparent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notDeclaration() {
    Node parent = new Node(Token.BLOCK);
    Node n = new Node(Token.FUNCTION);
    n.setParent(parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}