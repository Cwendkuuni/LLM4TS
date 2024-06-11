package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class InlineCostEstimatorTest {
    @Test
    public void testGetCostWithRootNode() {
        Node root = new Node(Token.ROOT);
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithRootNodeAndCostThreshold() {
        Node root = new Node(Token.ROOT);
        int costThreshold = 100;
        int cost = InlineCostEstimator.getCost(root, costThreshold);
        assertEquals(0, cost);
    }

    @Test
    public void testAddIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.addIdentifier("identifier");
        int cost = estimator.getCost();
        assertEquals(2, cost);
    }

    @Test
    public void testContinueProcessing() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        boolean continueProcessing = estimator.continueProcessing();
        assertFalse(continueProcessing);
    }

    @Test
    public void testGetLastChar() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.append("string");
        char lastChar = estimator.getLastChar();
        assertEquals('g', lastChar);
    }

    @Test
    public void testAppend() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.append("string");
        int cost = estimator.getCost();
        assertEquals(6, cost);
    }
}