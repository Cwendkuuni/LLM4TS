import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node threeChildNode = new Node(Token.FOR);
    threeChildNode.setChildCount(3);
    Node child1 = new Node(Token.IDENTIFIER, "forVar");
    Node child2 = new Node(Token.ASSIGN, null, new Node[] { child1, new Node(Token.NUMBER, 0) });
    Node child3 = new Node(Token.SEMICOLON);
    threeChildNode.setChild(0, child1);
    threeChildNode.setChild(1, child2);
    threeChildNode.setChild(2, child3);

    assertTrue(NodeUtil.isForIn(n));
    assertTrue(NodeUtil.isForIn(threeChildNode));

    Node invalidForIn = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(invalidForIn));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IDENTIFIER, "statement");
    Node parentScriptNode = new Node(Token.SCRIPT);
    parentScriptNode.setChild(0, statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(parentScriptNode));

    Node nonStatementNode = new Node(Token.NUMBER, 10);
    assertFalse(NodeUtil.isStatement(nonStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, "functionName");
    Node parentBlockNode = new Node(Token.BLOCK);
    parentBlockNode.setChild(0, functionDeclarationNode);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(parentBlockNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionDeclarationNode = new Node(Token.FUNCTION, "functionName");
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, "functionName");
    hoistedFunctionDeclarationNode.setParent(scriptNode);
    functionDeclarationNode.setParent(new Node(Token.BLOCK));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }
}