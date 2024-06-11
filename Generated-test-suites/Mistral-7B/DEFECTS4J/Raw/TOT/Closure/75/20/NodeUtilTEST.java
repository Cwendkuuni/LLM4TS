import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.ident("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.ident("x"));
    Node nodeNotStatement = Node.newExpression("x + y");
    Node nodeFunction = Node.newFunction(null, "f", new Node[0], Node.BLOCK);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
    assertFalse(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Node.ident("sum"), new Node[0], Node.BLOCK);
    Node nodeFunctionExpression = Node.newExpressionFunction("sum", new Node[0], Node.BLOCK);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript();
    Node functionDeclaration = Node.newFunction(Node.ident("sum"), new Node[0], Node.BLOCK);
    Node functionExpression = Node.newExpressionFunction("sum", new Node[0], Node.BLOCK);

    scriptNode.addChild(functionDeclaration);
    scriptNode.addChild(Node.newExpressionStatement(Node.ident("x")));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpression));

    Node functionDeclarationInFunction = Node.newFunction(
        Node.ident("f"),
        new Node[0],
        Node.newBlock(Node.newFunction(Node.ident("sum"), new Node[0], Node.BLOCK))
    );

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }
}