import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

  @Test
  public void testSetConvergenceChecker() {
    // Test creating a convergence checker and setting it
    MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealPointValuePairOptimizer(), 5, new RandomGenerator());
    ConvergenceChecker<UnivariateRealPointValuePair> checker = new SimplePointChecker(0.01, 0.01);
    optimizer.setConvergenceChecker(checker);
    assertEquals(checker, optimizer.getConvergenceChecker());
  }

  @Test
  public void testOptimize() {
    // Test optimizing a function with a given goal, min, and max
    MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealPointValuePairOptimizer(), 5, new RandomGenerator());
    UnivariateRealFunction function = new UnivariateRealFunction() {
        public double value(double x) {
            return x * x;
        }
    };
    UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
    assertNotNull(result);
    assertEquals(0.0, result.getValue(), 0.01);
  }

  @Test(expected = FunctionEvaluationException.class)
  public void testOptimizeWithException() {
    // Test optimizing a function that throws a FunctionEvaluationException
    MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealPointValuePairOptimizer(), 5, new RandomGenerator());
    UnivariateRealFunction function = new UnivariateRealFunction() {
        public double value(double x) {
            throw new FunctionEvaluationException(x);
        }
    };
    optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
  }

  @Test(expected = ConvergenceException.class)
  public void testOptimizeWithConvergenceException() {
    // Test optimizing a function that throws a ConvergenceException
    MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealPointValuePairOptimizer(), 5, new RandomGenerator());
    UnivariateRealFunction function = new UnivariateRealFunction() {
        public double value(double x) {
            throw new ConvergenceException();
        }
    };
    optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
  }

  @Test
  public void testGetOptimaWithNoOptimumComputed() {
    // Test getting optima with no optimum computed yet
    MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealPointValuePairOptimizer(), 5, new RandomGenerator());
    assertThrows(MathIllegalStateException.class, () -> optimizer.getOptima());
  }

}