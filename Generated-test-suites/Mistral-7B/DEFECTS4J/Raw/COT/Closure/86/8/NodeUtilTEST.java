import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{1, 2, 3});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{1, 2, 3});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.SCRIPT, new int[]{1});
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.NUMBER, new int[]{1});
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1, 2}, null);
    nodeFunctionDeclaration.setProperty("id", new Node(Token.IDENTIFIER, new int[]{1}));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{1, 2}, null);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test(expected = AssertionError.class)
  public void testCheckParentNotNull() {
    Node node = new Node(Token.NUMBER, new int[]{1});
    Node parent = null;
    NodeUtil.isStatement(node);
    Preconditions.checkState(parent != null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1, 2}, null);
    nodeHoistedFunctionDeclaration.setProperty("id", new Node(Token.IDENTIFIER, new int[]{1}));
    Node parentScript = new Node(Token.SCRIPT, new int[]{1});
    Node parentFunction = new Node(Token.FUNCTION, new int[]{1, 2}, null);
    parentFunction.setProperty("id", new Node(Token.IDENTIFIER, new int[]{1}));
    parentScript.addChildToChildrenList(parentFunction);
    parentFunction.addChildToChildrenList(nodeHoistedFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{1, 2}, null);
    nodeFunctionDeclarationInFunction.setProperty("id", new Node(Token.IDENTIFIER, new int[]{1}));
    parentFunction.addChildToChildrenList(nodeFunctionDeclarationInFunction);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}