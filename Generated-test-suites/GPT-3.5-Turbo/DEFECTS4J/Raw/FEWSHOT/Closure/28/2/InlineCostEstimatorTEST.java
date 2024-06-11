package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InlineCostEstimatorTest {
    private InlineCostEstimator estimator;

    @Before
    public void setUp() {
        estimator = new InlineCostEstimator();
    }

    @Test
    public void testGetCost_DefaultThreshold() {
        Node root = new Node(Node.SCRIPT);
        root.addChildToFront(new Node(Node.EMPTY));
        root.getFirstChild().addChildToFront(new Node(Node.VAR));

        int cost = estimator.getCost(root);

        assertEquals(3, cost);
    }

    @Test
    public void testGetCost_CustomThreshold() {
        Node root = new Node(Node.SCRIPT);
        root.addChildToFront(new Node(Node.EMPTY));
        root.getFirstChild().addChildToFront(new Node(Node.VAR));

        int cost = estimator.getCost(root, 5);

        assertEquals(3, cost);
    }

    @Test
    public void testContinueProcessing_Continue() {
        CompiledSizeEstimator compiledSizeEstimator = new CompiledSizeEstimator(10);

        boolean continueProcessing = compiledSizeEstimator.continueProcessing();

        assertTrue(continueProcessing);
    }

    @Test
    public void testContinueProcessing_Stop() {
        CompiledSizeEstimator compiledSizeEstimator = new CompiledSizeEstimator(5);
        compiledSizeEstimator.append("123456789");

        boolean continueProcessing = compiledSizeEstimator.continueProcessing();

        assertFalse(continueProcessing);
    }

    @Test
    public void testGetLastChar() {
        CompiledSizeEstimator compiledSizeEstimator = new CompiledSizeEstimator(10);
        compiledSizeEstimator.append("123");

        char lastChar = compiledSizeEstimator.getLastChar();

        assertEquals('3', lastChar);
    }

    @Test
    public void testAppend() {
        CompiledSizeEstimator compiledSizeEstimator = new CompiledSizeEstimator(10);

        compiledSizeEstimator.append("123");

        assertEquals(3, compiledSizeEstimator.getCost());
    }

    @Test
    public void testAddIdentifier() {
        CompiledSizeEstimator compiledSizeEstimator = new CompiledSizeEstimator(10);

        compiledSizeEstimator.addIdentifier("abc");

        assertEquals(2, compiledSizeEstimator.getCost());
    }
}