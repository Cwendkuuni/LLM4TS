import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newName("x");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("foo", new Node[0], Node.newBlock(Node.newName("x").set("value", Node.newNumber(42))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunctionExpression(Node.newName("foo"), new Node[0], Node.newBlock(Node.newName("x").set("value", Node.newNumber(42)))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRoot = Node.newScript(
        Node.newFunctionDeclaration("foo", new Node[0], Node.newBlock(Node.newName("x").set("value", Node.newNumber(42)))))
        .setSource("(function() { foo(); })();");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot.getFirstChild()));

    Node nodeFunctionDeclarationInFunction = Node.newFunctionDeclaration("foo", new Node[0], Node.newBlock(Node.newName("x").set("value", Node.newNumber(42))));
    Node nodeFunction = Node.newFunctionExpression(Node.newName("foo"), new Node[0], Node.newBlock(Node.newName("x").set("value", Node.newNumber(42))));
    Node nodeFunctionInFunction = Node.newExpressionStatement(nodeFunction);
    Node nodeScript = Node.newScript(Node.newList(nodeFunctionInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction.getFirstChild()));
  }
}