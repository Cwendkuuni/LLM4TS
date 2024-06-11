import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, null, new int[0]);
    Node nodeFor = new Node(FOR, null, new int[0]);
    Node nodeIf = new Node(IF, null, new int[0]);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT, null, new int[0]);
    Node nodeFunction = new Node(FUNCTION, null, new int[0]);
    Node nodeIf = new Node(IF, null, new int[0]);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(FUNCTION, "functionName", new int[0]);
    Node nodeFunctionExpression = new Node(FUNCTION, null, new int[0]);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(SCRIPT, null, new int[0]);
    Node nodeFunction = new Node(FUNCTION, "functionName", new int[0]);
    Node nodeFunctionDeclarationInScript = nodeFunction;
    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, "functionName", new int[0]);
    Node nodeFunctionInFunction = new Node(FUNCTION, null, new int[0]);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));

    Node parentScript = new Node(SCRIPT, null, new int[0]);
    parentScript.setFirstChild(nodeFunctionDeclarationInFunction);
    NodeUtil.preconditionsCheckState(parentScript != null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}