import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setup() {
     nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn() {
    // Typical use case: FOR node with 3 children
    Node forNode = new Node(Token.FOR);
    forNode.addChildToBack(new Node(Token.NAME));
    forNode.addChildToBack(new Node(Token.NAME));
    forNode.addChildToBack(new Node(Token.NAME));
    Assert.assertTrue(nodeUtil.isForIn(forNode));

    // Edge case: FOR node with less than 3 children
    forNode.removeChild(forNode.getLastChild());
    Assert.assertFalse(nodeUtil.isForIn(forNode));

    // Edge case: FOR node with more than 3 children
    forNode.addChildToBack(new Node(Token.NAME));
    Assert.assertFalse(nodeUtil.isForIn(forNode));

    // Error scenario: non-FOR node
    Node nonForNode = new Node(Token.IF);
    Assert.assertFalse(nodeUtil.isForIn(nonForNode));
  }

  @Test
  public void testIsStatement() {
    // Typical use case: PARENT is SCRIPT, BLOCK, or LABEL
    Node parent = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.BLOCK);
    statementNode.addChildToBack(parent);
    Assert.assertTrue(nodeUtil.isStatement(statementNode));

    // Edge case: PARENT is not SCRIPT, BLOCK, or LABEL
    Node nonStatementNode = new Node(Token.IF);
    Assert.assertFalse(nodeUtil.isStatement(nonStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case: FUNCTION node that is a statement
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(functionNode);
    functionNode.addChildToBack(new Node(Token.NAME));
    Assert.assertTrue(nodeUtil.isFunctionDeclaration(functionNode));

    // Edge case: FUNCTION node that is not a statement
    Node nonFunctionDeclarationNode = new Node(Token.FUNCTION);
    Assert.assertFalse(nodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case: FUNCTION node that is a hoisted declaration
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(functionNode);
    functionNode.addChildToBack(new Node(Token.NAME));
    Assert.assertTrue(nodeUtil.isHoistedFunctionDeclaration(functionNode));

    // Edge case: FUNCTION node in nested function
    Node parentFunctionNode = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.SCRIPT);
    grandParent.addChildToBack(parentFunctionNode);
    parentFunctionNode.addChildToBack(new Node(Token.NAME));
    parentFunctionNode.addChildToBack(functionNode);
    Assert.assertTrue(nodeUtil.isHoistedFunctionDeclaration(functionNode));

    // Edge case: FUNCTION node that is not in the scope root
    Node nonHoistedFunctionNode = new Node(Token.FUNCTION);
    Node nonParent = new Node(Token.BLOCK);
    nonParent.addChildToBack(nonHoistedFunctionNode);
    nonHoistedFunctionNode.addChildToBack(new Node(Token.NAME));
    Assert.assertFalse(nodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionNode));

    // Error scenario: non-FUNCTION node
    Node nonFunctionNode = new Node(Token.IF);
    Assert.assertFalse(nodeUtil.isHoistedFunctionDeclaration(nonFunctionNode));
  }
}