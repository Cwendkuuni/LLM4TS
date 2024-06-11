package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;

import java.io.IOException;

public class RuntimeTypeCheckTest {

  private static final String TEST_JS = "test.js";
  private static final String RUNTIME_TYPE_CHECK_JS = "runtime_type_check.js";

  private AbstractCompiler compiler;
  private Node rootNode;

  @org.junit.Before
  public void setUp() throws IOException {
    compiler = new CompilerOptions().getCompiler();
    rootNode = NodeUtil.parseJavaScript(Resources.read(TEST_JS, Charsets.UTF_8), null);
    compiler.process(null, rootNode);
  }

  @org.junit.Test
  public void testRuntimeTypeCheck() {
    RuntimeTypeCheck checker = new RuntimeTypeCheck(compiler, null);
    checker.process(null, rootNode);
    Node runtimeTypeCheckFunction = checkNode("checkType", rootNode);
    Node expectedResult = expectCheckTypeCall(runtimeTypeCheckFunction, 0, JSType.STRING);
    Node actualResult = runCheckTypeCall(runtimeTypeCheckFunction, 0);
    assertEquals(expectedResult, actualResult);

    expectedResult = expectCheckTypeCall(runtimeTypeCheckFunction, 1, JSType.NUMBER);
    actualResult = runCheckTypeCall(runtimeTypeCheckFunction, 1);
    assertEquals(expectedResult, actualResult);

    expectedResult = expectCheckTypeCall(runtimeTypeCheckFunction, 2, JSType.BOOLEAN);
    actualResult = runCheckTypeCall(runtimeTypeCheckFunction, 2);
    assertEquals(expectedResult, actualResult);

    expectedResult = expectCheckTypeCall(runtimeTypeCheckFunction, 3, JSType.STRING);
    actualResult = runCheckTypeCall(runtimeTypeCheckFunction, 3);
    assertEquals(expectedResult, actualResult);

    expectedResult = expectCheckTypeCall(runtimeTypeCheckFunction, 4, JSType.NULL);
    actualResult = runCheckTypeCall(runtimeTypeCheckFunction, 4);
    assertEquals(expectedResult, actualResult);

    expectedResult = expectCheckTypeCall(runtimeTypeCheckFunction, 5, JSType.FUNCTION);
    actualResult = runCheckTypeCall(runtimeTypeCheckFunction, 5);
    assertEquals(expectedResult, actualResult);

    expectedResult = expectCheckTypeCall(runtimeTypeCheckFunction, 6, JSType.OBJECT);
    actualResult = runCheckTypeCall(runtimeTypeCheckFunction, 6);
    assertEquals(expectedResult, actualResult);

    expectedResult = expectCheckTypeCall(runtimeTypeCheckFunction, 7, JSType.UNIONTYPE, JSType.STRING, JSType.NUMBER);
    actualResult = runCheckTypeCall(runtimeTypeCheckFunction, 7);
    assertEquals(expectedResult, actualResult);

    expectedResult = expectCheckTypeCall(runtimeTypeCheckFunction, 8, JSType.UNIONTYPE, JSType.BOOLEAN, JSType.NUMBER);
    actualResult = runCheckTypeCall(runtimeTypeCheckFunction, 8);
    assertEquals(expectedResult, actualResult);

    expectedResult = expectCheckTypeCall(runtimeTypeCheckFunction, 9, JSType.UNIONTYPE, JSType.STRING, JSType.NUMBER, JSType.BOOLEAN);
    actualResult = runCheckTypeCall(runtimeTypeCheckFunction, 9);
    assertEquals(expectedResult, actualResult);
  }

  private Node checkNode(String name, Node node) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.getType() == Token.FUNCTION) {
        FunctionType functionType = (FunctionType) child.getJSType();
        for (Node functionChild : functionType.getParameters()) {
          if (NodeUtil.getFunctionName(functionChild).equals(name)) {
            return child;
          }
        }
      }
    }
    return null;
  }

  private Node expectCheckTypeCall(Node checkTypeFunction, int index, JSType expectedType) {
    Node checkTypeCall = checkTypeFunction.getChild(index);
    Node checkerNode = checkTypeCall.getLastChild();
    Node arrayNode = (Node) checkerNode.getFirstChild();
    ImmutableList<Node> checkers = NodeUtil.getChildrenAsList(arrayNode);
    Node expectedChecker = checkers.get(0);
    JSType actualType = NodeUtil.getTypeOfExpression(expectedChecker);
    assertNotNull(actualType);
    assertEquals(expectedType, actualType);
    return checkTypeCall;
  }

  private Node expectCheckTypeCall(Node checkTypeFunction, int index, JSType expectedType1, JSType... expectedTypes) {
    Node checkTypeCall = checkTypeFunction.getChild(index);
    Node checkerNode = checkTypeCall.getLastChild();
    Node arrayNode = (Node) checkerNode.getFirstChild();
    ImmutableList<Node> checkers = NodeUtil.getChildrenAsList(arrayNode);
    Node expectedChecker = checkers.get(0);
    JSType actualType = NodeUtil.getTypeOfExpression(expectedChecker);
    assertNotNull(actualType);
    assertEquals(expectedType1, actualType);
    for (int i = 1; i < checkers.size(); i++) {
      Node checker = checkers.get(i);
      JSType checkerType = NodeUtil.getTypeOfExpression(checker);
      assertEquals(expectedTypes[i], checkerType);
    }
    return checkTypeCall;
  }

  private Node expectCheckTypeCall(Node checkTypeFunction, int index, UnionType expectedType) {
    Node checkTypeCall = checkTypeFunction.getChild(index);
    Node checkerNode = checkTypeCall.getLastChild();
    Node arrayNode = (Node) checkerNode.getFirstChild();
    ImmutableList<Node> checkers = NodeUtil.getChildrenAsList(arrayNode);
    int size = checkers.size();
    assertEquals(expectedType.getAlternates().size(), size);
    for (int i = 0; i < size; i++) {
      Node checker = checkers.get(i);
      JSType checkerType = NodeUtil.getTypeOfExpression(checker);
      assertEquals(expectedType.getAlternates().get(i), checkerType);
    }
    return checkTypeCall;
  }

  private Node runCheckTypeCall(Node checkTypeFunction, int index) {
    Node checkTypeCall = checkTypeFunction.getChild(index);
    Node resultNode = checkTypeCall.getFirstChild();
    Node result = resultNode.getFirstChild();
    return result;
  }
}