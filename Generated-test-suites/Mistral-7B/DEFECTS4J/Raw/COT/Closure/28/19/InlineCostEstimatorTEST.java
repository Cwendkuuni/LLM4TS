import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.CompiledSizeEstimator;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyCode() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);
    when(root.getFirstChild()).thenReturn(null);

    int cost = estimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSimpleCode() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);
    when(root.getFirstChild()).thenReturn(mock(Node.class));
    when(root.getLastChild()).thenReturn(mock(Node.class));
    when(root.getFirstChild().getType()).thenReturn(Node.IDENTIFIER);
    when(root.getLastChild().getType()).thenReturn(Node.IDENTIFIER);
    when(root.getFirstChild().getFirstChild()).thenReturn(mock(Node.class));
    when(root.getLastChild().getFirstChild()).thenReturn(mock(Node.class));
    when(root.getFirstChild().getLastChild()).thenReturn(mock(Node.class));
    when(root.getLastChild().getLastChild()).thenReturn(mock(Node.class));
    when(root.getFirstChild().getFirstChild().getType()).thenReturn(Node.NumberLiteral);
    when(root.getLastChild().getFirstChild().getType()).thenReturn(Node.NumberLiteral);
    when(root.getFirstChild().getLastChild().getType()).thenReturn(Node.NumberLiteral);
    when(root.getLastChild().getLastChild().getType()).thenReturn(Node.NumberLiteral);

    int cost = estimator.getCost(root);
    assertEquals(4, cost); // 2 identifiers + 2 numbers
  }

  @Test
  public void testGetCostOfComplexCode() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);
    Node child1 = mock(Node.class);
    Node child2 = mock(Node.class);
    Node child3 = mock(Node.class);
    Node child4 = mock(Node.class);
    Node child5 = mock(Node.class);
    Node child6 = mock(Node.class);
    Node child7 = mock(Node.class);
    Node child8 = mock(Node.class);

    when(root.getFirstChild()).thenReturn(child1);
    when(root.getLastChild()).thenReturn(child8);
    when(child1.getType()).thenReturn(Node.BLOCK);
    when(child2.getType()).thenReturn(Node.BLOCK);
    when(child3.getType()).thenReturn(Node.BLOCK);
    when(child4.getType()).thenReturn(Node.NUMBER_LITERAL);
    when(child5.getType()).thenReturn(Node.NUMBER_LITERAL);
    when(child6.getType()).thenReturn(Node.NUMBER_LITERAL);
    when(child7.getType()).thenReturn(Node.NUMBER_LITERAL);
    when(child8.getType()).thenReturn(Node.NUMBER_LITERAL);

    when(child1.getFirstChild()).thenReturn(child4);
    when(child1.getLastChild()).thenReturn(child5);
    when(child2.getFirstChild()).thenReturn(child6);
    when(child2.getLastChild()).thenReturn(child7);

    when(child3.getFirstChild()).thenReturn(root);
    when(child3.getLastChild()).thenReturn(child8);

    int cost = estimator.getCost(root);
    assertEquals(12, cost); // 4 identifiers + 8 numbers
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeThreshold() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);

    estimator.getCost(root, -1);
  }

  @Test
  public void testGetCostWithMaxThreshold() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);

    int cost = estimator.getCost(root, Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, cost);
  }

  @Test
  public void testGetCostWithThresholdLessThanMax() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = mock(Node.class);

    int cost = estimator.getCost(root, 10);
    assertEquals(10, cost);
  }
}