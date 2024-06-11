package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node();
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node();
        int cost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThresholdEdgeCase() {
        Node root = new Node();
        int cost = InlineCostEstimator.getCost(root, 5);
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimatorAdd() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
        Node root = new Node();
        estimator.add(root);
        int cost = estimator.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimatorContinueProcessing() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
        assertFalse(estimator.continueProcessing());
    }

    @Test
    public void testCompiledSizeEstimatorAppend() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
        estimator.append("test");
        int cost = estimator.getCost();
        assertEquals(4, cost);
    }

    @Test
    public void testCompiledSizeEstimatorAddIdentifier() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
        estimator.addIdentifier("test");
        int cost = estimator.getCost();
        assertEquals(2, cost);
    }

}