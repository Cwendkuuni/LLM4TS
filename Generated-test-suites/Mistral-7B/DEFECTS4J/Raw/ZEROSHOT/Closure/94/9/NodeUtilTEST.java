import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.common.base.Preconditions.checkState;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, null);
    Node nodeForWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENT, "i"), new Node(Token.ASSIGN, null), new Node(Token.IDENT, "array")});

    assertFalse("A single FOR token is not a FOR-IN loop", NodeUtil.isForIn(nodeFor));
    assertTrue("A FOR-IN loop with 3 children is a FOR-IN loop", NodeUtil.isForIn(nodeForWithThreeChildren));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null);
    Node nodeBlock = new Node(Token.BLOCK, null);
    Node nodeLabel = new Node(Token.LABEL, "label");
    Node nodeFunction = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "funcName")});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "funcName"), new Node(Token.ASSIGN, null), new Node(Token.IDENT, "arg")});

    Node nodeExpression = new Node(Token.ASSIGN, null);

    assertFalse("A SCRIPT node is not a statement", NodeUtil.isStatement(nodeScript));
    assertTrue("A BLOCK node is a statement", NodeUtil.isStatement(nodeBlock));
    assertTrue("A LABEL node is a statement", NodeUtil.isStatement(nodeLabel));
    assertFalse("A FUNCTION node can be either part of an expression or a statement, but cannot be determined with this method alone", NodeUtil.isStatement(nodeFunction));
    assertFalse("A FUNCTION EXPRESSION node is not a statement", NodeUtil.isStatement(nodeFunctionExpression));
    assertFalse("An expression node is not a statement", NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "funcName")});
    Node nodeFunctionWithBody = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "funcName"), new Node(Token.BLOCK, null)});

    assertFalse("A function expression is not a function declaration", NodeUtil.isFunctionDeclaration(nodeFunction));
    assertTrue("A function declaration is a function declaration", NodeUtil.isFunctionDeclaration(nodeFunctionWithBody));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInScript = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "funcName")});
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "funcName"), new Node(Token.BLOCK, null)});

    assertFalse("A function declaration in the global scope is not hoisted", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertTrue("A function declaration inside a function is hoisted", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test
  public void testPreconditions() {
    Node nodeParentNull = new Node(Token.IDENT, "node");

    NodeUtil.isStatement(nodeParentNull);

    checkState(false, "getParent should not return null");
  }
}