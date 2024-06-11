package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {
    private RemoveConstantExpressions.RemoveConstantRValuesCallback callback;

    @Before
    public void setUp() {
        callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
    }

    @Test
    public void testVisit_ExprResult_NodeWithSideEffects() {
        NodeTraversal traversal = null;
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EXPR_RESULT);
        node.addChildToBack(new Node(Token.TRUE));
        parent.addChildToBack(node);

        callback.visit(traversal, node, parent);

        assertEquals(Token.SCRIPT, parent.getType());
        assertEquals(1, parent.getChildCount());
        assertEquals(Token.FALSE, parent.getFirstChild().getType());
    }

    @Test
    public void testVisit_ExprResult_NodeWithoutSideEffects() {
        NodeTraversal traversal = null;
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.EXPR_RESULT);
        node.addChildToBack(new Node(Token.TRUE));
        parent.addChildToBack(node);

        callback.visit(traversal, parent, null);

        assertEquals(Token.SCRIPT, parent.getType());
        assertEquals(1, parent.getChildCount());
        assertEquals(Token.TRUE, parent.getFirstChild().getType());
    }

    @Test
    public void testGetSideEffectNodes_OneSubexpression() {
        Node node = new Node(Token.ADD);
        node.addChildToBack(new Node(Token.TRUE));

        List<Node> result = callback.getSideEffectNodes(node);

        assertEquals(1, result.size());
        assertEquals(Token.EXPR_RESULT, result.get(0).getType());
        assertEquals(Token.TRUE, result.get(0).getFirstChild().getType());
    }

    @Test
    public void testGetSideEffectNodes_MultipleSubexpressions() {
        Node node = new Node(Token.ADD);
        node.addChildToBack(new Node(Token.TRUE));
        node.addChildToBack(new Node(Token.FALSE));

        List<Node> result = callback.getSideEffectNodes(node);

        assertEquals(2, result.size());
        assertEquals(Token.EXPR_RESULT, result.get(0).getType());
        assertEquals(Token.TRUE, result.get(0).getFirstChild().getType());
        assertEquals(Token.EXPR_RESULT, result.get(1).getType());
        assertEquals(Token.FALSE, result.get(1).getFirstChild().getType());
    }

    @Test
    public void testGetSideEffectNodes_NoSubexpressions() {
        Node node = new Node(Token.TRUE);

        List<Node> result = callback.getSideEffectNodes(node);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetResult() {
        RemoveConstantExpressions.Result result = callback.getResult();

        assertFalse(result.changed);
    }
}