import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR);
    forInNode.setChild(0, new Node(Token.KEYWORD, "for"));
    forInNode.setChild(1, new Node(Token.IDENT, "var"));
    forInNode.setChild(2, new Node(Token.IDENT, "a"));
    forInNode.setChild(3, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "i"), new Node(Token.IN, null, new Node(Token.IDENT, "a"), new Node(Token.LBRACKET, null, new Node(Token.RBRACKET, null)))));

    assertTrue(NodeUtil.isForIn(forInNode));

    Node invalidForInNode = new Node(Token.FOR);
    invalidForInNode.setChild(0, new Node(Token.KEYWORD, "for"));
    invalidForInNode.setChild(1, new Node(Token.IDENT, "var"));
    invalidForInNode.setChild(2, new Node(Token.IDENT, "a"));

    assertFalse(NodeUtil.isForIn(invalidForInNode));

    // Test with null input
    Node nullNode = null;
    assertFalse(NodeUtil.isForIn(nullNode));

    // Test with different Node types
    Node ifNode = new Node(Token.IF, null, new Node[0], new Node[0]);
    assertFalse(NodeUtil.isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 10));

    assertTrue(NodeUtil.isStatement(statementNode));

    Node nonStatementNode = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "foo"), new Node[0]);

    assertFalse(NodeUtil.isStatement(nonStatementNode));

    // Test with null input
    Node nullNode = null;
    assertFalse(NodeUtil.isStatement(nullNode));
  }

  // Test methods isFunctionDeclaration and isHoistedFunctionDeclaration similarly
}