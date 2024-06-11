package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {
    @Test
    public void testGetCost() {
        Node root = null; // create an instance of Node
        int expected = 0; // set the expected value
        int result = InlineCostEstimator.getCost(root); // call the method to be tested
        assertEquals(expected, result); // assert the result
    }

    @Test
    public void testGetCostWithOptions() {
        Node root = null; // create an instance of Node
        int costThreshhold = 10; // set the cost threshold
        int expected = 0; // set the expected value
        int result = InlineCostEstimator.getCost(root, costThreshhold); // call the method to be tested
        assertEquals(expected, result); // assert the result
    }
}