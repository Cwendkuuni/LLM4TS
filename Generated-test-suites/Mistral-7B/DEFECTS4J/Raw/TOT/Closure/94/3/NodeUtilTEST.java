import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeType;
import com.google.javascript.jscomp.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {0, 0, 1});
    Node nodeNotForIn = Node.newNode(Token.FUNCTION, new int[] {0, 0, 0});

    assertTrue("Expected FOR-IN node to be identified as FOR-IN", NodeUtil.isForIn(nodeForIn));
    assertFalse("Expected non-FOR-IN node to not be identified as FOR-IN", NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {0, 0, 0});
    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] {0, 0, 0});
    Node nodeExpression = Node.newNode(Token.IDENTIFIER, new int[] {0, 0, 0});

    assertTrue("Expected script node to be identified as statement", NodeUtil.isStatement(nodeScript));
    assertTrue("Expected function declaration node to be identified as statement", NodeUtil.isStatement(nodeFunction));
    assertFalse("Expected expression node to not be identified as statement", NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0, 1, 0}, Node.newNode(Token.IDENTIFIER, new int[] {0, 0, 0}));
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {0, 1, 0}, Node.newNode(Token.ASSIGNMENT, new int[] {1, 0, 0}, Node.newNode(Token.IDENTIFIER, new int[] {1, 0, 0}), Node.newNode(Token.IDENTIFIER, new int[] {2, 0, 0})));

    assertTrue("Expected function declaration node to be identified as function declaration", NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse("Expected function expression node to not be identified as function declaration", NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {0, 0, 0});
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0, 1, 0}, Node.newNode(Token.IDENTIFIER, new int[] {0, 0, 0}));
    Node functionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] {0, 1, 0}, Node.newNode(Token.BLOCK, new int[] {0, 0, 0}, functionDeclaration));

    assertFalse("Expected non-hoisted function declaration to not be identified as hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertTrue("Expected hoisted function declaration to be identified as hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }

  @Test
  public void testNullNodes() {
    assertNull("Expected null for null Node", NodeUtil.isForIn(null));
    assertNull("Expected null for null Node", NodeUtil.isStatement(null));
    assertNull("Expected null for null Node", NodeUtil.isFunctionDeclaration(null));
  }

  @Test
  public void testEmptyNodes() {
    Node emptyNode = Node.newNode(Token.EMPTY, new int[] {0, 0, 0});

    assertFalse("Expected empty Node to not be identified as FOR-IN", NodeUtil.isForIn(emptyNode));
    assertFalse("Expected empty Node to not be identified as statement", NodeUtil.isStatement(emptyNode));
    assertFalse("Expected empty Node to not be identified as function declaration", NodeUtil.isFunctionDeclaration(emptyNode));
  }

  @Test
  public void testUnknownNodes() {
    Node unknownNode = Node.newNode(5, new int[] {0, 0, 0});

    assertFalse("Expected unknown Node type to not be identified as FOR-IN", NodeUtil.isForIn(unknownNode));
    assertFalse("Expected unknown Node type to not be identified as statement", NodeUtil.isStatement(unknownNode));
    assertFalse("Expected unknown Node type to not be identified as function declaration", NodeUtil.isFunctionDeclaration(unknownNode));
  }

  @Test
  public void testInvalidNodeTypes() {
    Node invalidNode1 = Node.newNode(Token.IF, new int[] {0, 0, 0});
    Node invalidNode2 = Node.newNode(Token.ASSIGNMENT, new int[] {0, 1, 0}, Node.newNode(Token.IDENTIFIER, new int[] {0, 0, 0}), Node.newNode(Token.NUMBER_LITERAL, new int[] {1, 0, 0}));

    assertFalse("Expected IF node to not be identified as FOR-IN", NodeUtil.isForIn(invalidNode1));
    assertFalse("Expected ASSIGNMENT node to not be identified as statement", NodeUtil.isStatement(invalidNode1));
    assertFalse("Expected ASSIGNMENT node to not be identified as function declaration", NodeUtil.isFunctionDeclaration(invalidNode1));

    assertFalse("Expected IF node to not be identified as FOR-IN", NodeUtil.isForIn(invalidNode2));
    assertFalse("Expected ASSIGNMENT node to not be identified as statement", NodeUtil.isStatement(invalidNode2));
    assertFalse("Expected ASSIGNMENT node to not be identified as function declaration", NodeUtil.isFunctionDeclaration(invalidNode2));
  }
}