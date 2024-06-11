
package com.google.javascript.jscomp;

import org.junit.Test;

import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    // Test the getCost method with a simple Node
    @Test
    public void testGetCost() {
        Node node = new Node(Token.ADD);
        int cost = InlineCostEstimator.getCost(node);
        assertEquals(0, cost);
    }

    // Test the getCost method with a complex Node and cost threshold
    @Test
    public void testGetCostWithThreshold() {
        Node node = new Node(Token.MUL, new Node(Token.NUMBER, 5), new Node(Token.NUMBER, 10));
        int cost = InlineCostEstimator.getCost(node, 20);
        assertEquals(7, cost);
    }

    // Test the getCost method with a null root Node
    @Test
    public void testGetCostWithNullRoot() {
        int cost = InlineCostEstimator.getCost(null);
        assertEquals(0, cost);
    }

    // Test the getCost method with a null root Node and cost threshold
    @Test
    public void testGetCostWithNullRootAndThreshold() {
        int cost = InlineCostEstimator.getCost(null, 10);
        assertEquals(0, cost);
    }
}
