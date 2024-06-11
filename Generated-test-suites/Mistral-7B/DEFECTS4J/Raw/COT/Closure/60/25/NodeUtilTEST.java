import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, new int[]{0, 0, 0}, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(IF, new int[]{0, 0}, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(PRINT, new int[]{0, 0}, null);
    Node parent = new Node(SCRIPT, new int[]{0}, new Node[]{nodeStatement});
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(parent));

    Node nodeNotStatement = new Node(ASSIGN, new int[]{0, 0}, null);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(
        FUNCTION, new int[]{0, 0, 1}, new Node[]{new Node("name", new int[]{0, 0}, null)});
    nodeFunctionDeclaration.setLine(1);
    nodeFunctionDeclaration.setColumn(1);

    Node parent = new Node(SCRIPT, new int[]{0}, new Node[]{nodeFunctionDeclaration});

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(new Node(FUNCTION, new int[]{0, 0, 1}, null)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullParent() {
    Node nodeFunctionDeclaration = new Node(
        FUNCTION, new int[]{0, 0, 1}, new Node[]{new Node("name", new int[]{0, 0}, null)});
    NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(
        FUNCTION, new int[]{0, 0, 1}, new Node[]{new Node("name", new int[]{0, 0}, null)});
    Node parentScript = new Node(SCRIPT, new int[]{0}, null);
    Node parentFunction = new Node(FUNCTION, new int[]{0, 0, 1}, null);
    parentFunction.setLine(1);
    parentFunction.setColumn(1);
    parentScript.setChildren(new Node[]{parentFunction, nodeFunctionDeclaration});

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(
        FUNCTION, new int[]{0, 0, 1}, new Node[]{new Node("name", new int[]{0, 0}, null)});
    Node parentFunctionInFunction = new Node(FUNCTION, new int[]{0, 0, 1}, null);
    parentFunctionInFunction.setLine(1);
    parentFunctionInFunction.setColumn(1);
    nodeFunctionDeclarationInFunction.setParent(parentFunctionInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullParentFunction() {
    Node nodeFunctionDeclaration = new Node(
        FUNCTION, new int[]{0, 0, 1}, new Node[]{new Node("name", new int[]{0, 0}, null)});
    Node parentScript = new Node(SCRIPT, new int[]{0}, null);
    Node parentFunction = new Node(FUNCTION, new int[]{0, 0, 1}, null);
    parentFunction.setLine(1);
    parentFunction.setColumn(1);
    parentScript.setChildren(new Node[]{parentFunction, nodeFunctionDeclaration});

    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration);
  }
}