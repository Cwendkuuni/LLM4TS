
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.NUMBER), new Node(Token.NUMBER), new Node(Token.NUMBER));
    assertTrue(NodeUtil.isForIn(n));
  
    Node invalidNode = new Node(Token.FOR, new Node(Token.NUMBER), new Node(Token.NUMBER));
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT, n);
    n.setParent(parent);
    assertTrue(NodeUtil.isStatement(n));

    Node invalidNode = new Node(Token.NUMBER);
    assertFalse(NodeUtil.isStatement(invalidNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT, n);
    n.setParent(parent);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  
    Node invalidNode = new Node(Token.NUMBER);
    assertFalse(NodeUtil.isFunctionDeclaration(invalidNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT, n);
    Node grandParent = new Node(Token.FUNCTION, parent);
    n.setParent(parent);
    parent.setParent(grandParent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  
    Node invalidNode = new Node(Token.NUMBER);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(invalidNode));
  }
}
