import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newExpression("x + y");
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock(Node.newList()));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression(Node.newIdentifier("myFunction"),
        Node.newFunction(Node.newIdentifier("x"), Node.newList(), Node.newBlock(Node.newList())));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock(Node.newList()));
    Node nodeScript = Node.newScript(Node.newList(nodeHoistedFunctionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionInsideFunction = Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newList(), Node.newBlock(Node.newList()));
    Node nodeFunction = Node.newFunction(
        Node.newIdentifier("parentFunction"), Node.newList(), Node.newBlock(Node.newList(nodeFunctionInsideFunction)));
    Node nodeScript2 = Node.newScript(Node.newList(nodeFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
  }
}