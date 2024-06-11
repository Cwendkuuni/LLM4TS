
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {
    @Test
    public void testGetCost() {
        Node root = new Node();
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshhold() {
        Node root = new Node();
        int costThreshhold = 10;
        int cost = InlineCostEstimator.getCost(root, costThreshhold);
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimator() {
        CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        Node root = new Node();
        estimator.add(root);
        int cost = estimator.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testAppend() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String str = "abc";
        estimator.append(str);
        int cost = estimator.getCost();
        assertEquals(3, cost);
    }

    @Test
    public void testAddIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        String identifier = "abc";
        estimator.addIdentifier(identifier);
        int cost = estimator.getCost();
        assertEquals(2, cost);
    }
}
