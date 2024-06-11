import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement("for (var i in arr) { }", 0);
    assertTrue(isForIn(nodeForIn));

    Node nodeFor = Node.newForStatement("for (var i = 0; i < 10; i++) { }", 0);
    assertFalse(isForIn(nodeFor));

    Node nodeWhile = Node.newWhileStatement("while (i < 10) { }", 0);
    assertFalse(isForIn(nodeWhile));

    Node nodeIf = Node.newStatement("if (i < 10) { }", 0);
    assertFalse(isForIn(nodeIf));

    Node nodeVariable = Node.newVariable("i", 0);
    assertFalse(isForIn(nodeVariable));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript("function foo() { }", 0);
    assertTrue(isStatement(nodeScript));

    Node nodeBlock = Node.newBlock("{ function foo() { } }", 0);
    assertTrue(isStatement(nodeBlock));

    Node nodeLabel = Node.newLabel("loop: { }", 0);
    assertTrue(isStatement(nodeLabel));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunctionExpression(
            Node.newIdentifier("foo"),
            Node.newArray(),
            Node.newBlock(
                "{ }",
                0
            ),
            0,
            0,
            null
        ),
        0
    );
    assertFalse(isStatement(nodeFunctionExpression));

    Node nodeIf = Node.newIfStatement(
        Node.newExpression(Node.newBinaryOperator(
            Node.newIdentifier("i"),
            Token.LT,
            Node.newNumericLiteral(10),
            0
        )),
        Node.newBlock("{ }", 0),
        Node.newBlock("{ }", 0),
        0
    );
    assertTrue(isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration(
        Node.newIdentifier("foo"),
        Node.newArray(),
        Node.newBlock("{ }", 0),
        0,
        0,
        0,
        null,
        0
    );
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newFunctionExpression(
        Node.newIdentifier("foo"),
        Node.newArray(),
        Node.newBlock("{ }", 0),
        0,
        0,
        0,
        null,
        0
    );
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(
        "function foo() { } function bar() { }",
        0
    );
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration(1)));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration(2)));

    Node nodeFunction = Node.newFunction(
        Node.newIdentifier("foo"),
        Node.newArray(),
        Node.newBlock("{ }", 0),
        0,
        0,
        null,
        0
    );
    Node nodeScriptWithFunction = Node.newScript(
        "function foo() { } " + nodeFunction.toSource(),
        0
    );
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(isHoistedFunctionDeclaration(nodeFunction(0, nodeScriptWithFunction)));
  }
}