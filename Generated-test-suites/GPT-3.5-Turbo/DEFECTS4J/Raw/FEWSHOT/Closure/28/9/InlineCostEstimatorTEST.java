package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {
    @Test
    public void testGetCost() {
        Node root = new Node(Node.SCRIPT, new Node(Node.EMPTY));
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);

        Node root1 = new Node(Node.SCRIPT, new Node(Node.FUNCTION, new Node(Node.BLOCK, new Node(Node.EMPTY))));
        int cost1 = InlineCostEstimator.getCost(root1);
        assertEquals(0, cost1);

        Node root2 = new Node(Node.SCRIPT, new Node(Node.EMPTY), new Node(Node.VAR));
        int cost2 = InlineCostEstimator.getCost(root2);
        assertEquals(3, cost2);

        Node root3 = new Node(Node.SCRIPT, new Node(Node.EMPTY), new Node(Node.EXPR_RESULT));
        int cost3 = InlineCostEstimator.getCost(root3);
        assertEquals(3, cost3);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node(Node.SCRIPT, new Node(Node.EMPTY));
        int cost = InlineCostEstimator.getCost(root, 10);
        assertEquals(0, cost);

        Node root1 = new Node(Node.SCRIPT, new Node(Node.FUNCTION, new Node(Node.BLOCK, new Node(Node.EMPTY))));
        int cost1 = InlineCostEstimator.getCost(root1, 10);
        assertEquals(0, cost1);

        Node root2 = new Node(Node.SCRIPT, new Node(Node.EMPTY), new Node(Node.VAR));
        int cost2 = InlineCostEstimator.getCost(root2, 2);
        assertEquals(0, cost2);

        Node root3 = new Node(Node.SCRIPT, new Node(Node.EMPTY), new Node(Node.EXPR_RESULT));
        int cost3 = InlineCostEstimator.getCost(root3, 5);
        assertEquals(3, cost3);
    }
}