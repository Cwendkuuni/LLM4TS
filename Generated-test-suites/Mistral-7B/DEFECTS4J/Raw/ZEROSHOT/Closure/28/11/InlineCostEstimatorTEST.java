import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.CompiledSizeEstimator;
import com.google.javascript.rhino.CodeGenerator;
import com.google.javascript.rhino.CodeConsumer;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node rootNode = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    InlineCostEstimator.getCost(rootNode, estimator);

    CodeConsumer codeConsumer = estimator;
    CodeGenerator codeGenerator = mock(CodeGenerator.class);
    when(codeGenerator.add(rootNode)).thenReturn(codeConsumer);

    int cost = InlineCostEstimator.getCost(rootNode);
    assertEquals(estimator.getCost(), cost);
  }

  @Test
  public void testGetCostThreshhold() {
    Node rootNode = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
    InlineCostEstimator.getCost(rootNode, estimator);

    CodeConsumer codeConsumer = estimator;
    CodeGenerator codeGenerator = mock(CodeGenerator.class);
    when(codeGenerator.add(rootNode)).thenReturn(codeConsumer);

    int cost = InlineCostEstimator.getCost(rootNode);
    assertEquals(estimator.getCost(), cost);
    assertEquals(5, estimator.getCost());
  }

  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    InlineCostEstimator.addIdentifier(estimator, "testIdentifier");

    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 2, estimator.getCost());
  }
}