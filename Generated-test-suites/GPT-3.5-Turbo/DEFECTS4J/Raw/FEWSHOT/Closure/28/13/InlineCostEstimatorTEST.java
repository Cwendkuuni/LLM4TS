package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        // Test empty Node
        Node emptyRoot = new Node(Node.IR_BLOCK);
        assertEquals(0, InlineCostEstimator.getCost(emptyRoot));

        // Test Node with single character
        Node singleCharRoot = new Node(Node.IR_STRING).setString("a");
        assertEquals(1, InlineCostEstimator.getCost(singleCharRoot));

        // Test Node with multiple characters
        Node multipleCharsRoot = new Node(Node.IR_STRING).setString("abc");
        assertEquals(3, InlineCostEstimator.getCost(multipleCharsRoot));
    }

    @Test
    public void testGetCostWithCostThreshold() {
        Node root = new Node(Node.IR_STRING).setString("abcde");

        // Test when cost threshold is greater than the node cost
        assertEquals(5, InlineCostEstimator.getCost(root, 10));

        // Test when cost threshold is equal to the node cost
        assertEquals(5, InlineCostEstimator.getCost(root, 5));

        // Test when cost threshold is less than the node cost
        assertEquals(10, InlineCostEstimator.getCost(root, 4));
    }

    @Test
    public void testCompiledSizeEstimator() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        assertEquals(10, estimator.maxCost);
        assertEquals(0, estimator.cost);
        assertEquals('\0', estimator.last);
        assertTrue(estimator.continueProcessing);

        // Test add()
        Node root = new Node(Node.IR_BLOCK);
        estimator.add(root);

        // Test getCost()
        assertEquals(0, estimator.getCost());

        // Test continueProcessing()
        assertTrue(estimator.continueProcessing());

        // Test getLastChar()
        assertEquals('\0', estimator.getLastChar());

        // Test append() with str of length 3
        String str = "abc";
        estimator.append(str);
        assertEquals('c', estimator.last);
        assertEquals(3, estimator.cost);
        assertTrue(estimator.continueProcessing);

        // Test addIdentifier()
        String identifier = "identifier";
        estimator.addIdentifier(identifier);
        assertEquals('r', estimator.last);
        assertEquals(18, estimator.cost);
        assertFalse(estimator.continueProcessing);
    }
}