package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {
    
    @Test
    public void testGetCost() {
        Node root = new Node(Node.SCRIPT);
        
        int cost = InlineCostEstimator.getCost(root);
        
        assertEquals(0, cost);
    }
    
    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node(Node.SCRIPT);
        int threshold = 10;
        
        int cost = InlineCostEstimator.getCost(root, threshold);
        
        assertEquals(0, cost);
    }
    
    @Test
    public void testCompiledSizeEstimatorAdd() {
        Node root = new Node(Node.SCRIPT);
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
        
        estimator.add(root);
        
        assertEquals(0, estimator.getCost());
    }
    
    @Test
    public void testCompiledSizeEstimatorGetCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
        
        int cost = estimator.getCost();
        
        assertEquals(0, cost);
    }
    
    @Test
    public void testContinueProcessing() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
        
        boolean continueProcessing = estimator.continueProcessing();
        
        assertTrue(continueProcessing);
    }
    
    @Test
    public void testGetLastChar() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
        
        char lastChar = estimator.getLastChar();
        
        assertEquals('\0', lastChar);
    }
    
    @Test
    public void testAppend() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
        String str = "test";
        
        estimator.append(str);
        
        assertEquals(4, estimator.getCost());
    }
    
    @Test
    public void testAddIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
        String identifier = "test";
        
        estimator.addIdentifier(identifier);
        
        assertEquals(2, estimator.getCost());
    }
    
}