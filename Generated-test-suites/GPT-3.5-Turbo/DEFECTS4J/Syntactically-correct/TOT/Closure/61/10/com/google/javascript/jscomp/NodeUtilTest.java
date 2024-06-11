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
  public void testIsForIn_notForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.NAME));

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_emptyLoop() {
    Node node = new Node(Token.FOR);

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_scriptParent() {
    Node script = new Node(Token.SCRIPT);
    Node node = new Node(Token.NAME);
    script.addChildToFront(node);

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_blockParent() {
    Node block = new Node(Token.BLOCK);
    Node node = new Node(Token.NAME);
    block.addChildToFront(node);

    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_labelParent() {
    Node label = new Node(Token.LABEL);
    Node node = new Node(Token.NAME);
    label.addChildToFront(node);

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_otherParent() {
    Node parent = new Node(Token.ADD);
    Node node = new Node(Token.NAME);
    parent.addChildToFront(node);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_typical() {
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.NAME);
    parent.addChildToBack(function);
    function.addChildToBack(node);

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_notFunction() {
    Node node = new Node(Token.NAME);

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_notStatement() {
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ADD);
    Node node = new Node(Token.NAME);
    parent.addChildToBack(function);
    function.addChildToBack(node);

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    Node node = new Node(Token.NAME);
    parent.addChildToBack(function);
    function.addChildToBack(grandParent);
    grandParent.addChildToBack(node);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoisted() {
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.NAME);
    parent.addChildToBack(function);
    function.addChildToBack(node);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notFunctionDeclaration() {
    Node node = new Node(Token.NAME);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notStatement() {
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ADD);
    Node grandParent = new Node(Token.SCRIPT);
    Node node = new Node(Token.NAME);
    parent.addChildToBack(function);
    function.addChildToBack(grandParent);
    grandParent.addChildToBack(node);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}