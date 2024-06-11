import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  Node createNode(int type, int lineNumber, int lineOffset, Node... children) {
    Node node = Context.getCurrentContext().newNode();
    node.setType(type);
    node.setLineNumber(lineNumber);
    node.setLineOffset(lineOffset);
    if (children != null) {
      for (Node child : children) {
        node.addChild(child);
      }
    }
    return node;
  }

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, 1, 1);
    Node init = createNode(Token.VAR, 1, 2, createNode(Token.IDENT, "i"), null);
    Node condition = createNode(Token.IDENT, "i", null);
    Node increment = createNode(Token.IDENT, "i", null);
    forNode.addChild(init);
    forNode.addChild(condition);
    forNode.addChild(increment);
    forNode.addChild(createNode(Token.BLOCK, 1, 1, createNode(Token.STATMENT, null), null));

    assertTrue(isForIn(forNode));

    Node invalidForNode = createNode(Token.FOR, 1, 1);
    assertFalse(isForIn(invalidForNode));

    Node ifStatement = createNode(Token.IF, 1, 1);
    assertFalse(isForIn(ifStatement));
  }

  @Test
  public void testIsStatement() {
    Node statement = createNode(Token.PRINT, 1, 1, createNode(Token.STRING_LITERAL, "Hello World"), null);

    assertTrue(isStatement(statement));

    Node expression = createNode(Token.IDENT, "x", null);

    assertFalse(isStatement(expression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createNode(Token.FUNCTION, 1, 1, createNode(Token.IDENT, "myFunction"), null);
    functionDeclaration.addChild(createNode(Token.BLOCK, 1, 1, createNode(Token.RETURN_STATEMENT, null), null));

    assertTrue(isFunctionDeclaration(functionDeclaration));

    Node functionExpression = createNode(Token.FUNCTION, 1, 1, null, createNode(Token.BLOCK, 1, 1, createNode(Token.RETURN_STATEMENT, null), null));

    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode(Token.SCRIPT, 1, 1);
    Node functionDeclaration = createNode(Token.FUNCTION, 1, 1, createNode(Token.IDENT, "myFunction"), null);
    functionDeclaration.addChild(createNode(Token.BLOCK, 1, 1, createNode(Token.RETURN_STATEMENT, null), null));
    scriptNode.addChild(functionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(functionDeclaration));

    Node functionDeclarationInBlock = createNode(Token.FUNCTION, 2, 1, createNode(Token.IDENT, "myFunction"), null);
    Node blockNode = createNode(Token.BLOCK, 1, 1, null, functionDeclarationInBlock);
    scriptNode.addChild(blockNode);

    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInBlock));
  }
}