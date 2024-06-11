import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompiledSizeEstimator;
import com.google.javascript.jscomp.CodeGenerator;
import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class InlineCostEstimatorTest {

  private CompiledSizeEstimator estimator;
  private CodeGenerator codeGenerator;

  @Before
  public void setup() {
    codeGenerator = mock(CodeGenerator.class);
    estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
  }

  @Test
  public void testGetCost() {
    Node root = mock(Node.class);
    when(root.getFirstChild()).thenReturn(null);

    estimator.add(root);
    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testGetCostWithRoot() {
    Node root = mock(Node.class);
    when(root.getFirstChild()).thenReturn(null);

    estimator.add(root);
    estimator.getCost(); // Exercise getCost() method after add()
    assertEquals(0, estimator.getCost());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    estimator = new CompiledSizeEstimator(-1);
    estimator.add(mock(Node.class));
  }

  @Test
  public void testGetCostWithLargeCostThreshold() {
    Node root = mock(Node.class);
    when(root.getFirstChild()).thenReturn(null);

    estimator.add(root);
    estimator.add(ESTIMATED_IDENTIFIER);
    estimator.add(ESTIMATED_IDENTIFIER);
    estimator.add(ESTIMATED_IDENTIFIER);

    assertEquals(ESTIMATED_IDENTIFIER_COST * 3, estimator.getCost());
  }

  @Test
  public void testGetLastChar() {
    estimator.add("testString");
    assertEquals('t', estimator.getLastChar());
  }

  @Test
  public void testContinueProcessing() {
    estimator.add("testString");
    estimator.add(ESTIMATED_IDENTIFIER);
    estimator.add(ESTIMATED_IDENTIFIER);

    assertFalse(estimator.continueProcessing());
  }

  @Test
  public void testAddIdentifier() {
    estimator.addIdentifier(ESTIMATED_IDENTIFIER);
    assertEquals(ESTIMATED_IDENTIFIER_COST, estimator.getCost());
  }

  @Test
  public void testAppend() {
    estimator.append("testString");
    assertEquals("testString".length(), estimator.getCost());
  }

  @Test
  public void testAddCodeGenerator() {
    when(codeGenerator.toString()).thenReturn("testCodeString");
    estimator.add(codeGenerator);

    assertEquals("testCodeString".length(), estimator.getCost());
  }
}