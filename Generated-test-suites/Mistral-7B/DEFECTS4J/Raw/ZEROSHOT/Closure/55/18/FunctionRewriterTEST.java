import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FunctionRewriterTest {

  private static Node createNode(int type, int lineNumber, int charPositionInLine) {
    Node node = new Node(type, lineNumber, charPositionInLine);
    NodeFactory factory = new NodeFactory();
    return factory.makeTree(Arrays.asList(node));
  }

  private static Node makeFunctionNode(String name, Node body, int lineNumber, int charPositionInLine) {
    Node functionNode = new Node(Token.FUNCTION, lineNumber, charPositionInLine);
    Node expressionStatementNode = new Node(Token.EXPRESSION_STATEMENT, lineNumber, charPositionInLine);
    expressionStatementNode.addChildToBack(functionNode);
    Node functionExpressionNode = new Node(Token.FUNCTION_EXPRESSION, lineNumber, charPositionInLine);
    functionExpressionNode.addChildToBack(expressionStatementNode);
    if (body != null) {
      functionExpressionNode.addChildToBack(body);
    }
    return functionExpressionNode;
  }

  private static void assertReducedTo(Node node, Node expected) {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = createNode(Token.PROGRAM, 1, 1);
    root.addChildToBack(node);
    functionRewriter.process(null, root);
    assertEquals(expected, root.getFirstChild());
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    Node emptyFunction = makeFunctionNode("emptyFunction", null, 1, 1);
    assertReducedTo(emptyFunction, createNode(Token.CALL, 1, 1));
  }

  @org.junit.Test
  public void testIdentityFunctionReducer() {
    Node identityFunction = makeFunctionNode("identityFunction", null, 1, 1);
    assertReducedTo(identityFunction, createNode(Token.CALL, 1, 1, "identityFn_value"));
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    Node constantFunction = makeFunctionNode("constantFunction", createNode(Token.NUMBER, 1, 1), 1, 1);
    assertReducedTo(constantFunction, createNode(Token.CALL, 1, 1, "returnconst_value"));
  }

  @org.junit.Test
  public void testSetterReducer() {
    Node setterFunction = makeFunctionNode("setterFunction", null, 1, 1);
    Node setterPropertyName = createNode(Token.IDENTIFIER, 1, 1, "propertyName");
    Node setterFunctionWithPropertyName = makeFunctionNode("setterFunctionWithPropertyName", setterFunction, 1, 1);
    Node setterFunctionCall = createNode(Token.CALL, 1, 1, "setterFunctionWithPropertyName");
    Node setterFunctionCallWithPropertyName = createNode(Token.CALL, 1, 1, "setterFunctionWithPropertyName", setterPropertyName);

    assertReducedTo(setterFunction, setterFunctionCall);
    assertReducedTo(setterFunctionCall, setterFunctionCallWithPropertyName);
  }

  @org.junit.Test
  public void testGetterReducer() {
    Node getterFunction = makeFunctionNode("getterFunction", null, 1, 1);
    Node getterPropertyName = createNode(Token.IDENTIFIER, 1, 1, "propertyName");
    Node getterFunctionWithPropertyName = makeFunctionNode("getterFunctionWithPropertyName", getterFunction, 1, 1);
    Node getterFunctionCall = createNode(Token.CALL, 1, 1, "getterFunctionWithPropertyName");
    Node getterFunctionCallWithPropertyName = createNode(Token.CALL, 1, 1, "getterFunctionWithPropertyName", getterPropertyName);

    assertReducedTo(getterFunction, getterFunctionCall);
    assertReducedTo(getterFunctionCall, getterFunctionCallWithPropertyName);
  }

  @org.junit.Test
  public void testComplexFunctionReducer() {
    Node complexFunction = makeFunctionNode("complexFunction", null, 1, 1);
    assertReducedTo(complexFunction, complexFunction);
  }

  @org.junit.Test (expected = AssertionError.class)
  public void testInvalidFunctionReducer() {
    Node invalidFunction = makeFunctionNode("invalidFunction", null, 1, 1);
    assertReducedTo(invalidFunction, invalidFunction);
  }

  @org.junit.Test
  public void testNonFunctionNode() {
    Node nonFunctionNode = createNode(Token.NUMBER, 1, 1);
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = createNode(Token.PROGRAM, 1, 1);
    root.addChildToBack(nonFunctionNode);
    functionRewriter.process(null, root);
    assertEquals(nonFunctionNode, root.getFirstChild());
  }

  @org.junit.Before
  public void setup() {
    Parser parser = mock(Parser.class);
    FunctionRewriter functionRewriter = new FunctionRewriter(parser);
  }
}