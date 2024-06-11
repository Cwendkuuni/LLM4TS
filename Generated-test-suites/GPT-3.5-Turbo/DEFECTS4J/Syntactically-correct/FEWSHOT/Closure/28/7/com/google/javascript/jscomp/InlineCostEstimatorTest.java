package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {
    @Test
    public void testGetCost() {
        Node root = new Node(Node.SCRIPT);
        assertEquals(0, InlineCostEstimator.getCost(root));
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node(Node.SCRIPT);
        assertEquals(0, InlineCostEstimator.getCost(root, 100));
    }
}