import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node threeChildrenNode = new Node(Token.FOR).setChild(0, new Node(Token.IDENTIFIER, "i"))
        .setChild(1, new Node(Token.KEYWORD, "in"))
        .setChild(2, new Node(Token.IDENTIFIER, "arr"));
    assertTrue(NodeUtil.isForIn(threeChildrenNode));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.PRINT, new Node(Token.STRING_LITERAL, "hello"));
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new Node(Token.IDENTIFIER, "x");
    assertFalse(NodeUtil.isStatement(expressionNode));

    Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "myFunction"));
    assertTrue(NodeUtil.isStatement(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "myFunction"));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "myFunction"))
        .setChild(0, new Node(Token.IDENTIFIER, "x"));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test with a function declaration in the script level context
    Node scriptLevelFunctionDeclarationNode = new Node(Token.SCRIPT, null)
        .setChild(0, new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "myFunction")))
        .setChild(1, new Node(Token.IDENTIFIER, "x"));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptLevelFunctionDeclarationNode.getChild(0)));

    // Test with a function declaration inside a function
    Node functionLevelFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "outerFunction"))
        .setChild(0, new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "myFunction")))
        .setChild(1, new Node(Token.IDENTIFIER, "x"));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionLevelFunctionDeclarationNode.getChild(0)));
  }

  @Test(expected = AssertionError.class)
  public void testCheckParentIsNotNull() {
    Node node = new Node(Token.SCRIPT, null);
    NodeUtil.isStatement(node);
  }
}