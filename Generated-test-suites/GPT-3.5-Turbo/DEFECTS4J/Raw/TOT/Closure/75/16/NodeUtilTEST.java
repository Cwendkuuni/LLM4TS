import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_emptyForLoop() {
    Node node = new Node(Token.FOR);

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_incorrectNodeType() {
    Node node = new Node(Token.IF);
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));
    node.addChildToFront(new Node(Token.NAME));

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_typical() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToFront(new Node(Token.EXPR_RESULT));

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_nullParent() {
    Node node = new Node(Token.EXPR_RESULT);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_inBlock() {
    Node block = new Node(Token.BLOCK);
    block.addChildToFront(new Node(Token.EXPR_RESULT));

    assertTrue(NodeUtil.isStatement(block.getFirstChild()));
  }

  @Test
  public void testIsStatement_isNotStatement() {
    Node node = new Node(Token.EXPR_RESULT);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    node.setType(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_inExpression() {
    Node node = new Node(Token.NAME);
    node.setType(Token.FUNCTION);
    node.addChildToFront(new Node(Token.NAME));

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    function.addChildToFront(new Node(Token.NAME));
    script.addChildToFront(function);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_inFunction() {
    Node parentFunction = new Node(Token.FUNCTION);
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    function.addChildToFront(new Node(Token.NAME));
    parentFunction.addChildToFront(function);
    script.addChildToFront(parentFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
  }

}