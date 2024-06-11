import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

  private static final double PRECISION = 1e-6;

  private BrentOptimizer optimizer;

  @Before
  public void setup() {
    // Create a new optimizer instance with default convergence criteria
    optimizer = new BrentOptimizer(1e-6, 1e-6);
  }

  @Test
  public void testOptimize_MinimizeFunction() {
    // Define a simple quadratic function to be minimized:  f(x) = (x - 2)^2
    UnivariateFunction function = new UnivariateFunction() {
      public double value(double x) {
        return Math.pow(x - 2, 2);
      }
    };

    // Set the objective function and range for optimization
    optimizer.optimize(function, GoalType.MINIMIZE, 0, 5);

    // Check that the optimizer has converged to the known minimum at x=2
    UnivariatePointValuePair result = optimizer.getResult();
    assertEquals(2.0, result.getPoint(), PRECISION);
  }

  @Test
  public void testOptimize_MaximizeFunction() {
    // Define a simple quadratic function to be maximized:  f(x) = -(x - 2)^2
    UnivariateFunction function = new UnivariateFunction() {
      public double value(double x) {
        return -Math.pow(x - 2, 2);
      }
    };

    // Set the objective function and range for optimization
    optimizer.optimize(function, GoalType.MAXIMIZE, 0, 5);

    // Check that the optimizer has converged to the known maximum at x=2
    UnivariatePointValuePair result = optimizer.getResult();
    assertEquals(2.0, result.getPoint(), PRECISION);
  }

  @Test
  public void testOptimize_FunctionNotConverging() {
    // Define a function that does not converge within given range
    UnivariateFunction function = new UnivariateFunction() {
      public double value(double x) {
        return x;
      }
    };

    // Set the objective function and range for optimization
    optimizer.optimize(function, GoalType.MINIMIZE, 0, 5);

    // Check that the optimizer has not converged
    assertTrue(optimizer.getResult() == null);
  }

  @Test
  public void testOptimize_FunctionThrowsException() {
    // Define a function that throws a runtime exception
    UnivariateFunction function = new UnivariateFunction() {
      public double value(double x) {
        throw new RuntimeException("Function evaluation error");
      }
    };

    // Set the objective function and range for optimization
    try {
      optimizer.optimize(function, GoalType.MINIMIZE, 0, 5);

      // Expecting an exception to be thrown
      fail("Exception not thrown");
    } catch (OptimizationException e) {
      // Check that the exception message is correct
      assertEquals("Function evaluation error", e.getMessage());
    }
  }
}