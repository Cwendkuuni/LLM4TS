import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR);
    forNode.setChild(0, Node.newNode(Token.IDENT, "a"));
    forNode.setChild(1, Node.newNode(Token.KEYWORD, "in"));
    forNode.setChild(2, Node.newNode(Token.IDENT, "array"));

    assertTrue(NodeUtil.isForIn(forNode));

    Node invalidForNode = Node.newNode(Token.FOR);
    assertFalse(NodeUtil.isForIn(invalidForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.RETURN, Node.newNode(Token.IDENT, "x"));
    assertTrue(NodeUtil.isStatement(statementNode));

    Node nonStatementNode = Node.newNode(Token.IDENT, "x");
    assertFalse(NodeUtil.isStatement(nonStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENT, "myFunction"), Node.newNode(Token.BLOCK, null));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENT, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENT, "x"))));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, null);
    Node functionDeclaration = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENT, "myFunction"), Node.newNode(Token.BLOCK, null));
    scriptNode.setChild(0, functionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));

    Node functionInFunction = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENT, "outerFunction"), Node.newNode(Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENT, "myFunction"), Node.newNode(Token.BLOCK, null)), null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInFunction.getChild(0)));
  }
}