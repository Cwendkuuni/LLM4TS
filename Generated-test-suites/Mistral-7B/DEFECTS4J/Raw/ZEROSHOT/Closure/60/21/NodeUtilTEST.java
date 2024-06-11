import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.NodeType;
import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newNode(NodeType.FOR, null, 0, 1, new int[] { 0, 1, 2 }, null);
    assertTrue(NodeUtil.isForIn(nodeFor));

    Node nodeIf = Node.newNode(NodeType.IF, null, 0, 1, new int[] { 0, 1 }, null);
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeFunction = Node.newNode(NodeType.FUNCTION, null, 0, 1, new int[] { 0, 1 }, null);
    assertFalse(NodeUtil.isStatement(nodeFunction));

    Node nodeScript = Node.newNode(NodeType.SCRIPT, null, 0, 1, new int[] {}, null);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeBlock = Node.newNode(NodeType.BLOCK, null, 0, 1, new int[] {}, null);
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeLabel = Node.newNode(NodeType.LABEL, null, 0, 1, new int[] { 0 }, null);
    assertTrue(NodeUtil.isStatement(nodeLabel));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newNode(NodeType.FUNCTION, null, 0, 1, new int[] { 0, 1 }, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionDeclaration = Node.newNode(
        NodeType.FUNCTION, "myFunction", 0, 1, new int[] { 0, 1 }, null);
    nodeFunctionDeclaration.setParent(Node.newNode(NodeType.SCRIPT, null, 0, 1, new int[] {}, null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newNode(NodeType.FUNCTION, null, 0, 1, new int[] { 0, 1 }, null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionDeclaration = Node.newNode(
        NodeType.FUNCTION, "myFunction", 0, 1, new int[] { 0, 1 }, null);
    nodeFunctionDeclaration.setParent(Node.newNode(NodeType.SCRIPT, null, 0, 1, new int[] {}, null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(
        NodeType.FUNCTION, "myFunction", 0, 1, new int[] { 0, 1 }, null);
    Node nodeFunctionInFunction = Node.newNode(NodeType.FUNCTION, null, 0, 1, new int[] { 0, 1 }, null);
    nodeFunctionInFunction.setParent(Node.newNode(NodeType.SCRIPT, null, 0, 1, new int[] {}, null));
    nodeFunctionDeclarationInFunction.setParent(nodeFunctionInFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test
  public void testPreconditions() {
    Preconditions.checkState(true);
    Preconditions.checkState(false, "Error message");
    Node node = Node.newNode(NodeType.SCRIPT, null, 0, 1, new int[] {}, null);
    Preconditions.checkState(node != null);
    Preconditions.checkState(NodeUtil.isStatement(node));
  }
}