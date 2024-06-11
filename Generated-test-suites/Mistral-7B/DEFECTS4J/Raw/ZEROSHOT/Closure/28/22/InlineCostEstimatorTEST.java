import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.CompiledSizeEstimator;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = mock(Node.class);
    InlineCostEstimator estimator = new InlineCostEstimator();
    CompiledSizeEstimator compiledSizeEstimator = new CompiledSizeEstimator(Integer.MAX_VALUE);

    when(root.getFirstChild()).thenReturn(null);

    int cost = estimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = mock(Node.class);
    InlineCostEstimator estimator = new InlineCostEstimator();
    CompiledSizeEstimator compiledSizeEstimator = mock(CompiledSizeEstimator.class);

    when(root.getFirstChild()).thenReturn(null);
    when(compiledSizeEstimator.getCost()).thenReturn(10);

    estimator.getCost(root, compiledSizeEstimator);

    // Since getCost method in CompiledSizeEstimator does not change the costThreshhold, we cannot directly verify its value.
    // Instead, we can test if the InlineCostEstimator correctly used the CompiledSizeEstimator's cost.
    verify(compiledSizeEstimator).getCost();
  }

  @Test
  public void testAddNode() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    CompiledSizeEstimator compiledSizeEstimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    Node root = mock(Node.class);

    when(root.getFirstChild()).thenReturn(null);

    estimator.getCost(root, compiledSizeEstimator);

    // Since getCost method in InlineCostEstimator does not change the root node, we cannot directly verify its state.
    // Instead, we can test if the CompiledSizeEstimator correctly processed the node.
    verify(compiledSizeEstimator).add(root);
  }
}