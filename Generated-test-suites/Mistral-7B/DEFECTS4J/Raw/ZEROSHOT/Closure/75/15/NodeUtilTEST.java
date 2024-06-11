import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newNode(Token.FOR, new int[] {1, 2, 3, 4, 5});
    Node nodeIf = Node.newNode(Token.IF, new int[] {1, 2, 3, 4, 5});

    assertTrue(isForIn(nodeFor));
    assertFalse(isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {1, 2, 3, 4, 5});
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4, 5});
    Node nodeIf = Node.newNode(Token.IF, new int[] {1, 2, 3, 4, 5});
    Node nodeFor = Node.newNode(Token.FOR, new int[] {1, 2, 3, 4, 5});

    Node nodeExpression = Node.newNode(Token.IDENT, new int[] {1, 2, 3, 4, 5});
    Node nodeLiteral = Node.newNode(Token.NUM_LIT, new int[] {1, 2, 3, 4, 5});

    Node parentScript = Node.newNode(Token.SCRIPT, new int[] {1, 2, 3, 4, 5});
    Node parentBlock = Node.newNode(Token.BLOCK, new int[] {1, 2, 3, 4, 5});
    Node parentLabel = Node.newNode(Token.LABEL, new int[] {1, 2, 3, 4, 5});
    Node parentFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4, 5});

    // Test when node has a parent
    Node nodeWithParent = Node.newNode(Token.IDENT, new int[] {1, 2, 3, 4, 5});
    nodeWithParent.setParent(parentScript);

    // Is statement tests
    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeFunction));
    assertTrue(isStatement(nodeBlock));
    assertTrue(isStatement(nodeLabel));
    assertFalse(isStatement(nodeIf));
    assertFalse(isStatement(nodeFor));
    assertFalse(isStatement(nodeExpression));
    assertFalse(isStatement(nodeLiteral));

    // Test when node has no parent
    Node nodeNoParent = Node.newNode(Token.IDENT, new int[] {1, 2, 3, 4, 5});
    assertFalse(isStatement(nodeNoParent));

    // Test when node has an expression parent
    Node expressionParent = Node.newNode(Token.ASSIGN, new int[] {1, 2, 3, 4, 5});
    expressionParent.setRight(nodeFunction);
    assertFalse(isStatement(nodeFunction));

    // Test when node is a function expression (not a statement)
    Node functionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4, 5});
    functionExpression.setProperty("name", "testFunction");
    assertFalse(isStatement(functionExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4, 5});
    Node nodeIf = Node.newNode(Token.IF, new int[] {1, 2, 3, 4, 5});
    Node nodeFor = Node.newNode(Token.FOR, new int[] {1, 2, 3, 4, 5});

    Node parentScript = Node.newNode(Token.SCRIPT, new int[] {1, 2, 3, 4, 5});
    Node parentBlock = Node.newNode(Token.BLOCK, new int[] {1, 2, 3, 4, 5});

    // Test when node is a function declaration
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4, 5});
    functionDeclaration.setProperty("name", "testFunction");
    functionDeclaration.setParent(parentScript);
    assertTrue(isFunctionDeclaration(functionDeclaration));

    // Test when node is not a function declaration
    assertFalse(isFunctionDeclaration(nodeFunction));
    assertFalse(isFunctionDeclaration(nodeIf));
    assertFalse(isFunctionDeclaration(nodeFor));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4, 5});
    Node nodeIf = Node.newNode(Token.IF, new int[] {1, 2, 3, 4, 5});
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {1, 2, 3, 4, 5});
    Node nodeBlock = Node.newNode(Token.BLOCK, new int[] {1, 2, 3, 4, 5});
    Node nodeFunctionInScript = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4, 5});
    Node nodeFunctionInBlock = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4, 5});

    // Test when node is a hoisted function declaration
    Node hoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2, 3, 4, 5});
    hoistedFunctionDeclaration.setProperty("name", "testFunction");
    hoistedFunctionDeclaration.setParent(nodeScript);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    // Test when node is not a hoisted function declaration
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeIf));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInBlock));
  }
}