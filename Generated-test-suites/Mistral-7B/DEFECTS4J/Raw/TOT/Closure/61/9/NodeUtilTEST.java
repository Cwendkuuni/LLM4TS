import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[]{}, new int[]{0, 1, 2});
    Node notForNode = new Node(Token.IF, new int[]{}, new int[]{0, 1, 2});

    assertTrue(isForIn(forNode));
    assertFalse(isForIn(notForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[]{}, new int[]{0, 1, 2});
    Node notStatementNode = new Node(Token.PRIM_VOID, new int[]{}, new int[]{0});

    assertTrue(isStatement(statementNode));
    assertFalse(isStatement(notStatementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(
        Token.FUNCTION,
        new int[]{},
        new int[]{0, 1, 2, 3, 4},
        "myFunction"
    );

    Node functionExpression = new Node(
        Token.FUNCTION,
        new int[]{},
        new int[]{0, 1, 2, 3, 4},
        null
    );

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(
        Token.FUNCTION,
        new int[]{},
        new int[]{0, 1, 2, 3, 4},
        "myFunction"
    );

    Node notHoistedFunctionDeclaration = new Node(
        Token.FUNCTION,
        new int[]{},
        new int[]{0, 1, 2, 3, 4},
        "myFunction"
    );
    notHoistedFunctionDeclaration.setParent(new Node(Token.BLOCK, new int[]{}, new int[0]));

    Node hoistedFunctionDeclarationInFunction = new Node(
        Token.FUNCTION,
        new int[]{},
        new int[]{0, 1, 2, 3, 4},
        "myFunction"
    );
    hoistedFunctionDeclarationInFunction.setParent(new Node(Token.FUNCTION, new int[]{}, new int[0], null));

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunction));
  }
}