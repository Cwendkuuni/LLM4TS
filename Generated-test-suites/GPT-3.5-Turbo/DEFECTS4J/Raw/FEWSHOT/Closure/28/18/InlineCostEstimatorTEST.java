package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

    private Node rootNode;
    private InlineCostEstimator inlineCostEstimator;

    @Before
    public void setUp() {
        rootNode = new Node();
        inlineCostEstimator = new InlineCostEstimator();
    }

    @Test
    public void testGetCost() {
        int cost = inlineCostEstimator.getCost(rootNode);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithCostThreshold() {
        int cost = inlineCostEstimator.getCost(rootNode, 10);
        assertEquals(0, cost);
    }

    @Test
    public void testAdd() {
        inlineCostEstimator.add(rootNode);
        int cost = inlineCostEstimator.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testContinueProcessing() {
        boolean continueProcessing = inlineCostEstimator.continueProcessing();
        assertEquals(true, continueProcessing);
    }

    @Test
    public void testGetLastChar() {
        char lastChar = inlineCostEstimator.getLastChar();
        assertEquals('\0', lastChar);
    }

    @Test
    public void testAppend() {
        inlineCostEstimator.append("");
        int cost = inlineCostEstimator.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testAddIdentifier() {
        inlineCostEstimator.addIdentifier("");
        int cost = inlineCostEstimator.getCost();
        assertEquals(2, cost);
    }
}