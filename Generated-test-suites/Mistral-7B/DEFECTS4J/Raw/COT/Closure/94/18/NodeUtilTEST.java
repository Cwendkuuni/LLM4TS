import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[0], new Node[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[0], new Node[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[0], new Node[0]);
    Node parentScript = Node.newNode(Token.SCRIPT, new int[0], new Node[0]);
    Node statementInsideExpression = Node.newNode(Token.IF, new int[0], new Node[0]);
    Node expressionParentBlock = Node.newNode(Token.BLOCK, new int[0], new Node[0]);

    parentScript.addChild(statementStatement);
    expressionParentBlock.addChild(statementInsideExpression);

    NodeUtil.isStatement(nodeStatement);
    NodeUtil.isStatement(nodeStatement.getParent());
    assertFalse(NodeUtil.isStatement(statementInsideExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[0], new Node[1]);
    Node functionExpression = Node.newNode(Token.FUNCTION, new int[0], new Node[0]);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateException() {
    Node node = Node.newNode(Token.SCRIPT, new int[0], new Node[0]);
    NodeUtil.isStatement(node);
    Preconditions.checkState(false);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[0], new Node[1]);
    Node functionDeclarationInsideFunction = Node.newNode(Token.FUNCTION, new int[0], new Node[1]);
    Node functionInsideBlock = Node.newNode(Token.FUNCTION, new int[0], new Node[0]);
    Node block = Node.newNode(Token.BLOCK, new int[0], new Node[0]);

    hoistedFunctionDeclaration.getParent().setType(Token.SCRIPT);
    functionDeclarationInsideFunction.getParent().setType(Token.FUNCTION);
    functionInsideBlock.getParent().setType(Token.BLOCK);

    block.addChild(functionInsideBlock);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInsideFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInsideBlock));
  }

  @Test
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[0], new Node[1]);

    NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);
    assertNull(functionDeclaration.getParent());
  }
}