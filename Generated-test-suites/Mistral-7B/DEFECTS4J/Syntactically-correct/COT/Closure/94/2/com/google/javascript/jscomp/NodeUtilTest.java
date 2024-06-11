import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.jscomp.NodeUtil.isStatement;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isHoistedFunctionDeclaration;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3}, null);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1}, null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] {1}, null);
    Node parentScript = Node.newNode(Token.SCRIPT, new int[] {0}, null);
    Node parentBlock = Node.newNode(Token.BLOCK, new int[] {0}, null);
    Node parentLabel = Node.newNode(Token.LABEL, new int[] {1}, null);

    parentScript.addChild(nodeStatement);
    assertTrue(isStatement(nodeStatement));

    parentBlock.addChild(nodeStatement);
    assertTrue(isStatement(nodeStatement));

    parentLabel.addChild(nodeStatement);
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.NUMBER, new int[] {1}, null);
    Node parentStatement = nodeStatement;

    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new int[] {0});
    Node name = nodeFunctionDeclaration.getFirstChild();
    Node body = nodeFunctionDeclaration.getLastChild();

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(name != null);
    assertTrue(body != null);

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new int[] {0, 0});
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRoot = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new int[] {0});
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] {1, 1}, new int[] {0, 0});
    Node functionParent = Node.newNode(Token.FUNCTION, new int[] {0}, null);
    Node root = Node.newNode(Token.SCRIPT, new int[] {0}, null);

    root.addChild(nodeFunctionDeclarationInRoot);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));

    functionParent.addChild(nodeFunctionDeclarationInFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPreconditionsCheckStateWithNullParent() {
    Node node = Node.newNode(Token.IF, new int[] {1}, null);
    Node nullParent = null;
    Preconditions.checkState(nullParent != null, "Parent should not be null.");
    node.setParent(nullParent);
  }
}