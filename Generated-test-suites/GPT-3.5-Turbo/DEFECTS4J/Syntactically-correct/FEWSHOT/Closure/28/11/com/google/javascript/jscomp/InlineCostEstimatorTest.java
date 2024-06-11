package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {
    @Test
    public void testGetCost() {
        // Test case 1
        Node root1 = new Node(Token.EMPTY);
        int result1 = InlineCostEstimator.getCost(root1);
        assertEquals(0, result1);

        // Test case 2
        Node root2 = new Node(Token.AND);
        root2.addChildToFront(new Node(Token.EMPTY));
        int result2 = InlineCostEstimator.getCost(root2);
        assertEquals(0, result2);

        // Test case 3
        Node root3 = new Node(Token.AND);
        root3.addChildToFront(new Node(Token.STRING));
        int result3 = InlineCostEstimator.getCost(root3);
        assertEquals(0, result3);
    }

    @Test
    public void testGetCostWithThreshold() {
        // Test case 1
        Node root1 = new Node(Token.NULL);
        int result1 = InlineCostEstimator.getCost(root1, 1);
        assertEquals(4, result1);

        // Test case 2
        Node root2 = new Node(Token.TRUE);
        int result2 = InlineCostEstimator.getCost(root2, 2);
        assertEquals(5, result2);

        // Test case 3
        Node root3 = new Node(Token.FALSE);
        int result3 = InlineCostEstimator.getCost(root3, 3);
        assertEquals(6, result3);
    }
}