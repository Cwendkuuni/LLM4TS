package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {
    
    @Test
    public void testGetCost() {
        Node root = new Node();
        int result = InlineCostEstimator.getCost(root);
        assertEquals(0, result);
    }
    
    @Test
    public void testGetCostWithCostThreshold() {
        Node root = new Node();
        int costThreshold = 100;
        int result = InlineCostEstimator.getCost(root, costThreshold);
        assertEquals(0, result);
    }
    
}