
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

  private MultiStartUnivariateRealOptimizer optimizer;
  private UnivariateRealFunction function;
  private RandomGenerator generator;

  @Before
  public void setUp() {
    optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizerImpl(), 5, generator);
    function = new UnivariateRealFunctionImpl();
    generator = new RandomGeneratorImpl();
  }

  @Test
  public void testGetFunctionValue() throws Exception {
    double functionValue = optimizer.getFunctionValue();
    assertEquals(0.0, functionValue, 0.00001);
  }

  @Test
  public void testGetResult() throws Exception {
    double result = optimizer.getResult();
    assertEquals(0.0, result, 0.00001);
  }

  @Test
  public void testGetAbsoluteAccuracy() throws Exception {
    double absoluteAccuracy = optimizer.getAbsoluteAccuracy();
    assertEquals(1e-6, absoluteAccuracy, 0.00001);
  }

  @Test
  public void testGetIterationCount() throws Exception {
    int iterationCount = optimizer.getIterationCount();
    assertEquals(0, iterationCount);
  }

  @Test
  public void testGetMaximalIterationCount() throws Exception {
    int maximalIterationCount = optimizer.getMaximalIterationCount();
    assertEquals(Integer.MAX_VALUE, maximalIterationCount);
  }

  @Test
  public void testGetMaxEvaluations() throws Exception {
    int maxEvaluations = optimizer.getMaxEvaluations();
    assertEquals(Integer.MAX_VALUE, maxEvaluations);
  }

  @Test
  public void testGetEvaluations() throws Exception {
    int evaluations = optimizer.getEvaluations();
    assertEquals(0, evaluations);
  }

  @Test
  public void testGetRelativeAccuracy() throws Exception {
    double relativeAccuracy = optimizer.getRelativeAccuracy();
    assertEquals(1e-14, relativeAccuracy, 0.00001);
  }

  @Test
  public void testResetAbsoluteAccuracy() throws Exception {
    optimizer.resetAbsoluteAccuracy();
    assertEquals(0.0, optimizer.getAbsoluteAccuracy(), 0.00001);
  }

  @Test
  public void testResetMaximalIterationCount() throws Exception {
    optimizer.resetMaximalIterationCount();
    assertEquals(Integer.MAX_VALUE, optimizer.getMaximalIterationCount());
  }

  @Test
  public void testResetRelativeAccuracy() throws Exception {
    optimizer.resetRelativeAccuracy();
    assertEquals(0.0, optimizer.getRelativeAccuracy(), 0.00001);
  }

  @Test
  public void testSetAbsoluteAccuracy() throws Exception {
    optimizer.setAbsoluteAccuracy(0.05);
    assertEquals(0.05, optimizer.getAbsoluteAccuracy(), 0.00001);
  }

  @Test
  public void testSetMaximalIterationCount() throws Exception {
    optimizer.setMaximalIterationCount(100);
    assertEquals(100, optimizer.getMaximalIterationCount());
  }

  @Test
  public void testSetMaxEvaluations() throws Exception {
    optimizer.setMaxEvaluations(1000);
    assertEquals(1000, optimizer.getMaxEvaluations());
  }

  @Test
  public void testSetRelativeAccuracy() throws Exception {
    optimizer.setRelativeAccuracy(1e-8);
    assertEquals(1e-8, optimizer.getRelativeAccuracy(), 0.00001);
  }

  @Test
  public void testGetOptima() throws Exception {
    double[] optima = optimizer.getOptima();

    for (int i = 0; i < optima.length; i++) {
      assertTrue(Double.isNaN(optima[i]));
    }
  }

  @Test
  public void testGetOptimaValues() throws Exception {
    double[] optimaValues = optimizer.getOptimaValues();

    for (int i = 0; i < optimaValues.length; i++) {
      assertTrue(Double.isNaN(optimaValues[i]));
    }
  }

  @Test
  public void testOptimize() throws Exception {
    double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
    assertEquals(0.0, result, 0.00001);
  }

  @Test
  public void testOptimizeWithStartValue() throws Exception {
    double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 5);
    assertEquals(0.0, result, 0.00001);
  }
}
