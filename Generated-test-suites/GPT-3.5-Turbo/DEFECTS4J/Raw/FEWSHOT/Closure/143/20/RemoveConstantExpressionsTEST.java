package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RemoveConstantExpressionsTest {
    private AbstractCompiler compiler;
    private RemoveConstantExpressions removeConstantExpressions;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        removeConstantExpressions = new RemoveConstantExpressions(compiler);
    }

    @Test
    public void testRemoveConstantRValuesCallback_visit_noSideEffects() {
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.NAME, "x");

        removeConstantExpressions.new RemoveConstantRValuesCallback().visit(null, node, parent);

        assertEquals(node, parent.getFirstChild());
    }

    @Test
    public void testRemoveConstantRValuesCallback_trySimplify_exprBodyHasNoSideEffects() {
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.PLUS,
                new Node(Token.NUMBER, 1),
                new Node(Token.NAME, "x"));

        removeConstantExpressions.new RemoveConstantRValuesCallback().trySimplify(parent, node);

        assertEquals(Token.EXPR_RESULT, parent.getType());
        assertEquals(Token.NAME, parent.getFirstChild().getType());
        assertEquals("x", parent.getFirstChild().getString());
    }

    @Test
    public void testRemoveConstantRValuesCallback_trySimplify_exprBodyHasSideEffects() {
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.PLUS,
                new Node(Token.NUMBER, 1),
                new Node(Token.CALL,
                        new Node(Token.NAME, "foo")));

        removeConstantExpressions.new RemoveConstantRValuesCallback().trySimplify(parent, node);

        assertEquals(Token.EXPR_RESULT, parent.getType());
        assertEquals(Token.CALL, parent.getFirstChild().getType());
        assertEquals(Token.NAME, parent.getFirstChild().getFirstChild().getType());
        assertEquals("foo", parent.getFirstChild().getFirstChild().getString());
    }

    @Test
    public void testRemoveConstantRValuesCallback_trySimplify_parentIsNotExprResult() {
        Node parent = new Node(Token.NAME, "x");
        Node node = new Node(Token.PLUS,
                new Node(Token.NUMBER, 1),
                new Node(Token.NAME, "y"));

        removeConstantExpressions.new RemoveConstantRValuesCallback().trySimplify(parent, node);

        assertEquals(node, parent);
    }

    @Test
    public void testRemoveConstantRValuesCallback_getSideEffectNodes() {
        Node node = new Node(Token.PLUS,
                new Node(Token.NUMBER, 1),
                new Node(Token.CALL,
                        new Node(Token.NAME, "foo")));

        List<Node> sideEffectNodes = removeConstantExpressions.new RemoveConstantRValuesCallback().getSideEffectNodes(node);

        assertEquals(1, sideEffectNodes.size());
        assertEquals(Token.EXPR_RESULT, sideEffectNodes.get(0).getType());
        assertEquals(Token.CALL, sideEffectNodes.get(0).getFirstChild().getType());
        assertEquals(Token.NAME, sideEffectNodes.get(0).getFirstChild().getFirstChild().getType());
        assertEquals("foo", sideEffectNodes.get(0).getFirstChild().getFirstChild().getString());
    }

    @Test
    public void testRemoveConstantRValuesCallback_getResult() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback =
                removeConstantExpressions.new RemoveConstantRValuesCallback();

        RemoveConstantExpressions.Result result = removeConstantRValuesCallback.getResult();

        assertFalse(result.changed);
    }

    @Test
    public void testRemoveConstantExpressions_constructor() {
        RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);

        assertEquals(compiler, removeConstantExpressions.compiler);
    }

    @Test
    public void testRemoveConstantExpressions_process() {
        Node externs = null;
        Node root = new Node(Token.SCRIPT);

        removeConstantExpressions.process(externs, root);
    }

    @Test
    public void testRemoveConstantExpressions_RemoConstantRValuesCallback_visit() {
        NodeTraversal traversal = new NodeTraversal(compiler, null);
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.NAME, "x");

        removeConstantExpressions.new RemoveConstantRValuesCallback().visit(traversal, node, parent);

        assertEquals(node, parent.getFirstChild());
    }

    @Test
    public void testRemoveConstantExpressions_RemoConstantRValuesCallback_getResult() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback =
                removeConstantExpressions.new RemoveConstantRValuesCallback();

        RemoveConstantExpressions.Result result = removeConstantRValuesCallback.getResult();

        assertFalse(result.changed);
    }

    @Test
    public void testReportCodeHasChangedListener_nodeRemoved() {
        Node node = new Node(Token.NAME, "x");
        RemoveConstantExpressions.ReportCodeHasChangedListener reportCodeHasChangedListener =
                removeConstantExpressions.new ReportCodeHasChangedListener(new RemoveConstantExpressions.Result());

        reportCodeHasChangedListener.nodeRemoved(node);

        assertTrue(reportCodeHasChangedListener.result.changed);
    }
}