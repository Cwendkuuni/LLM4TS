package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {
    @Test
    public void testGetCostNode() {
        Node root = new Node(Node.SCRIPT);
        assertEquals(0, InlineCostEstimator.getCost(root));
    }

    @Test
    public void testGetCostNodeInt() {
        Node root = new Node(Node.SCRIPT);
        assertEquals(0, InlineCostEstimator.getCost(root, 10));
        assertEquals(0, InlineCostEstimator.getCost(root, Integer.MAX_VALUE));
    }

    @Test
    public void testCompiledSizeEstimator() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        assertEquals(0, estimator.getCost());
    }

    @Test
    public void testContinueProcessing() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
        assertEquals(true, estimator.continueProcessing());
    }

    @Test
    public void testGetLastChar() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
        assertEquals('\0', estimator.getLastChar());
    }

    @Test
    public void testAppend() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
        estimator.append("test");
        assertEquals(4, estimator.getCost());
        assertEquals('t', estimator.getLastChar());
    }

    @Test
    public void testAddIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
        estimator.addIdentifier("test");
        assertEquals(2, estimator.getCost());
    }
}