import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.BLOCK;
import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.LABEL;
import static com.google.javascript.rhino.NodeType.SCRIPT;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;

/**
 * Tests for NodeUtil class
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR);
    nodeForIn.setChild(0, Node.newNode(Token.IDENT, "for"));
    nodeForIn.setChild(1, Node.newNode(Token.IDENT, "var"));
    nodeForIn.setChild(2, Node.newNode(Token.ASSIGN, null, Node.newNode(Token.IDENT, "i"), Node.newNode(Token.NUMBER, "0")));
    nodeForIn.setChild(3, Node.newNode(Token.KEYWORD, "in"));
    nodeForIn.setChild(4, Node.newNode(Token.IDENT, "array"));

    Node nodeNotForIn = Node.newNode(Token.FOR);
    nodeNotForIn.setChild(0, Node.newNode(Token.IDENT, "for"));
    nodeNotForIn.setChild(1, Node.newNode(Token.IDENT, "var"));
    nodeNotForIn.setChild(2, Node.newNode(Token.ASSIGN, null, Node.newNode(Token.IDENT, "i"), Node.newNode(Token.NUMBER, "0")));
    nodeNotForIn.setChild(3, Node.newNode(Token.KEYWORD, "if"));

    assertFalse("Expect not to be FOR-IN", NodeUtil.isForIn(nodeNotForIn));
    assertTrue("Expect to be FOR-IN", NodeUtil.isForIn(nodeForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IDENT, "statement");
    Node nodeParentScript = Node.newNode(SCRIPT, null);
    Node nodeParentBlock = Node.newNode(BLOCK, null);
    Node nodeParentLabel = Node.newNode(LABEL, "label");

    nodeStatement.setParent(nodeParentScript);
    assertFalse("Expect not to be statement when parent is script", NodeUtil.isStatement(nodeStatement));

    nodeStatement.setParent(nodeParentBlock);
    assertTrue("Expect to be statement when parent is block", NodeUtil.isStatement(nodeStatement));

    nodeStatement.setParent(nodeParentLabel);
    assertFalse("Expect not to be statement when parent is label", NodeUtil.isStatement(nodeStatement));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullParent() {
    Node nodeStatement = Node.newNode(TOKEN_IDENT, "statement");
    nodeStatement.setParent(null);
    NodeUtil.isStatement(nodeStatement);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(FUNCTION, null);
    nodeFunctionDeclaration.setChild(0, Node.newNode(Token.IDENT, "functionName"));
    Node nodeParentScript = Node.newNode(SCRIPT, null);

    nodeFunctionDeclaration.setParent(nodeParentScript);
    assertFalse("Expect not to be function declaration when parent is script", NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationStatement = Node.newNode(FUNCTION, null);
    nodeFunctionDeclarationStatement.setChild(0, Node.newNode(Token.IDENT, "functionName"));
    nodeFunctionDeclarationStatement.setParent(Node.newNode(BLOCK, null));
    assertTrue("Expect to be function declaration when parent is statement", NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(FUNCTION, null);
    nodeFunctionDeclaration.setChild(0, Node.newNode(Token.IDENT, "functionName"));
    Node nodeParentScript = Node.newNode(SCRIPT, null);

    nodeFunctionDeclaration.setParent(nodeParentScript);
    assertFalse("Expect not to be hoisted function declaration when parent is script", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationScript = Node.newNode(SCRIPT, null);
    nodeFunctionDeclarationScript.setChild(0, nodeFunctionDeclaration);
    assertTrue("Expect to be hoisted function declaration when parent is script", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationFunction = Node.newNode(FUNCTION, null);
    nodeFunctionDeclarationFunction.setChild(0, Node.newNode(Token.IDENT, "functionName"));
    nodeFunctionDeclarationFunction.setParent(Node.newNode(FUNCTION, null));
    assertFalse("Expect not to be hoisted function declaration when parent is function", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationFunction));
  }

  private static final Token TOKEN_IDENT = new Token(Token.IDENT, "identifier", null);

}