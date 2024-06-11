import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.STRING));

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_empty() {
    Node node = new Node(Token.FOR);

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_incorrectType() {
    Node node = new Node(Token.IF);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.STRING));

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_typical() {
    Node node = new Node(Token.NAME);
    node.setParent(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_block() {
    Node node = new Node(Token.NAME);
    node.setParent(new Node(Token.BLOCK));

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_label() {
    Node node = new Node(Token.NAME);
    node.setParent(new Node(Token.LABEL));

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_invalid() {
    Node node = new Node(Token.NAME);
    node.setParent(new Node(Token.FUNCTION));

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_notStatement() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.EXPR_RESULT));

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_incorrectType() {
    Node node = new Node(Token.NAME);
    node.setParent(new Node(Token.SCRIPT));

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    node.setParent(parent);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notStatement() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    node.setParent(parent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_incorrectParentType() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(new Node(Token.FUNCTION));
    node.setParent(parent);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_incorrectGrandparentType() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandparent = new Node(Token.LABEL);
    parent.addChildToBack(grandparent);
    grandparent.addChildToBack(node);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}