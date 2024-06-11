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
    public void testGetCostWithThreshold() {
        Node root = new Node();
        int result = InlineCostEstimator.getCost(root, 100);
        assertEquals(0, result);
    }

    @Test
    public void testCompiledSizeEstimatorAdd() {
        Node root = new Node();
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.add(root);
        int result = estimator.getCost();
        assertEquals(0, result);
    }

    @Test
    public void testCompiledSizeEstimatorGetCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        int result = estimator.getCost();
        assertEquals(0, result);
    }

    @Test
    public void testCompiledSizeEstimatorContinueProcessing() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        boolean result = estimator.continueProcessing();
        assertEquals(true, result);
    }

    @Test
    public void testCompiledSizeEstimatorGetLastChar() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        char result = estimator.getLastChar();
        assertEquals('\0', result);
    }

    @Test
    public void testCompiledSizeEstimatorAppend() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.append("");
        int result1 = estimator.getCost();
        assertEquals(0, result1);
        estimator.append("a");
        int result2 = estimator.getCost();
        assertEquals(1, result2);
    }

    @Test
    public void testCompiledSizeEstimatorAddIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.addIdentifier("");
        int result1 = estimator.getCost();
        assertEquals(2, result1);
        estimator.addIdentifier("test");
        int result2 = estimator.getCost();
        assertEquals(4, result2);
    }
}