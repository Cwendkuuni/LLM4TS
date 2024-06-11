import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Before
  public void setUp() {
    // Set up any necessary dependencies or configurations
  }

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_false() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));

    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_false() {
    Node n = new Node(Token.NAME);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_false() {
    Node n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}