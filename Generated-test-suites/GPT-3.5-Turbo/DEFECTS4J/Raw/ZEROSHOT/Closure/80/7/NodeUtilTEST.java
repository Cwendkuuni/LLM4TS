import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  // Test isForIn method
  @Test
  public void testIsForIn() {
    // Test for FOR-IN loop node
    Node forInNode = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(forInNode));

    // Test for non-FOR-IN loop node
    Node nonForInNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isForIn(nonForInNode));
  }

  // Test isStatement method
  @Test
  public void testIsStatement() {
    // Test for statement node
    Node statementNode = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    assertTrue(NodeUtil.isStatement(statementNode));

    // Test for non-statement node
    Node nonStatementNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isStatement(nonStatementNode));
  }

  // Test isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    // Test for function declaration node
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    // Test for non-function declaration node
    Node nonFunctionDeclarationNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
  }

  // Test isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test for hoisted function declaration node
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    hoistedFunctionDeclarationNode.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    // Test for non-hoisted function declaration node
    Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }

}