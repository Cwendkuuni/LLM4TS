package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        // create sample test cases
        Node rootNode1 = new Node();
        Node rootNode2 = new Node();
        Node rootNode3 = new Node();

        // add child nodes to sample test cases
        rootNode1.addChildToFront(new Node());
        rootNode1.addChildToFront(new Node());
        rootNode2.addChildToFront(new Node());
        rootNode2.addChildToFront(new Node());
        rootNode3.addChildToFront(new Node());
        rootNode3.addChildToFront(new Node());

        // test getCost() method
        assertEquals(0, InlineCostEstimator.getCost(rootNode1));
        assertEquals(0, InlineCostEstimator.getCost(rootNode2));
        assertEquals(0, InlineCostEstimator.getCost(rootNode3));
    }

    @Test
    public void testGetCostWithThreshold() {
        // create sample test cases
        Node rootNode1 = new Node();
        Node rootNode2 = new Node();
        Node rootNode3 = new Node();

        // add child nodes to sample test cases
        rootNode1.addChildToFront(new Node());
        rootNode1.addChildToFront(new Node());
        rootNode2.addChildToFront(new Node());
        rootNode2.addChildToFront(new Node());
        rootNode3.addChildToFront(new Node());
        rootNode3.addChildToFront(new Node());

        // test getCost() method with threshold
        assertEquals(0, InlineCostEstimator.getCost(rootNode1, 10));
        assertEquals(0, InlineCostEstimator.getCost(rootNode2, 10));
        assertEquals(0, InlineCostEstimator.getCost(rootNode3, 10));
    }
}