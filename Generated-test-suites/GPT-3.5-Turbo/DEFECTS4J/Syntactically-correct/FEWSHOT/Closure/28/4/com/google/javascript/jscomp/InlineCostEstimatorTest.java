package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {
    @Test
    public void testGetCost() {
        Node root = new Node();
        int result = InlineCostEstimator.getCost(root);
        assertEquals(0, result);
    }

    @Test
    public void testGetCostWithThreshhold() {
        Node root = new Node();
        int costThreshhold = 10;
        int result = InlineCostEstimator.getCost(root, costThreshhold);
        assertEquals(0, result);
    }
}