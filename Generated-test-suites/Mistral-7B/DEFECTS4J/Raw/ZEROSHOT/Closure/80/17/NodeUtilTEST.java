import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode(FOR, new int[] {0, 1, 2});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = createNode(IF, new int[] {0, 1});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode(ADD, new int[] {0, 1, 2});
    Node parent = createNode(BLOCK, new int[] {0});
    parent.addChild(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = createNode(ADD, new int[] {0, 1, 2});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(
        FUNCTION, new int[] {0, 1, 2}, new String[] {"name"}, new Node[] {});
    Node parent = createNode(BLOCK, new int[] {0});
    parent.addChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNode(
        FUNCTION, new int[] {0, 1, 2}, new String[] {"name"}, new Node[] {});
    Node statement = createNode(ASSIGN, new int[] {0, 1, 2});
    Node parentFunctionExpression = createNode(BLOCK, new int[] {0});
    parentFunctionExpression.addChild(nodeFunctionExpression);
    parentFunctionExpression.addChild(statement);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode(
        FUNCTION, new int[] {0, 1, 2}, new String[] {"name"}, new Node[] {});
    Node parentScript = createNode(SCRIPT, new int[] {0});
    parentScript.addChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = createNode(
        FUNCTION, new int[] {0, 1, 2}, new String[] {"name"}, new Node[] {});
    Node parentFunction = createNode(FUNCTION, new int[] {0, 1}, new String[] {},
        new Node[] {nodeFunctionDeclarationInFunction});
    Node parentScriptInFunction = createNode(SCRIPT, new int[] {0});
    parentScriptInFunction.addChild(parentFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}