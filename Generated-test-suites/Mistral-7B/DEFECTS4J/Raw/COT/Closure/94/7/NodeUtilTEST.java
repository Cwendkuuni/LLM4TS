import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChild(0, new Node(Token.KEYWORD, "for"));
    nodeForIn.setChild(1, new Node(Token.LEFT_PAREN, "("));
    nodeForIn.setChild(2, new Node(Token.IDENT, "i"));
    nodeForIn.setChild(3, new Node(Token.SEMICOLON, ";"));
    nodeForIn.setChild(4, new Node(Token.KEYWORD, "in"));
    nodeForIn.setChild(5, new Node(Token.LEFT_PAREN, "("));
    nodeForIn.setChild(6, new Node(Token.IDENT, "arr"));
    nodeForIn.setChild(7, new Node(Token.RIGHT_PAREN, ")"));

    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IDENT, "someVariable");
    assertFalse(isForIn(nodeNotForIn));

    Node nodeIf = new Node(Token.IF);
    assertFalse(isForIn(nodeIf));

    Node nodeFunction = new Node(Token.FUNCTION, "myFunction");
    assertFalse(isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT);
    assertTrue(isStatement(nodeScript));

    Node nodeFunction = new Node(Token.FUNCTION, "myFunction");
    assertFalse(isStatement(nodeFunction)); // function expression
    assertTrue(isStatement(nodeFunction.getFirstChild())); // function declaration

    Node nodeIf = new Node(Token.IF);
    assertTrue(isStatement(nodeIf));

    Node nodeVariable = new Node(Token.VAR, "myVariable");
    assertTrue(isStatement(nodeVariable));

    Node nodeExpression = new Node(Token.IDENT, "someExpression");
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "myFunction");
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENT, "myFunction"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.LEFT_PAREN, "("));
    nodeFunctionDeclaration.setChild(2, new Node(Token.RIGHT_PAREN, ")"));
    nodeFunctionDeclaration.setChild(3, new Node(Token.BLOCK, ""));

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, "myFunction");
    nodeFunctionExpression.setChild(0, new Node(Token.IDENT, "myFunction"));
    nodeFunctionExpression.setChild(1, new Node(Token.LEFT_PAREN, "("));
    nodeFunctionExpression.setChild(2, new Node(Token.RIGHT_PAREN, ")"));
    nodeFunctionExpression.setChild(3, nodeScript);

    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT);

    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, "myFunction");
    nodeFunctionDeclarationInScript.setChild(0, new Node(Token.IDENT, "myFunction"));
    nodeFunctionDeclarationInScript.setChild(1, new Node(Token.LEFT_PAREN, "("));
    nodeFunctionDeclarationInScript.setChild(2, new Node(Token.RIGHT_PAREN, ")"));
    nodeFunctionDeclarationInScript.setChild(3, new Node(Token.BLOCK, ""));
    nodeScript.setFirstChild(nodeFunctionDeclarationInScript);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, "myFunction");
    nodeFunctionDeclarationInFunction.setChild(0, new Node(Token.IDENT, "myFunction"));
    nodeFunctionDeclarationInFunction.setChild(1, new Node(Token.LEFT_PAREN, "("));
    nodeFunctionDeclarationInFunction.setChild(2, new Node(Token.RIGHT_PAREN, ")"));
    nodeFunctionDeclarationInFunction.setChild(3, new Node(Token.BLOCK, ""));
    Node nodeFunction = new Node(Token.FUNCTION, "myFunctionParent");
    nodeFunction.setChild(0, new Node(Token.IDENT, "myFunctionParent"));
    nodeFunction.setChild(1, new Node(Token.LEFT_PAREN, "("));
    nodeFunction.setChild(2, nodeFunctionDeclarationInFunction);
    nodeFunction.setChild(3, new Node(Token.RIGHT_PAREN, ")"));

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
  }
}