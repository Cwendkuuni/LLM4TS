import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  Node testNodeForIn;
  Node testNodeStatement;
  Node testNodeFunctionDeclaration;
  Node testNodeHoistedFunctionDeclaration;
  Node testNodeFunctionExpression;

  @org.junit.Before
  public void setUp() {
    // Initialize test nodes here, for example:
    testNodeForIn = new Node(Token.FOR, new Node[3]);
    testNodeStatement = new Node(Token.PRINT, new Node("test"));
  }

  @org.junit.Test
  public void testIsForIn() {
    assertTrue(isForIn(testNodeForIn));
  }

  @org.junit.Test
  public void testIsForInWithInvalidNodeType() {
    assertFalse(isForIn(testNodeStatement));
  }

  @org.junit.Test
  public void testIsStatement() {
    assertTrue(isStatement(testNodeStatement));
  }

  @org.junit.Test
  public void testIsStatementWithInvalidNodeType() {
    assertFalse(isStatement(testNodeForIn));
  }

  @org.junit.Before
  public void setUpNodes() {
    testNodeFunctionDeclaration = new Node(Token.FUNCTION, new Node("test"), new Node[0]);
    testNodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new Node("test"), new Node[0]);
    testNodeFunctionExpression = new Node(Token.FUNCTION, new Node("test"), new Node[0]);
    testNodeFunctionExpression.setParent(new Node(Token.ASSIGN, new Node(Token.IDENT, "test"), testNodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    assertTrue(isFunctionDeclaration(testNodeFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationWithInvalidNodeType() {
    assertFalse(isFunctionDeclaration(testNodeStatement));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    assertTrue(isHoistedFunctionDeclaration(testNodeHoistedFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationWithNonHoistedFunctionDeclaration() {
    assertFalse(isHoistedFunctionDeclaration(testNodeFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsFunctionExpression() {
    assertFalse(isFunctionDeclaration(testNodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsFunctionExpressionWithFunctionExpression() {
    assertTrue(isFunctionExpression(testNodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsFunctionExpressionWithInvalidNodeType() {
    assertFalse(isFunctionExpression(testNodeStatement));
  }
}