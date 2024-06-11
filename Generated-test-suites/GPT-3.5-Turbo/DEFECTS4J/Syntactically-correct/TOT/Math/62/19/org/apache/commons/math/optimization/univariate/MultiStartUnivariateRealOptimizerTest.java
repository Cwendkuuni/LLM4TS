import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;

public class MultiStartUnivariateRealOptimizerTest {

  private MockOptimizer optimizer;
  private MultiStartUnivariateRealOptimizer<MockFunction> multiStartOptimizer;

  @Before
  public void setUp() throws Exception {
    optimizer = new MockOptimizer();
    multiStartOptimizer = new MultiStartUnivariateRealOptimizer<MockFunction>(optimizer, 3, new MockGenerator());
  }

  @Test
  public void testOptimize() {
    MockFunction function = new MockFunction();
    GoalType goal = GoalType.MINIMIZE;
    double min = 0.0;
    double max = 10.0;
    double startValue = 0.0;

    UnivariateRealPointValuePair result = null;
    try {
      result = multiStartOptimizer.optimize(function, goal, min, max, startValue);
    } catch (Exception e) {
      fail("Exception thrown");
    }

    assertNotNull(result);
    assertEquals(0.0, result.getPoint(), 0.0001);
    assertEquals(0.0, result.getValue(), 0.0001);
  }

  @Test(expected = MathIllegalStateException.class)
  public void testGetOptimaWithoutOptimize() {
    multiStartOptimizer.getOptima();
  }

  @Test
  public void testGetOptima() {
    MockFunction function = new MockFunction();
    GoalType goal = GoalType.MAXIMIZE;
    double min = 0.0;
    double max = 10.0;
    double startValue = 0.0;

    UnivariateRealPointValuePair result = null;
    try {
      result = multiStartOptimizer.optimize(function, goal, min, max, startValue);
    } catch (Exception e) {
      fail("Exception thrown");
    }

    UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
    assertNotNull(optima);
    assertEquals(3, optima.length);
    assertNotNull(optima[0]);
    assertNull(optima[1]);
    assertNull(optima[2]);
    assertEquals(0.0, optima[0].getPoint(), 0.0001);
    assertEquals(0.0, optima[0].getValue(), 0.0001);
  }

  @Test(expected = ConvergenceException.class)
  public void testOptimizeWithConvergenceException() {
    MockOptimizerConvergenceException optimizerWithException = new MockOptimizerConvergenceException();
    MultiStartUnivariateRealOptimizer<MockFunction> multiStartOptimizerWithException =
        new MultiStartUnivariateRealOptimizer<MockFunction>(optimizerWithException, 3, new MockGenerator());

    MockFunction function = new MockFunction();
    GoalType goal = GoalType.MAXIMIZE;
    double min = 0.0;
    double max = 10.0;
    double startValue = 0.0;

    multiStartOptimizerWithException.optimize(function, goal, min, max, startValue);
  }

  class MockOptimizer implements BaseUnivariateRealOptimizer<MockFunction> {
    
    private ConvergenceChecker<UnivariateRealPointValuePair> checker;
    private int maxEvaluations = Integer.MAX_VALUE;
    private int evaluations = 0;
    
    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
      this.checker = checker;
    }

    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
      return checker;
    }

    public int getMaxEvaluations() {
      return maxEvaluations;
    }
    
    public int getEvaluations() {
      return evaluations;
    }

    public void setMaxEvaluations(int maxEvaluations) {
      this.maxEvaluations = maxEvaluations;
    }

    public UnivariateRealPointValuePair optimize(MockFunction f, GoalType goal, double min, double max)
        throws MathIllegalStateException {
      evaluations++;
      return new UnivariateRealPointValuePair(0.0, 0.0);
    }
  }

  class MockOptimizerConvergenceException implements BaseUnivariateRealOptimizer<MockFunction> {

    private ConvergenceChecker<UnivariateRealPointValuePair> checker;
    private int maxEvaluations = Integer.MAX_VALUE;
    private int evaluations = 0;

    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
      this.checker = checker;
    }

    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
      return checker;
    }

    public int getMaxEvaluations() {
      return maxEvaluations;
    }

    public int getEvaluations() {
      return evaluations;
    }

    public void setMaxEvaluations(int maxEvaluations) {
      this.maxEvaluations = maxEvaluations;
    }

    public UnivariateRealPointValuePair optimize(MockFunction f, GoalType goal, double min, double max)
        throws MathIllegalStateException {
      evaluations++;
      throw new ConvergenceException(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, 3);
    }
  }

  class MockFunction implements UnivariateRealFunction {

    public double value(double x) {
      return x;
    }
  }

  class MockGenerator implements RandomGenerator {

    public double nextDouble() {
      return 0.5;
    }
  }

}