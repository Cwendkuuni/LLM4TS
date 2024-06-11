import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case: a FOR loop with three children
    Node forNode = new Node(Token.FOR);
    forNode.addChildToBack(new Node(Token.EMPTY));
    forNode.addChildToBack(new Node(Token.EMPTY));
    forNode.addChildToBack(new Node(Token.EMPTY));
    Assert.assertTrue(NodeUtil.isForIn(forNode));

    // Edge case: a FOR loop with only two children
    Node forNode2 = new Node(Token.FOR);
    forNode2.addChildToBack(new Node(Token.EMPTY));
    forNode2.addChildToBack(new Node(Token.EMPTY));
    Assert.assertFalse(NodeUtil.isForIn(forNode2));

    // Edge case: a non-FOR loop with three children
    Node whileNode = new Node(Token.WHILE);
    whileNode.addChildToBack(new Node(Token.EMPTY));
    whileNode.addChildToBack(new Node(Token.EMPTY));
    whileNode.addChildToBack(new Node(Token.EMPTY));
    Assert.assertFalse(NodeUtil.isForIn(whileNode));
  }

  @Test
  public void testIsStatement() {
    // Typical use case: parent node is a SCRIPT
    Node scriptNode = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.EMPTY);
    statementNode.setParent(scriptNode);
    Assert.assertTrue(NodeUtil.isStatement(statementNode));

    // Typical use case: parent node is a BLOCK
    Node blockNode = new Node(Token.BLOCK);
    statementNode.setParent(blockNode);
    Assert.assertTrue(NodeUtil.isStatement(statementNode));

    // Typical use case: parent node is a LABEL
    Node labelNode = new Node(Token.LABEL);
    statementNode.setParent(labelNode);
    Assert.assertTrue(NodeUtil.isStatement(statementNode));

    // Edge case: parent node is neither SCRIPT, BLOCK, nor LABEL
    Node exprNode = new Node(Token.EXPR_RESULT);
    statementNode.setParent(exprNode);
    Assert.assertFalse(NodeUtil.isStatement(statementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case: a FUNCTION node that is a statement
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setParent(new Node(Token.SCRIPT));
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    // Edge case: a FUNCTION node that is not a statement
    Node functionNode2 = new Node(Token.FUNCTION);
    functionNode2.setParent(new Node(Token.EXPR_RESULT));
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(functionNode2));

    // Edge case: a non-FUNCTION node that is a statement
    Node varNode = new Node(Token.VAR);
    varNode.setParent(new Node(Token.SCRIPT));
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(varNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case: a FUNCTION node that is a statement,
    // and its parent is SCRIPT
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    functionNode.setParent(scriptNode);
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    // Typical use case: a FUNCTION node that is a statement,
    // and its parent's parent is FUNCTION
    Node functionNode2 = new Node(Token.FUNCTION);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    functionNode2.setParent(parentFunctionNode);
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode2));

    // Edge case: a FUNCTION node that is not a statement
    Node functionNode3 = new Node(Token.FUNCTION);
    functionNode3.setParent(new Node(Token.EXPR_RESULT));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode3));

    // Edge case: a non-FUNCTION node that is a statement
    Node varNode = new Node(Token.VAR);
    varNode.setParent(new Node(Token.SCRIPT));
    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(varNode));
  }
}