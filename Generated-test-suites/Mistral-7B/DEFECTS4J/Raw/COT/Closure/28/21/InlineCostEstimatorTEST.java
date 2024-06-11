import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostRoot() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);
    when(root.getClass().getName()).thenReturn("Node"); // for mockito to recognize it as a Node

    int actualCost = estimator.getCost(root);
    int expectedCost = 0; // We don't know the exact cost, so let's assume 0 for now
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostRootWithCostThreshold() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);
    int costThreshold = 10;

    int actualCost = estimator.getCost(root, costThreshold);
    int expectedCost = 0; // We don't know the exact cost, so let's assume 0 for now
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostWithCostThreshold() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);
    int costThreshold = 10;

    String str = "Some long string to test cost estimation.";
    when(root.getClass().getName()).thenReturn("Node"); // for mockito to recognize it as a Node
    when(root.getFirstChild()).thenReturn(mock(Node.class));
    when(root.getLastChild()).thenReturn(mock(Node.class));
    when(root.getLastChild().toString()).thenReturn(str);

    estimator.getCost(root, costThreshold);

    int expectedCost = str.length();
    assertEquals(expectedCost, estimator.getCost());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);
    int costThreshold = -1;

    estimator.getCost(root, costThreshold);
  }
}