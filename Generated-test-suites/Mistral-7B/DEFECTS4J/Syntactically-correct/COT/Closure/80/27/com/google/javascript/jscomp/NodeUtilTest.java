import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, new int[0]);
    Node nodeFor = new Node(FOR, new int[0]);
    Node nodeIf = new Node(IF, new int[0]);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT, new int[0]);
    Node nodeFunction = new Node(FUNCTION, new int[0]);
    Node nodeExpression = new Node(IDENT, new int[0]);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsStatementNullParent() {
    Node node = new Node(SCRIPT, new int[0]);
    Preconditions.checkState(node.getParent() != null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, new int[0]);
    Node nodeFunctionExpression = new Node(FUNCTION, new int[0], new int[] { IDENT, OP_COLON });
    Node nodeIf = new Node(IF, new int[0]);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIf));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(SCRIPT, new int[0]);
    Node nodeFunctionDeclaration = new Node(FUNCTION, new int[0]);
    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, new int[0], new int[] { IDENT, OP_COLON }, new Node[]{nodeFunctionDeclaration});

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}