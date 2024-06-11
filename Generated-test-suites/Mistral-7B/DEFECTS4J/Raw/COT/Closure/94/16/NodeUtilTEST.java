import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(FOR);
    node.setChildCount(3);
    assertTrue(NodeUtil.isForIn(node));

    node.setType(ASSIGN); // Change node type to ASSIGN
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(SCRIPT);
    Node child = new Node(PRINT);
    node.setChild(0, child);

    assertTrue(NodeUtil.isStatement(node));
    assertTrue(NodeUtil.isStatement(child));

    node.setType(BLOCK); // Change node type to BLOCK
    assertTrue(NodeUtil.isStatement(node));

    child.setParent(null); // Orphan child node
    assertFalse(NodeUtil.isStatement(child));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParent() {
    Node node = new Node(SCRIPT);
    Node child = new Node(PRINT);
    Preconditions.checkState(node != null); // Check state for NodeUtil.isStatement
    NodeUtil.isStatement(child); // Throw exception when parent is null
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(FUNCTION);
    Node nameNode = new Node(IDENTIFIER, "myFunction");
    Node bodyNode = new Node(BLOCK);
    Node paramsNode = new Node(NULL); // No parameters

    functionNode.setChild(0, nameNode);
    functionNode.setChild(1, paramsNode);
    functionNode.setChild(2, bodyNode);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    functionNode.setType(FUNCTION_CALL); // Change node type to FUNCTION_CALL
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(FUNCTION);
    Node nameNode = new Node(IDENTIFIER, "myFunction");
    Node bodyNode = new Node(BLOCK);

    Node scriptNode = new Node(SCRIPT);
    scriptNode.setChild(0, functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    functionNode.setParent(new Node(ASSIGN)); // Change parent node type
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node functionInFunctionNode = new Node(FUNCTION);
    Node innerFunctionNode = new Node(FUNCTION);

    functionNode.setChild(0, nameNode);
    functionNode.setChild(1, paramsNode);
    functionNode.setChild(2, bodyNode);
    bodyNode.setChild(0, innerFunctionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(innerFunctionNode));
  }
}