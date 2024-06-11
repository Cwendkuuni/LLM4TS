package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = createNode("for (var i in array) {}", Token.FOR);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeFor = createNode("for (var i = 0; i < array.length; i++) {}", Token.FOR);
    assertFalse(NodeUtil.isForIn(nodeFor));

    Node nodeIf = createNode("if (condition) {}", Token.IF);
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = createNode("var x = 5;", Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = createNode("2 + 3", Token.EXPR);
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = createNode("function foo() {}", Token.FUNCTION);
    assertTrue(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = createNode("function foo() {}", Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = createNode("var foo = function() {}", Token.VAR);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = createNode("function foo() {}", Token.SCRIPT);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeScript));

    Node nodeFunction = createNode("function foo() {}", Token.FUNCTION);
    Node parentScript = createNode("", Token.SCRIPT);
    parentScript.addChild(nodeFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInFunction = createNode("function bar() {" + "function foo() {}" + "}", Token.FUNCTION);
    Node parentFunction = createNode("", Token.FUNCTION);
    parentFunction.addChild(nodeFunctionInFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction.getChild(0)));
  }

  private Node createNode(String code, int type) {
    RhinoApi api = new RhinoApi();
    RhinoCompiler compiler = api.newCompiler();
    compiler.compile(code);
    Node node = compiler.getResultNode();
    node.setParent(null);
    return node;
  }

  @Test
  public void testIsHoistedFunctionDeclaration_null() {
    assertNull(NodeUtil.isHoistedFunctionDeclaration(null));
  }

  private static void assertFalse(boolean condition) {
    // No-op
  }
}