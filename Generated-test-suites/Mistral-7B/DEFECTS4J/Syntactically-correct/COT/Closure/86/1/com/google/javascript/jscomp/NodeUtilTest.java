import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newNode(FOR); // FOR token
    Node threeChildrenNode = Node.newNode(FOR, new Node[] {Node.newNode(IDENTIFIER, "x"), Node.newNode(ASSIGN, OP_ASSIGN), Node.newNode(IDENTIFIER, "A")}); // FOR node with 3 children

    assertTrue(NodeUtil.isForIn(threeChildrenNode));
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(SCRIPT); // SCRIPT node
    Node blockNode = Node.newNode(BLOCK, new Node[0]); // BLOCK node with no children
    Node labelNode = Node.newNode(LABEL, "label"); // LABEL node
    Node functionNode = Node.newNode(FUNCTION, new Node[] {new Node(IDENTIFIER, "fn"), new Node(BLOCK, new Node[0])}); // FUNCTION node with name and block
    Node assignmentNode = Node.newNode(ASSIGN, OP_ASSIGN, Node.newNode(IDENTIFIER, "x"), Node.newNode(INT_LITERAL, 5)); // ASSIGN node

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(functionNode));
    assertFalse(NodeUtil.isStatement(assignmentNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(FUNCTION, new Node[] {Node.newNode(IDENTIFIER, "fn"), new Node(BLOCK, new Node[0])}); // FUNCTION node with name and block
    Node functionExpressionNode = Node.newNode(ASSIGN, OP_ASSIGN, Node.newNode(VAR, "fn"), Node.newNode(FUNCTION, new Node[0])); // ASSIGN node assigning a FUNCTION expression

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(SCRIPT, new Node[] {Node.newNode(FUNCTION, new Node[] {Node.newNode(IDENTIFIER, "foo"), new Node(BLOCK, new Node[0])})}); // SCRIPT node with a hoisted function declaration
    Node functionNode = Node.newNode(FUNCTION, new Node[] {Node.newNode(IDENTIFIER, "fn"), new Node(BLOCK, new Node[0])}); // FUNCTION node

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getFirstChild()));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateWithNullParent() {
    Node node = Node.newNode(SCRIPT); // SCRIPT node
    Preconditions.checkState(node.getParent() != null); // Check state should fail
  }
}