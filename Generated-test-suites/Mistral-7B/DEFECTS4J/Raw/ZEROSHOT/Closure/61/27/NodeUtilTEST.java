import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn("for (var i in arr) {}");
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeFor = Node.newFor("for (var i = 0; i < 10; i++) {}");
    assertFalse(NodeUtil.isForIn(nodeFor));

    Node nodeIf = Node.newIfStatement();
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newStatement(1, Token.IDENT, "stmt");
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpressionStatement(Token.ASSIGN, Node.newNumberLiteral(1));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        "MyFunction",
        new Node[0],
        Node.SCOPE_GLOBAL,
        Node.newBlock(new Node[0]));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Token.ASSIGN,
        Node.newFunction(Token.VOID, new Node[0], Node.functionScope(), Node.newBlock(new Node[0])));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression.getFirstChild()));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(
        "function MyFunction() {}",
        new Node[0], Node.SCOPE_GLOBAL);
    Node functionDeclaration = nodeScript.getFirstChild();
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));

    Node nodeFunction = Node.newFunction(
        "MyFunction",
        new Node[0],
        Node.SCOPE_GLOBAL,
        Node.newBlock(new Node[0]));
    nodeFunction.setParent(Node.newScript(new Node[0], Node.SCOPE_GLOBAL));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
  }
}