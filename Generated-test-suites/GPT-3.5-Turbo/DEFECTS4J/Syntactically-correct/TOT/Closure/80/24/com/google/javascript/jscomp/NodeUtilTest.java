import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalCase() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_singleChild() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_emptyChild() {
    Node node = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_differentType() {
    Node node = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_scriptParent() {
    Node script = new Node(Token.SCRIPT);
    Node node = new Node(Token.NAME);
    node.setParent(script);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_blockParent() {
    Node block = new Node(Token.BLOCK);
    Node node = new Node(Token.NAME);
    node.setParent(block);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_labelParent() {
    Node label = new Node(Token.LABEL);
    Node node = new Node(Token.NAME);
    node.setParent(label);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_differentParent() {
    Node node = new Node(Token.NAME);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_functionStatement() {
    Node function = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    function.setParent(parent);
    assertTrue(NodeUtil.isFunctionDeclaration(function));
  }

  @Test
  public void testIsFunctionDeclaration_differentType() {
    Node node = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_differentParent() {
    Node function = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(function));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_scriptParent() {
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    function.setParent(script);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_functionParent() {
    Node function = new Node(Token.FUNCTION);
    Node parentFunction = new Node(Token.FUNCTION);
    function.setParent(parentFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_differentType() {
    Node function = new Node(Token.NAME);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_differentParent() {
    Node function = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
  }
}