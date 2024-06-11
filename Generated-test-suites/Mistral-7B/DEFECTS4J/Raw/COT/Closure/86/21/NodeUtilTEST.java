import static org.junit.Assert.AssertFalse;
import static org.junit.Assert.AssertTrue;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{3});
    AssertTrue("Should return true for FOR-IN loop", NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{1});
    AssertFalse("Should return false for non-FOR-IN node", NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{1});
    AssertTrue("Should return true for statement node", NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.PLUS, new int[]{}, new int[]{1, 2});
    AssertFalse("Should return false for expression node", NodeUtil.isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{1});
    AssertTrue("Should return true for function declaration", NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{1}, new int[]{1});
    AssertFalse("Should return false for function expression", NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{"name"}, new int[]{1});
    Node parentOfFunctionDeclarationInScript = new Node(Token.SCRIPT, new int[]{1}, new int[]{1});
    parentOfFunctionDeclarationInScript.setChild(0, nodeFunctionDeclarationInScript);

    AssertTrue(
        "Should return true for hoisted function declaration in script",
        NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{1});
    Node parentOfFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{1}, new int[]{1});
    parentOfFunctionDeclarationInFunction.setChild(0, nodeFunctionDeclarationInFunction);

    AssertFalse(
        "Should return false for function declaration in function",
        NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}