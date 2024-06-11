import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.CompiledCode;
import com.google.javascript.rhino.CompiledSizeEstimator;
import com.google.javascript.rhino.CodeConsumer;
import com.google.javascript.rhino.CodeGenerator;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostRoot() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    InlineCostEstimator.getCost(root, estimator);
    CodeConsumer codeConsumer = estimator;
    CodeGenerator codeGenerator = mock(CodeGenerator.class);
    when(root.getCodeGenerator()).thenReturn(codeGenerator);
    when(codeGenerator.getConsumer()).thenReturn(codeConsumer);

    String expectedCode = "abab";
    when(codeGenerator.generateCode(eq(root), anyInt(), anyInt())).thenReturn(new CompiledCode(expectedCode.getBytes()));

    int actualCost = InlineCostEstimator.getCost(root);
    int expectedCost = estimator.getCost();
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostRootWithThreshold() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
    InlineCostEstimator.getCost(root, estimator);
    CodeConsumer codeConsumer = estimator;
    CodeGenerator codeGenerator = mock(CodeGenerator.class);
    when(root.getCodeGenerator()).thenReturn(codeGenerator);
    when(codeGenerator.getConsumer()).thenReturn(codeConsumer);

    String expectedCode = "abab";
    when(codeGenerator.generateCode(eq(root), anyInt(), anyInt())).thenReturn(new CompiledCode(expectedCode.getBytes()));

    int actualCost = InlineCostEstimator.getCost(root);
    int expectedCost = estimator.getCost();
    assertEquals(expectedCost, actualCost);

    boolean isContinueProcessing = estimator.continueProcessing();
    assertEquals(false, isContinueProcessing);
  }

  @Test
  public void testGetCostEmpty() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    InlineCostEstimator.getCost(root, estimator);
    CodeConsumer codeConsumer = estimator;

    int actualCost = InlineCostEstimator.getCost(root);
    int expectedCost = 0;
    assertEquals(expectedCost, actualCost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostNull() {
    Node root = null;
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    InlineCostEstimator.getCost(root, estimator);
  }

  @Test
  public void testGetCostWithThreshold() {
    Node root = mock(Node.class);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
    InlineCostEstimator.getCost(root, estimator);
    CodeConsumer codeConsumer = estimator;
    CodeGenerator codeGenerator = mock(CodeGenerator.class);
    when(root.getCodeGenerator()).thenReturn(codeGenerator);
    when(codeGenerator.getConsumer()).thenReturn(codeConsumer);

    String expectedCode = "";
    when(codeGenerator.generateCode(eq(root), anyInt(), anyInt())).thenReturn(new CompiledCode(expectedCode.getBytes()));

    int actualCost = InlineCostEstimator.getCost(root);
    int expectedCost = estimator.getCost();
    assertEquals(expectedCost, actualCost);

    boolean isContinueProcessing = estimator.continueProcessing();
    assertEquals(false, isContinueProcessing);
  }
}