package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

    private RemoveConstantExpressions removeConstantExpressions;

    @Before
    public void setUp() {
        removeConstantExpressions = new RemoveConstantExpressions();
    }

    @Test
    public void testProcess() {
        Node externs = null;
        Node root = null;
        removeConstantExpressions.process(externs, root);

        // assert something
    }

    @Test
    public void testRemoveConstantRValuesCallback() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = removeConstantExpressions.new RemoveConstantRValuesCallback();
        NodeTraversal traversal = null;
        Node node = null;
        Node parent = null;
        callback.visit(traversal, node, parent);

        // assert something
    }

    @Test
    public void testTrySimplify() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = removeConstantExpressions.new RemoveConstantRValuesCallback();
        Node parent = null;
        Node node = null;
        callback.trySimplify(parent, node);

        // assert something
    }

    @Test
    public void testGetSideEffectNodes() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = removeConstantExpressions.new RemoveConstantRValuesCallback();
        Node node = null;
        List<Node> sideEffectNodes = callback.getSideEffectNodes(node);

        // assert something
    }

    @Test
    public void testGetResult() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = removeConstantExpressions.new RemoveConstantRValuesCallback();
        Result result = callback.getResult();

        // assert something
    }

    @Test
    public void testReportCodeHasChangedListener() {
        RemoveConstantExpressions.ReportCodeHasChangedListener listener = removeConstantExpressions.new ReportCodeHasChangedListener();
        Node node = null;
        listener.nodeRemoved(node);

        // assert something
    }
}