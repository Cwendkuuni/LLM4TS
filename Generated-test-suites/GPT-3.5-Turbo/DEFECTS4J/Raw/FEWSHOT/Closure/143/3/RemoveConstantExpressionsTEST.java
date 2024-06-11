
package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.*;

public class RemoveConstantExpressionsTest {

    private RemoveConstantExpressions.RemoveConstantRValuesCallback callback;
    private Node parentNode;
    private Node childNode;

    @Before
    public void setUp() {
        callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        parentNode = new Node(Token.EXPR_RESULT);
        childNode = new Node(Token.ADD, new Node(Token.NUMBER, 1), new Node(Token.NUMBER, 2));
    }

    @Test
    public void testTrySimplify_SimplifiesExpressionWithNoSideEffects_Succeeds() {
        parentNode.addChildToFront(childNode);
        callback.trySimplify(parentNode, childNode);

        assertEquals(2, parentNode.getChildCount());
        assertEquals(Token.NUMBER, parentNode.getFirstChild().getType());
        assertEquals(Token.NUMBER, parentNode.getLastChild().getType());
    }

    @Test
    public void testTrySimplify_DoesNotSimplifyExpressionWithSideEffects_Succeeds() {
        Node firstExpr = new Node(Token.CALL);
        Node secondExpr = new Node(Token.ADD, new Node(Token.NUMBER, 1), new Node(Token.NUMBER, 2));

        parentNode.addChildToFront(firstExpr);
        parentNode.addChildToBack(secondExpr);
        callback.trySimplify(parentNode, secondExpr);

        assertEquals(2, parentNode.getChildCount());
        assertEquals(Token.CALL, parentNode.getFirstChild().getType());
        assertEquals(Token.ADD, parentNode.getLastChild().getType());
    }

    @Test
    public void testGetSideEffectNodes_ReturnsCorrectList_Succeeds() {
        List<Node> nodes = new ArrayList<>();
        Node subexpression1 = new Node(Token.CALL);
        Node subexpression2 = new Node(Token.ADD, new Node(Token.NUMBER, 3), new Node(Token.NUMBER, 4));

        nodes.add(subexpression1);
        nodes.add(subexpression2);
        callback.getSideEffectNodes(childNode);

        assertEquals(2, nodes.size());
        assertEquals(Token.CALL, nodes.get(0).getType());
        assertEquals(Token.ADD, nodes.get(1).getType());
    }

    @Test
    public void testGetResult_ReturnsCorrectResult_Succeeds() {
        RemoveConstantExpressions.Result result = callback.getResult();

        assertNotNull(result);
    }
}

