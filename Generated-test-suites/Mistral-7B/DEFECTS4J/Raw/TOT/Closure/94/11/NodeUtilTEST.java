import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression(Node.newIdentifier("x"));
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock(Node.EMPTY_NODE_LIST));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock(Node.NEW_ARRAY(Node.EMPTY_NODE_LIST))));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newList(), Node.NEW_BLOCK(Node.EMPTY_NODE_LIST));
    Node parentScript = Node.newScript();
    parentScript.addChild(nodeHoistedFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInBlock = Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newList(), Node.NEW_BLOCK(Node.EMPTY_NODE_LIST));
    Node parentBlock = Node.newBlock(Node.EMPTY_NODE_LIST);
    parentBlock.addChild(nodeFunctionDeclarationInBlock);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
  }
}