import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForInFalse() {
    Node n = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    n.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatementFalse() {
    Node n = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclarationFalse() {
    Node n = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node grandparent = new Node(Token.FUNCTION);
    parent.addChildToBack(grandparent);
    grandparent.addChildToBack(new Node(Token.FUNCTION));
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(grandparent));
  }

  @Test
  public void testIsHoistedFunctionDeclarationFalse() {
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(new Node(Token.FUNCTION));
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(parent));
  }
}