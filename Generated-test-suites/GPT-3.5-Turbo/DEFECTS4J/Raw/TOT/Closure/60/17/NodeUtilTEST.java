import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn_withForInLoop_ReturnsTrue() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.BLOCK);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    assertTrue(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_withForLoop_ReturnsFalse() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.LP);
    Node child3 = new Node(Token.BLOCK);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    assertFalse(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_withinScript_ReturnsTrue() {
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    assertTrue(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_withinBlock_ReturnsTrue() {
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);

    assertTrue(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_withinLabel_ReturnsTrue() {
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.LABEL);
    parent.addChildToBack(node);

    assertTrue(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_withinFunction_ReturnsFalse() {
    Node node = new Node(Token.BLOCK);
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToBack(node);

    assertFalse(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_withFunctionDeclaration_ReturnsTrue() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    assertTrue(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_withFunctionExpression_ReturnsFalse() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);

    assertFalse(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withHoistedFunctionDeclaration_ReturnsTrue() {
    Node node = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(node);
    Node parent2 = new Node(Token.FUNCTION);
    parent2.addChildToBack(parent1);

    assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withRootFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withNonRootFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);

    assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
  }
}