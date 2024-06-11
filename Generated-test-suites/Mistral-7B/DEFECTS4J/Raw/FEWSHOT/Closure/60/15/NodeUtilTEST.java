package com.google.javascript.jscomp.jsonml;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node nodeForIn = createNodeForForIn("for (var i in array) { }");
        assertTrue(NodeUtil.isForIn(nodeForIn));

        Node nodeFor = createNodeFor("for (var i = 0; i < array.length; i++) { }");
        assertFalse(NodeUtil.isForIn(nodeFor));

        Node nodeIf = createNodeFor("if (condition) { }");
        assertFalse(NodeUtil.isForIn(nodeIf));
    }

    @Test
    public void testIsStatement() {
        Node nodeScript = createNodeForScript("function foo() { }");
        assertTrue(NodeUtil.isStatement(nodeScript));

        Node nodeIf = createNodeForIf("if (condition) { }");
        assertTrue(NodeUtil.isStatement(nodeIf));

        Node nodeExpression = createNodeForExpression("2 + 2");
        assertFalse(NodeUtil.isStatement(nodeExpression));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node nodeFunctionDeclaration = createNodeForFunctionDeclaration("function foo() { }");
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

        Node nodeFunctionExpression = createNodeForFunctionExpression("function foo() { }");
        assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node nodeFunctionDeclarationInScript = createNodeForFunctionDeclaration("function foo() { }");
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

        Node nodeFunctionDeclarationInFunction = createNodeForFunctionDeclaration("function bar(x) { function foo() { } }");
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

        Node nodeFunctionDeclarationNotInFunction = createNodeForFunctionDeclaration("function bar(x) { } function foo() { }");
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationNotInFunction));
    }

    private Node createNodeForForIn(String script) {
        return new RhinoCompiler().parse(script, null, null).asList().get(0);
    }

    private Node createNodeForFor(String script) {
        return new RhinoCompiler().parse(script, null, null).asList().get(0);
    }

    private Node createNodeForIf(String script) {
        return new RhinoCompiler().parse(script, null, null).asList().get(0);
    }

    private Node createNodeForExpression(String script) {
        return new RhinoCompiler().parse(script, null, null).asList().get(0);
    }

    private Node createNodeForFunctionDeclaration(String script) {
        return new RhinoCompiler().parse(script, null, null).asList().get(0);
    }

    private Node createNodeForFunctionExpression(String script) {
        return new RhinoCompiler().parse(script, null, null).asList().get(0);
    }

    private Node createNodeForScript(String script) {
        return new RhinoCompiler().parse(script, null, null).asList().get(0);
    }

    private void checkParent(Node node, int expectedType) {
        Node parent = node.getParent();
        checkState(parent != null);
        switch (parent.getType()) {
            case expectedType:
                break;
            default:
                throw new AssertionError("Expected parent with type " + expectedType);
        }
    }
}