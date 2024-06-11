import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

/**
 * Test class for NodeUtil.
 */
public class NodeUtilTest {

  /**
   * Test if the node represents a FOR-IN loop.
   */
  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(FOR);
    Node forNodeWithThreeChildren = Node.newNode(FOR, Node.newNode(IDENTIFIER, "for"), Node.newNode(IDENTIFIER, "in"), Node.newNode(IDENTIFIER, "array"));

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
  }

  /**
   * Test if the node is used as a statement.
   */
  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(PRINT, Node.newNode(STRING_LITERAL, "Hello, World!"));

    assertTrue(NodeUtil.isStatement(statementNode));
  }

  /**
   * Test if the node is a function declaration.
   */
  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(FUNCTION, Node.newNode(IDENTIFIER, "funcName"), Node.newNode(BLOCK, Node.newNode(RETURN, Node.newNode(NUMBER_LITERAL, 1))));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
  }

  /**
   * Test if the node is a hoisted function declaration.
   */
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newNode(FUNCTION, Node.newNode(IDENTIFIER, "funcName"), Node.newNode(BLOCK, Node.newNode(RETURN, Node.newNode(NUMBER_LITERAL, 1))));
    Node scriptNode = Node.newNode(SCRIPT, Node.newNode(hoistedFunctionDeclarationNode));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  /**
   * Test if the node is not a hoisted function declaration when it's not in the
   * script level.
   */
  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(FUNCTION, Node.newNode(IDENTIFIER, "funcName"), Node.newNode(BLOCK, Node.newNode(RETURN, Node.newNode(NUMBER_LITERAL, 1))));
    Node blockNode = Node.newNode(BLOCK, Node.newNode(functionDeclarationNode));
    Node functionNode = Node.newNode(FUNCTION, Node.newNode(IDENTIFIER, "func"), Node.newNode(BLOCK, Node.newNode(returnStatement(1))));

    Node scriptNode = Node.newNode(SCRIPT, blockNode, functionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
  }

  /**
   * Test if the node is not a function declaration when it's not a function.
   */
  @Test(expected = AssertionError.class)
  public void testIsNotFunctionDeclaration() {
    Node notFunctionNode = Node.newNode(IDENTIFIER, "notFunction");

    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));
  }

  /**
   * Helper method for creating a return statement with a given value.
   */
  private Node returnStatement(int value) {
    return Node.newNode(RETURN, Node.newNode(NUMBER_LITERAL, value));
  }

  /**
   * Test if the method throws an exception when given a null parent node.
   */
  @Test(expected = NullPointerException.class)
  public void testIsStatementWithNullParent() {
    Node statementNode = Node.newNode(PRINT, Node.newNode(STRING_LITERAL, "Hello, World!"));

    Preconditions.checkState(statementNode.getParent() != null);
    NodeUtil.isStatement(null);
  }
}