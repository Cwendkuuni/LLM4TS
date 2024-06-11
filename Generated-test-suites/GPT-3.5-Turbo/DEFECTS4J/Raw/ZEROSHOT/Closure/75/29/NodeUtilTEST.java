import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class NodeUtilTest {
  
  @Before
  public void setUp() {
    // Set up any necessary objects before each test method
  }
  
  @Test
  public void testIsForIn() {
    Node n = new Node();
    n.setType(Token.FOR);
    n.addChildToBack(new Node());
    n.addChildToBack(new Node());
    n.addChildToBack(new Node());

    boolean result = NodeUtil.isForIn(n);

    assertEquals(true, result);
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node();
    n.setType(Token.FOR);
    n.addChildToBack(new Node());
    n.addChildToBack(new Node());

    boolean result = NodeUtil.isForIn(n);

    assertEquals(false, result);
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node();
    scriptNode.setType(Token.SCRIPT);
    Node n = new Node();
    scriptNode.addChildToBack(n);

    boolean result = NodeUtil.isStatement(n);

    assertEquals(true, result);
  }

  @Test
  public void testIsNotStatement() {
    Node scriptNode = new Node();
    scriptNode.setType(Token.SCRIPT);
    Node n = new Node();
    scriptNode.addChildToFront(n);

    boolean result = NodeUtil.isStatement(n);

    assertEquals(false, result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.BLOCK);
    parent.addChildToBack(n);

    boolean result = NodeUtil.isFunctionDeclaration(n);

    assertEquals(true, result);
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.EXPR_RESULT);
    parent.addChildToBack(n);

    boolean result = NodeUtil.isFunctionDeclaration(n);

    assertEquals(false, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent1 = new Node();
    parent1.setType(Token.SCRIPT);
    parent1.addChildToBack(n);
    Node parent2 = new Node();
    parent2.setType(Token.FUNCTION);
    parent2.addChildToBack(parent1);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    assertEquals(true, result);
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node();
    n.setType(Token.FUNCTION);
    Node parent1 = new Node();
    parent1.setType(Token.BLOCK);
    parent1.addChildToBack(n);
    Node parent2 = new Node();
    parent2.setType(Token.SCRIPT);
    parent2.addChildToBack(parent1);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

    assertEquals(false, result);
  }
  
}