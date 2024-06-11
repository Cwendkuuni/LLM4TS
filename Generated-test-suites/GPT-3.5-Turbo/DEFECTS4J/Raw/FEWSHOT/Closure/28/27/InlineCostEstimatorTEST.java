package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node(/* node type and child nodes */);
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node(/* node type and child nodes */);
        int cost = InlineCostEstimator.getCost(root, 10);
        assertEquals(0, cost);
    }
}