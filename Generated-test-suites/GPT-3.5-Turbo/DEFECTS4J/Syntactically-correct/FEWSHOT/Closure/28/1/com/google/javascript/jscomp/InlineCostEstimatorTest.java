
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        // Test case 1
        Node root1 = new Node(Token.SCRIPT);
        int expected1 = 0;
        assertEquals(expected1, InlineCostEstimator.getCost(root1));

        // Test case 2
        Node root2 = new Node(Token.SCRIPT);
        root2.addChildToFront(new Node(Token.ADD));
        int expected2 = 0;
        assertEquals(expected2, InlineCostEstimator.getCost(root2));

        // Test case 3
        Node root3 = new Node(Token.SCRIPT);
        root3.addChildToFront(new Node(Token.ADD));
        root3.getChildAtIndex(0).addChildToFront(new Node(Token.NAME, new String("a")));
        int expected3 = 2;
        assertEquals(expected3, InlineCostEstimator.getCost(root3));

        // Test case 4
        Node root4 = new Node(Token.SCRIPT);
        root4.addChildToFront(new Node(Token.ADD));
        root4.getChildAtIndex(0).addChildToFront(new Node(Token.NAME, new String("abc")));
        int expected4 = 6;
        assertEquals(expected4, InlineCostEstimator.getCost(root4));

        // Test case 5
        Node root5 = new Node(Token.SCRIPT);
        root5.addChildToFront(new Node(Token.ADD));
        root5.getChildAtIndex(0).addChildToFront(new Node(Token.NAME, new String("abcd")));
        root5.getChildAtIndex(0).addChildAfter(new Node(Token.NAME, new String("ef")));
        root5.getChildAtIndex(0).getChildAtIndex(1).addChildToFront(new Node(Token.NAME, new String("ghi")));
        int expected5 = 10;
        assertEquals(expected5, InlineCostEstimator.getCost(root5));
    }

    @Test
    public void testGetCostWithThreshold() {
        // Test case 1
        Node root1 = new Node(Token.SCRIPT);
        int costThreshhold1 = 0;
        int expected1 = 0;
        assertEquals(expected1, InlineCostEstimator.getCost(root1, costThreshhold1));

        // Test case 2
        Node root2 = new Node(Token.SCRIPT);
        root2.addChildToFront(new Node(Token.ADD));
        int costThreshhold2 = 0;
        int expected2 = 0;
        assertEquals(expected2, InlineCostEstimator.getCost(root2, costThreshhold2));

        // Test case 3
        Node root3 = new Node(Token.SCRIPT);
        root3.addChildToFront(new Node(Token.ADD));
        root3.getChildAtIndex(0).addChildToFront(new Node(Token.NAME, new String("a")));
        int costThreshhold3 = 2;
        int expected3 = 2;
        assertEquals(expected3, InlineCostEstimator.getCost(root3, costThreshhold3));

        // Test case 4
        Node root4 = new Node(Token.SCRIPT);
        root4.addChildToFront(new Node(Token.ADD));
        root4.getChildAtIndex(0).addChildToFront(new Node(Token.NAME, new String("abc")));
        int costThreshhold4 = 6;
        int expected4 = 6;
        assertEquals(expected4, InlineCostEstimator.getCost(root4, costThreshhold4));

        // Test case 5
        Node root5 = new Node(Token.SCRIPT);
        root5.addChildToFront(new Node(Token.ADD));
        root5.getChildAtIndex(0).addChildToFront(new Node(Token.NAME, new String("abcd")));
        root5.getChildAtIndex(0).addChildAfter(new Node(Token.NAME, new String("ef")));
        root5.getChildAtIndex(0).getChildAtIndex(1).addChildToFront(new Node(Token.NAME, new String("ghi")));
        int costThreshhold5 = 9;
        int expected5 = 9;
        assertEquals(expected5, InlineCostEstimator.getCost(root5, costThreshhold5));
    }

    @Test
    public void testCompiledSizeEstimator() {
        // Test case 1
        CompiledSizeEstimator estimator1 = new CompiledSizeEstimator(0);
        Node root1 = new Node(Token.SCRIPT);
        estimator1.add(root1);
        int expected1 = 0;
        assertEquals(expected1, estimator1.getCost());

        // Test case 2
        CompiledSizeEstimator estimator2 = new CompiledSizeEstimator(0);
        Node root2 = new Node(Token.SCRIPT);
        root2.addChildToFront(new Node(Token.ADD));
        estimator2.add(root2);
        int expected2 = 0;
        assertEquals(expected2, estimator2.getCost());

        // Test case 3
        CompiledSizeEstimator estimator3 = new CompiledSizeEstimator(2);
        Node root3 = new Node(Token.SCRIPT);
        root3.addChildToFront(new Node(Token.ADD));
        root3.getChildAtIndex(0).addChildToFront(new Node(Token.NAME, new String("a")));
        estimator3.add(root3);
        int expected3 = 2;
        assertEquals(expected3, estimator3.getCost());

        // Test case 4
        CompiledSizeEstimator estimator4 = new CompiledSizeEstimator(6);
        Node root4 = new Node(Token.SCRIPT);
        root4.addChildToFront(new Node(Token.ADD));
        root4.getChildAtIndex(0).addChildToFront(new Node(Token.NAME, new String("abc")));
        estimator4.add(root4);
        int expected4 = 6;
        assertEquals(expected4, estimator4.getCost());

        // Test case 5
        CompiledSizeEstimator estimator5 = new CompiledSizeEstimator(10);
        Node root5 = new Node(Token.SCRIPT);
        root5.addChildToFront(new Node(Token.ADD));
        root5.getChildAtIndex(0).addChildToFront(new Node(Token.NAME, new String("abcd")));
        root5.getChildAtIndex(0).addChildAfter(new Node(Token.NAME, new String("ef")));
        root5.getChildAtIndex(0).getChildAtIndex(1).addChildToFront(new Node(Token.NAME, new String("ghi")));
        estimator5.add(root5);
        int expected5 = 10;
        assertEquals(expected5, estimator5.getCost());
    }
}

