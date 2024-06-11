import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typical() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME, "i"));
    node.addChildToBack(new Node(Token.NAME, "arr"));
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_noForToken() {
    Node node = new Node(Token.IF);
    node.addChildToBack(new Node(Token.NAME, "i"));
    node.addChildToBack(new Node(Token.NAME, "arr"));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_insufficientChildCount() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.NAME, "i"));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_scriptParent() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node node = new Node(Token.STRING);
    scriptNode.addChildToBack(node);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_blockParent() {
    Node blockNode = new Node(Token.BLOCK);
    Node node = new Node(Token.STRING);
    blockNode.addChildToBack(node);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_labelParent() {
    Node labelNode = new Node(Token.LABEL);
    Node node = new Node(Token.STRING);
    labelNode.addChildToBack(node);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_otherParent() {
    Node parent = new Node(Token.NAME);
    Node node = new Node(Token.STRING);
    parent.addChildToBack(node);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_typical() {
    Node node = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_noFunctionToken() {
    Node node = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_notStatement() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(node);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typical() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.BLOCK);
    parent1.addChildToBack(functionNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notScopeRoot() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(new Node(Token.IF));
    parent1.addChildToBack(functionNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

}