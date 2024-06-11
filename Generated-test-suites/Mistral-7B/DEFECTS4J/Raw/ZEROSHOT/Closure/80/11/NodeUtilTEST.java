import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(0, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newExpression(0, getIntLiteral(0)));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newExpression(0, getIntLiteral(0)));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("foo");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        0, null, getFunctionName("myFunction"), null, null, null, null, Node.newBlock(0, null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newExpression(0, getFunctionExpression()));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        0, null, getFunctionName("myFunction"), null, null, null, null, Node.newBlock(0, null));
    Node parentScript = Node.newScript(0, null);
    parentScript.appendChild(parentScript); // Adding a dummy child to simulate the script node
    parentScript.appendChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(
        0, null, getFunctionName("myFunction"), null, null, null, null, Node.newBlock(0, null));
    Node parentFunction = Node.newFunction(
        0, null, getFunctionName("myParentFunction"), null, null, null, null, Node.newBlock(0, null));
    parentFunction.appendChild(parentFunction); // Adding a dummy child to simulate the function node
    parentFunction.appendChild(nodeFunctionDeclarationInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  private Node getFunctionExpression() {
    return Node.newExpression(0, Node.newFunction(0, null, getFunctionName("myFunction"), null, null, null, null, Node.newBlock(0, null)));
  }

  private Node getFunctionName(String name) {
    return Node.newIdentifier(name);
  }

  private Node getIntLiteral(int value) {
    return Node.newIntLiteral(value);
  }
}