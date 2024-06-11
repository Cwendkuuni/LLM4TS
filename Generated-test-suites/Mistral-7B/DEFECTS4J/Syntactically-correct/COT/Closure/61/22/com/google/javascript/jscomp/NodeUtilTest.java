import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node threeChildNode = new Node(Token.FOR, new Node[] {new Node(Token.IDENTIFIER, "i"), new Node(Token.KEYWORD, "in"), new Node(Token.IDENTIFIER, "arr")});
    assertTrue(NodeUtil.isForIn(threeChildNode));
    assertFalse(NodeUtil.isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.ASSIGN, new Node[] {new Node(Token.IDENTIFIER, "x"), new Node(Token.NUMBER, 5)});
    Node expressionNode = new Node(Token.ASSIGN, new Node[] {new Node(Token.IDENTIFIER, "x"), new Node(Token.NUMBER, 5)});
    expressionNode.setParent(node);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(node));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENTIFIER, "myFunction"), new Node(Token.BLOCK, new Node[0])});
    Node functionExpressionNode = new Node(Token.ASSIGN, new Node[] {new Node(Token.IDENTIFIER, "x"), new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENTIFIER, "arg"), new Node(Token.BLOCK, new Node[0])})});

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new Node[] {
        new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENTIFIER, "outerFunction"), new Node(Token.BLOCK, new Node[] {
          new Node(Token.FUNCTION, new Node[] {new Node(Token.IDENTIFIER, "innerFunction"), new Node(Token.BLOCK, new Node[0])}),
          new Node(Token.RETURN, new Node(Token.NUMBER, 42))
        })}),
        new Node(Token.RETURN, new Node(Token.NUMBER, 43))
    });

    Node innerFunctionDeclarationNode = scriptNode.getChild(0).getChild(0);
    Node outerFunctionDeclarationNode = scriptNode.getChild(0);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(innerFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(outerFunctionDeclarationNode));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}