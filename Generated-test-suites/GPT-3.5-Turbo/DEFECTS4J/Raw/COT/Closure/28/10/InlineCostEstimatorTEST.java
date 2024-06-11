import static org.junit.Assert.*;

import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {
  
  @Test
  public void testGetCost() {
    Node root = new Node(Node.SCRIPT);
    int cost = InlineCostEstimator.getCost(root);
    
    assertEquals(0, cost);
  }
  
  @Test
  public void testGetCost_CostThreshold() {
    Node root = new Node(Node.SCRIPT);
    int costThreshold = 10;
    int cost = InlineCostEstimator.getCost(root, costThreshold);
    
    assertEquals(0, cost);
  }

  @Test
  public void testCompiledSizeEstimator_Add() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    Node root = new Node(Node.SCRIPT);
    estimator.add(root);
    
    int cost = estimator.getCost();
    
    assertEquals(0, cost);
  }
  
  @Test
  public void testCompiledSizeEstimator_GetCost() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    int cost = estimator.getCost();
    
    assertEquals(0, cost);
  }
  
  @Test
  public void testAppend() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.append("test");
    int cost = estimator.getCost();
    
    assertEquals(4, cost);
  }
  
  @Test
  public void testAddIdentifier() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.addIdentifier("identifier");
    int cost = estimator.getCost();
    
    assertEquals(2, cost);
  }
  
}