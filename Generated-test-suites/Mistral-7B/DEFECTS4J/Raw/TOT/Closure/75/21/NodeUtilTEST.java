import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(isForIn(nodeForIn));

    Node nodeOther = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(isForIn(nodeOther));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newExpressionStatement(Node.newBinaryOp(
        Node.newIdentifier("x"), Node.OP_PLUS, Node.newNumber(1)));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newIdentifier("x"))));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunction(Node.newIdentifier("myFunction"), Node.newArray(), null));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript();
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newIdentifier("x"))));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(
        Node.newIdentifier("myFunctionInFunction"),
        Node.newArray(),
        Node.newBlock(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunction(Node.newIdentifier("myFunctionExpression"), Node.newArray(), null));

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpression));

    nodeScript.addChild(nodeFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    nodeScript.addChild(nodeFunctionDeclarationInFunction);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}