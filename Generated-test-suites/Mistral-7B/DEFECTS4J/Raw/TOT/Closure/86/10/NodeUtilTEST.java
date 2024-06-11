import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.common.base.Preconditions.checkState;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeType;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] { 0, 1, 2 }, new Node[] {});
    assertTrue("Incorrectly identified as not FOR-IN loop.", NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] { 0, 1 }, new Node[] {});
    assertFalse("Incorrectly identified as FOR-IN loop.", NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] { 0 }, new Node[] {});
    Node nodeStatement = Node.newNode(Token.IF, new int[] { 0, 1 }, new Node[] { Node.newNode(Token.IDENT, new int[] { 0 }, new String[] { "foo" }) });
    Node nodeExpression = Node.newNode(Token.IF, new int[] { 0, 1 }, new Node[] { Node.newNode(Token.IDENT, new int[] { 0 }, new String[] { "foo" }) });

    assertTrue("Incorrectly identified as not statement.", NodeUtil.isStatement(nodeScript));
    assertTrue("Incorrectly identified as statement.", NodeUtil.isStatement(nodeStatement));
    assertFalse("Incorrectly identified as statement.", NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] { 0, 1 }, new Node[] { Node.newNode(Token.IDENT, new int[] { 0 }, new String[] { "foo" }) });
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] { 0 }, new Node[] { Node.newNode(Token.IDENT, new int[] { 0 }, new String[] { "foo" }) });

    assertFalse("Incorrectly identified as function declaration.", NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertTrue("Incorrectly identified as not function declaration.", NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] { 0 }, new Node[] {});
    Node nodeFunctionDeclarationInScript = Node.newNode(Token.FUNCTION, new int[] { 0, 1 }, new Node[] { Node.newNode(Token.IDENT, new int[] { 0 }, new String[] { "foo" }) });
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] { 0, 1 }, new Node[] { Node.newNode(Token.BLOCK, new int[] { 0 }, new Node[] { nodeFunctionDeclarationInScript }) });

    assertFalse("Incorrectly identified as hoisted function declaration.", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse("Incorrectly identified as not hoisted function declaration.", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] { 0, 1 }, new Node[] { Node.newNode(Token.IDENT, new int[] { 0 }, new String[] { "foo" }) });
    Node nodeScriptWithHoistedFunctionDeclaration = Node.newNode(Token.SCRIPT, new int[] { 0 }, new Node[] { nodeHoistedFunctionDeclaration });

    assertTrue("Incorrectly identified as not hoisted function declaration.", NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertTrue("Incorrectly identified as hoisted function declaration.", NodeUtil.isHoistedFunctionDeclaration(nodeScriptWithHoistedFunctionDeclaration));
  }

  @Test
  public void testPreconditionsCheckState() {
    Node node = null;
    checkState(node != null, "Parent node is null.");
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateWithFailure() {
    Node node = Node.newNode(Token.SCRIPT, new int[] { 0 }, new Node[] {});
    checkState(NodeUtil.isForIn(node), "Expected FOR-IN node, but got STATEMENT.");
  }
}