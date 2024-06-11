import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn_WithForInLoop_ReturnsTrue() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_WithNonForInLoop_ReturnsFalse() {
    Node node = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_WithStatementParent_ReturnsTrue() {
    Node node = new Node(Token.EMPTY);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_WithNonStatementParent_ReturnsFalse() {
    Node node = new Node(Token.EMPTY);
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToBack(node);
    node.setParent(parent);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_WithFunctionDeclaration_ReturnsTrue() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.NAME));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_WithNonFunctionDeclaration_ReturnsFalse() {
    Node node = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WithHoistedFunctionDeclaration_ReturnsTrue() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME));
    functionNode.addChildToBack(new Node(Token.EMPTY));
    functionNode.addChildToBack(new Node(Token.EMPTY));

    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WithNonHoistedFunctionDeclaration_ReturnsFalse() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME));
    functionNode.addChildToBack(new Node(Token.EMPTY));
    functionNode.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WithFunctionDeclarationNotInHierarchy_ReturnsFalse() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME));
    functionNode.addChildToBack(new Node(Token.EMPTY));
    functionNode.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}