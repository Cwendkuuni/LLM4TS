import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompiledSizeEstimator;
import com.google.javascript.jscomp.CodeGenerator;
import com.google.javascript.jscomp.CodePrinter;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineCostEstimatorTest {

  private Node createMockNode() {
    CodePrinter mockPrinter = mock(CodePrinter.class);
    CodeGenerator mockGenerator = new CodeGenerator(mockPrinter);
    return mockGenerator.createNode("testIdentifier");
  }

  @Test
  public void testGetCost() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = createMockNode();
    int actualCost = estimator.getCost(root);
    int expectedCost = ESTIMATED_IDENTIFIER_COST;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostWithThreshold() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node root = createMockNode();
    CompiledSizeEstimator estimatorWrapper = new CompiledSizeEstimator(10);
    when(estimatorWrapper.getCost()).thenReturn(5);
    estimator.getCost(root, estimatorWrapper);
    verify(estimatorWrapper).getCost();
  }

  @Test
  public void testAddIdentifier() {
    InlineCostEstimator estimator = new InlineCostEstimator();
    CompiledSizeEstimator estimatorWrapper = mock(CompiledSizeEstimator.class);
    estimator.addIdentifier("testIdentifier", estimatorWrapper);
    String identifier = "testIdentifier";
    when(estimatorWrapper.append(any(String.class))).then(invocation -> {
      String str = (String) invocation.getArgument(0);
      assertEquals(identifier, str);
      return null;
    });
    estimator.addIdentifier("testIdentifier", estimatorWrapper);
  }
}