
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_True() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_False_NonForNode() {
    Node node = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_False_IncorrectChildCount() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_True() {
    Node node = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.EMPTY);
    node.addChildToBack(statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsStatement_True_BlockParent() {
    Node blockNode = new Node(Token.BLOCK);
    Node statementNode = new Node(Token.EMPTY);
    blockNode.addChildToBack(statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsStatement_True_LabelParent() {
    Node labelNode = new Node(Token.LABEL);
    Node statementNode = new Node(Token.EMPTY);
    labelNode.addChildToBack(statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsStatement_False() {
    Node node = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_True() {
    Node node = new Node(Token.FUNCTION);
    Node statementNode = new Node(Token.EMPTY);
    node.addChildToBack(statementNode);

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    Node node = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_True() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    Node statementNode = new Node(Token.EMPTY);
    scriptNode.addChildToBack(functionNode);
    functionNode.addChildToBack(statementNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}
