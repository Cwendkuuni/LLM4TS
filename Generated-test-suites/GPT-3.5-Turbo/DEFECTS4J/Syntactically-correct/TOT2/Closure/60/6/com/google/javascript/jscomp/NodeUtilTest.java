import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node script = new Node(Token.SCRIPT);
    Node n = new Node(Token.NAME);
    script.addChildToBack(n);

    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    function.addChildToBack(new Node(Token.NAME));

    assertTrue(NodeUtil.isFunctionDeclaration(function));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    function.addChildToBack(new Node(Token.NAME));
    script.addChildToBack(function);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
  }
}