import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement(1, new Node[]{}, new Node[]{new Node(Token.IDENT, "i"), new Node(Token.ASSIGN, new Node(Token.PLUS, new Node(Token.NUM, 0), new Node(Token.NUM, 1))), new Node(Token.KEYWORD, "in"), new Node(Token.IDENT, "array")}, null);
    assertTrue(isForIn(forNode));

    Node notForNode = Node.newExpressionStatement(new Node(Token.IDENT, "x"));
    assertFalse(isForIn(notForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(new Node(Token.IDENT, "x"));
    assertTrue(isStatement(statementNode));

    Node notStatementNode = Node.newLiteral(Token.TRUE);
    assertFalse(isStatement(notStatementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunction(
        Node.newIdentifier("myFunction"),
        new Node[]{new Node(Token.IDENT, "arg")},
        Node.newBlock(new Node[]{new Node(Token.RETURN, new Node(Token.NUM, 42))}));
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node notFunctionDeclarationNode = Node.newExpressionStatement(new Node(Token.IDENT, "x"));
    assertFalse(isFunctionDeclaration(notFunctionDeclarationNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript(new Node[]{
      Node.newFunction(
          Node.newIdentifier("myFunction"),
          new Node[]{new Node(Token.IDENT, "arg")},
          Node.newBlock(new Node[]{new Node(Token.RETURN, new Node(Token.NUM, 42))}))
    });
    Node functionDeclarationNode = scriptNode.getFirstChild();
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = Node.newFunction(
        Node.newIdentifier("myFunction"),
        new Node[]{new Node(Token.IDENT, "arg")},
        Node.newBlock(new Node[]{new Node(Token.RETURN, new Node(Token.NUM, 42))}));
    Node notHoistedFunctionDeclarationParentNode = Node.newExpressionStatement(notHoistedFunctionDeclarationNode);
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}