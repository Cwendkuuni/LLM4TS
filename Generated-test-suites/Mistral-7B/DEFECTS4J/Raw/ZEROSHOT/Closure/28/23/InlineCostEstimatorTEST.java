import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CompiledSizeEstimator;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    when(root.getChild(0)).thenReturn(Node.NULL);
    when(root.getChild(1)).thenReturn(Node.NULL);

    InlineCostEstimator.getCost(root, Integer.MAX_VALUE);

    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testGetCostWithChildren() {
    Node root = mock(Node.class);
    Node child1 = mock(Node.class);
    Node child2 = mock(Node.class);

    when(root.getChild(0)).thenReturn(child1);
    when(root.getChild(1)).thenReturn(child2);

    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);

    InlineCostEstimator.getCost(root, Integer.MAX_VALUE);

    InlineCostEstimator.add(child1, estimator);
    InlineCostEstimator.add(child2, estimator);

    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
    InlineCostEstimator.addIdentifier("test", estimator);

    assertEquals(2, estimator.getCost());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddIdentifierThrowsExceptionWithNegativeCostThreshold() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(-1);
    InlineCostEstimator.getCost(Node.NULL, -1);
  }
}