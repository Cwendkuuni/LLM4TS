package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InlineCostEstimatorTest {
  @Test
  public void testGetCost() {
    // Test case 1: empty root node
    Node root1 = new Node(Token.EMPTY);
    assertEquals(0, InlineCostEstimator.getCost(root1));

    // Test case 2: root node with one child node
    Node root2 = new Node(Token.ROOT);
    root2.addChildToBack(new Node(Token.STRING, "test"));
    assertEquals(4, InlineCostEstimator.getCost(root2));

    // Test case 3: root node with multiple child nodes
    Node root3 = new Node(Token.ROOT);
    root3.addChildToBack(new Node(Token.STRING, "test1"));
    root3.addChildToBack(new Node(Token.STRING, "test2"));
    root3.addChildToBack(new Node(Token.STRING, "test3"));
    assertEquals(12, InlineCostEstimator.getCost(root3));

    // Test case 4: nested child nodes
    Node root4 = new Node(Token.ROOT);
    Node child1 = new Node(Token.STRING, "test1");
    Node child2 = new Node(Token.STRING, "test2");
    child1.addChildToBack(new Node(Token.STRING, "test3"));
    root4.addChildToBack(child1);
    root4.addChildToBack(child2);
    assertEquals(14, InlineCostEstimator.getCost(root4));
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = new Node(Token.ROOT);
    root.addChildToBack(new Node(Token.STRING, "test1"));
    root.addChildToBack(new Node(Token.STRING, "test2"));
    root.addChildToBack(new Node(Token.STRING, "test3"));
    assertEquals(12, InlineCostEstimator.getCost(root, 15));
    assertEquals(10, InlineCostEstimator.getCost(root, 11));
    assertEquals(6, InlineCostEstimator.getCost(root, 6));
  }
}