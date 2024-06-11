import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(isForIn(nodeForIn));

    Node nodeOther = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(isForIn(nodeOther));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(isStatement(nodeStatement));

    Node nodeOther = Node.newIdentifier("x");
    assertFalse(isStatement(nodeOther));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("func", new Node[]{}, Node.newBlock(Node.newExpressionStatement(Node.newIdentifier("x").assign(Node.newNumberLiteral(1)))));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunctionExpression(Node.newIdentifier("func"), new Node[]{Node.newArrayLiteral()}, Node.newBlock(Node.newExpressionStatement(Node.newIdentifier("x").assign(Node.newNumberLiteral(1))))));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript();
    Node nodeHoistedFunctionDeclaration = Node.newFunctionDeclaration("func", new Node[]{}, Node.newBlock(Node.newExpressionStatement(Node.newIdentifier("x").assign(Node.newNumberLiteral(1)))));
    Node nodeOtherFunctionDeclaration = Node.newFunctionDeclaration("func2", new Node[]{}, Node.newBlock(Node.newExpressionStatement(Node.newIdentifier("x").assign(Node.newNumberLiteral(2)))));
    nodeScript.setFirstChild(nodeHoistedFunctionDeclaration);
    nodeScript.setLastChild(nodeOtherFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeOtherFunctionDeclaration));
  }
}