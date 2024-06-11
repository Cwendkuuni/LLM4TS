import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new int[]{}, new int[]{1, 2, 3}); // FOR node with 3 children
    assertTrue(isForIn(node));

    Node invalidNode = new Node(Token.IF, new int[]{}, new int[]{1, 2}); // IF node with 2 children
    assertFalse(isForIn(invalidNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[]{}, new int[]{1, 2, 3}); // IF node with 3 children (can be a statement)
    assertTrue(isStatement(statementNode));

    Node expressionNode = new Node(Token.IF, new int[]{}, new int[]{1}); // IF node with 1 child (cannot be a statement)
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3, 4, 5}); // Function node with 5 children (name, parameters, body)
    assertTrue(isFunctionDeclaration(functionNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new int[]{Token.IDENT, 1}, new int[]{1, 2, 3, 4, 5}); // Function expression node with name and arguments
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3, 4, 5}); // Function declaration at the top level
    Node notHoistedFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3, 4, 5}, new Node(Token.SCRIPT, new int[]{}, new int[]{1, 2, 3})); // Function declaration inside a script
    Node hoistedFunctionInsideFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3, 4, 5}, new Node(Token.FUNCTION, new int[]{Token.IDENT, 1}, new int[]{1, 2, 3})); // Function declaration inside a function

    assertTrue(isHoistedFunctionDeclaration(hoistedFunction));
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunction));
    assertFalse(isHoistedFunctionDeclaration(hoistedFunctionInsideFunction));
  }
}