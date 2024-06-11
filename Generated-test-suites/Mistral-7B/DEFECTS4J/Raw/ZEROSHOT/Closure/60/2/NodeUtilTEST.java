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
    Node nodeForIn = new Node(Token.FOR, null, null, null, null, 1, 1);
    Node nodeNotForIn = new Node(Token.IF, null, null, null, null, 1, 1);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null, null, null, null, 1, 1);
    Node nodeFunction = new Node(Token.FUNCTION, null, null, null, null, 1, 1);
    Node nodeExpression = new Node(Token.IF, null, null, null, null, 1, 1);

    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeFunction));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, null, null, null, null, 1, 1);
    Node nodeExpression = new Node(Token.FUNCTION, null, null, null, null, 1, 1);

    assertTrue(isFunctionDeclaration(nodeFunction));
    assertFalse(isFunctionDeclaration(nodeExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInScript = new Node(Token.FUNCTION, null, null, null, null, 1, 1);
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, null, null, null, null, 1, 1);
    Node nodeFunctionInBlock = new Node(Token.FUNCTION, null, null, null, null, 1, 1);
    Node nodeFunctionInExpression = new Node(Token.FUNCTION, null, null, null, null, 1, 1);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInBlock));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInExpression));
  }

  @Test(expected = AssertionError.class)
  public void testCheckState() {
    Node node = new Node(Token.INVALID, null, null, null, null, 1, 1);
    Preconditions.checkState(node.getParent() == null);
  }
}