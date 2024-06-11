import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeScript = Node.newText("function test() {}");
    assertTrue(isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(isStatement(nodeExpression));

    Node nodeNotStatement = Node.newName("x");
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Node.newIdentifier("test"), new Node[0], Node.newBlock(new Node[0]));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(Node.newIdentifier("test"), new Node[0], Node.newBlock(new Node[0])));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));

    Node nodeNotFunction = Node.newExpressionStatement(Node.newName("test"));
    assertFalse(isFunctionDeclaration(nodeNotFunction));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newText("function test() {}");
    assertFalse(isHoistedFunctionDeclaration(nodeScript));

    Node nodeFunctionInScript = Node.newFunction(Node.newIdentifier("test"), new Node[0], Node.newBlock(new Node[0]));
    Node parentScript = Node.newText("test()"); // Parent script
    Preconditions.checkState(parentScript != null);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionInScript));

    Node nodeFunctionInFunction = Node.newFunction(Node.newIdentifier("test"), new Node[0], Node.newBlock(new Node[0]));
    Node parentFunction = Node.newFunction(Node.newIdentifier("outer"), new Node[0], Node.newBlock(new Node[0]));
    Preconditions.checkState(parentFunction != null);
    Node parentFunctionParent = Node.newText("outer()"); // Parent script
    Preconditions.checkState(parentFunctionParent != null);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }
}