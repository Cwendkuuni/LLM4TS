import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.jscomp.NodeUtil.isStatement;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isHoistedFunctionDeclaration;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{0, 0, 0});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{0, 0});
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{0, 0});
    Node nodeNotStatement = new Node(Token.RETURN, new int[]{0, 0});
    Node nodeParentScript = new Node(Token.SCRIPT, new int[]{0, 0});
    Node nodeParentBlock = new Node(Token.BLOCK, new int[]{0, 0});
    Node nodeParentLabel = new Node(Token.LABEL, new int[]{0, 0});

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeNotStatement));

    Preconditions.checkState(nodeParentScript != null);
    Preconditions.checkState(nodeParentBlock != null);
    Preconditions.checkState(nodeParentLabel != null);

    assertTrue(isStatement(nodeStatement), "Statement in script context");
    assertTrue(isStatement(nodeStatement), "Statement in block context");
    assertFalse(isStatement(nodeNotStatement), "Not statement in label context");
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0, 0, 1, 1}, null, null);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{0, 0, 1, 1}, null, null);
    Node nodeNotFunction = new Node(Token.IF, new int[]{0, 0});

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(isFunctionDeclaration(nodeNotFunction));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRoot = new Node(Token.FUNCTION, new int[]{0, 0, 1, 1}, null, null);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{0, 0, 1, 1}, null, null);
    Node nodeFunctionExpressionInRoot = new Node(Token.FUNCTION, new int[]{0, 0, 1, 1}, null, null);
    Node nodeFunctionExpressionInFunction = new Node(Token.FUNCTION, new int[]{0, 0, 1, 1}, null, null);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpressionInRoot));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpressionInFunction));
  }
}