import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical use case: n is a FOR node with 3 children
    Node forNode = new Node(Token.FOR);
    forNode.addChildToFront(new Node(Token.NAME));
    forNode.addChildToFront(new Node(Token.NAME));
    forNode.addChildToFront(new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(forNode));

    // Edge case: n is a FOR node with less than 3 children
    Node lessThan3Node = new Node(Token.FOR);
    lessThan3Node.addChildToFront(new Node(Token.NAME));
    lessThan3Node.addChildToFront(new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(lessThan3Node));

    // Edge case: n is not a FOR node
    Node notForNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isForIn(notForNode));

    // Edge case: n is null
    assertFalse(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsStatement() {
    // Typical use case: n is a statement node with a valid parent
    Node scriptNode = new Node(Token.SCRIPT);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToFront(new Node(Token.NAME));
    scriptNode.addChildToFront(blockNode);
    assertTrue(NodeUtil.isStatement(blockNode));

    // Edge case: n is not a statement node
    Node notStatementNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isStatement(notStatementNode));

    // Edge case: n has null parent
    assertFalse(NodeUtil.isStatement(new Node(Token.NAME)));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case: n is a function declaration node that is also a statement
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToFront(new Node(Token.NAME));
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    // Edge case: n is not a function declaration node
    Node notFunctionNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));

    // Edge case: n is a function declaration node but not a statement
    Node notStatementNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(notStatementNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case: n is a hoisted function declaration
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToFront(new Node(Token.NAME));
    scriptNode.addChildToFront(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    // Edge case: n is not a hoisted function declaration
    Node notHoistedFunctionNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));

    // Edge case: n is not a function declaration node
    Node notFunctionNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notFunctionNode));

    // Edge case: n is a hoisted function declaration node but not a statement
    Node notStatementNode = new Node(Token.FUNCTION);
    Node parentFunctionNode = new Node(Token.FUNCTION);
    parentFunctionNode.addChildToFront(notStatementNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notStatementNode));
  }
}