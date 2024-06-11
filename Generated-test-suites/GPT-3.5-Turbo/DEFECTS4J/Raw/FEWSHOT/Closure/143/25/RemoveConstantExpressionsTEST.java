package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.RemoveConstantExpressions.RemoveConstantRValuesCallback;

public class RemoveConstantExpressionsTest {

    @Test
    public void testProcess() {
        RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(new AbstractCompiler());
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);

        removeConstantExpressions.process(externs, root);
        // TODO: Implement test case
    }

    @Test
    public void testRemoveConstantRValuesCallback() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.ROOT);
        removeConstantRValuesCallback.visit(nodeTraversal, node, parent);
        // TODO: Implement test case
    }

    @Test
    public void testTrySimplify() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.ROOT);
        removeConstantRValuesCallback.trySimplify(parent, node);
        // TODO: Implement test case
    }

    @Test
    public void testGetSideEffectNodes() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

        Node node = new Node(Token.VAR);
        List<Node> result = removeConstantRValuesCallback.getSideEffectNodes(node);

        assertNotNull(result);
        // TODO: Implement more test cases
    }

    @Test
    public void testGetResult() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        RemoveConstantExpressions.Result result = removeConstantRValuesCallback.getResult();

        assertNotNull(result);
        // TODO: Implement more test cases
    }

    @Test
    public void testReportCodeHasChangedListener() {
        RemoveConstantExpressions.ReportCodeHasChangedListener reportCodeHasChangedListener = new RemoveConstantExpressions.ReportCodeHasChangedListener(new RemoveConstantExpressions.Result());
        Node node = new Node(Token.VAR);

        reportCodeHasChangedListener.nodeRemoved(node);
        // TODO: Implement test case
    }

}