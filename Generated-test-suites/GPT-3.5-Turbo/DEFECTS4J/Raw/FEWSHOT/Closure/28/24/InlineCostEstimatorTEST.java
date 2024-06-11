package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost_withRootNode() {
        InlineCostEstimator estimator = new InlineCostEstimator();
        Node rootNode = new Node();
        int result = estimator.getCost(rootNode);
        assertEquals(0, result);
    }

    @Test
    public void testGetCost_withRootNodeAndCostThreshold() {
        InlineCostEstimator estimator = new InlineCostEstimator();
        Node rootNode = new Node();
        int costThreshold = 100;
        int result = estimator.getCost(rootNode, costThreshold);
        assertEquals(0, result);
    }

    @Test
    public void testCompiledSizeEstimator_add() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        Node node = new Node();
        estimator.add(node);
        int result = estimator.getCost();
        assertEquals(0, result);
    }

    @Test
    public void testCompiledSizeEstimator_getLastChar() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        char result = estimator.getLastChar();
        assertEquals('\0', result);
    }

    @Test
    public void testCompiledSizeEstimator_append() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String str = "test";
        estimator.append(str);
        int result = estimator.getCost();
        assertEquals(4, result);
    }

    @Test
    public void testCompiledSizeEstimator_addIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        String identifier = "test";
        estimator.addIdentifier(identifier);
        int result = estimator.getCost();
        assertEquals(2, result);
    }
}