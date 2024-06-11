import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3}, new Node[]{});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1}, new Node[]{});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.BLOCK, new int[] {1}, new Node[]{});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.ADD, new int[] {1, 2}, new Node[]{Node.newNumberLiteral(1), Node.newNumberLiteral(2)});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2}, new Node[]{Node.newIdentifier("myFunction"), Node.newBlock(new int[] {1}, new Node[]{Node.newNumberLiteral(1)})});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 2}, new Node[]{Node.newIdentifier("myFunction"), Node.newBlock(new int[] {1}, new Node[]{Node.newNumberLiteral(1)})});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test(expected = AssertionError.class)
  public void testCheckParentNull() {
    Node node = Node.newNode(Token.BLOCK, new int[] {1}, new Node[]{});
    NodeUtil.isStatement(null);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRootScope = Node.newNode(Token.FUNCTION, new int[] {1, 2}, new Node[]{Node.newIdentifier("myFunction"), Node.newBlock(new int[] {1}, new Node[]{Node.newNumberLiteral(1)})});
    Node nodeParentScript = Node.newNode(Token.SCRIPT, new int[] {1}, new Node[]{nodeFunctionDeclarationInRootScope});
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInRootScope));

    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2}, new Node[]{Node.newIdentifier("myFunction"), Node.newBlock(new int[] {1}, new Node[]{Node.newNumberLiteral(1)})});
    Node nodeParentFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2}, new Node[]{Node.newIdentifier("outerFunction"), Node.newBlock(new int[] {1}, new Node[]{nodeFunctionDeclarationInFunction})});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}