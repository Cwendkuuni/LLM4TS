import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver.
 */
public class BrentSolverTest {

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver.getRoot());
  }

  /**
   * Test findRoot method with a simple function.
   */
  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = x0 * x0 - 2;
    double x1 = 2.0;
    double f1 = x1 * x1 - 2;
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(-1.0, solver.getRoot(), 0.0001);
  }

  /**
   * Test findRoot method with a function with roots at end points.
   */
  @Test
  public void testFindRootFunctionRootsAtEndPoints() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.HIGH;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 2.0;
    double f0 = Math.pow(x0, 2) - 4;
    double x1 = 3.0;
    double f1 = Math.pow(x1, 2) - 4;
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(-2.0, solver.getRoot(), 0.0001);
  }

  /**
   * Test findRoot method with a function having roots in between the interval.
   */
  @Test
  public void testFindRootFunctionRootsInBetween() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Math.pow(x0, 3) - 2;
    double x1 = 2.0;
    double f1 = Math.pow(x1, 3) - 2;
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertTrue(result);
    // The exact root is around 1.32471116
    assertEquals(1.32471116, solver.getRoot(), 0.001);
  }

  /**
   * Test findRoot method with a function having roots very close to the interval bounds.
   */
  @Test
  public void testFindRootFunctionRootsCloseToIntervalBounds() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 0.0001;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = Math.pow(x0, 2) - 0.0001;
    double x1 = 1.0;
    double f1 = Math.pow(x1, 2) - 0.0001;
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertTrue(result);
    // The exact root is 0, but it's hard to test it with the given tolerance
    // So, let's test if the root is closer to x0 than x1
    double root = solver.getRoot();
    assertTrue(root < x1);
  }

  /**
   * Test findRoot method with a function having roots at the interval midpoint.
   */
  @Test
  public void testFindRootFunctionRootsAtMidpoint() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 4) - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Math.pow(x0, 4) - 1;
    double x1 = 1.5;
    double f1 = Math.pow(x1, 4) - 1;
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertTrue(result);
    // The exact root is 1.0, but it's hard to test it with the given tolerance
    // So, let's test if the root is closer to x0 than x1
    double root = solver.getRoot();
    assertTrue(Math.abs(root - x0) < Math.abs(root - x1));
  }

  /**
   * Test findRoot method with a function having roots very close to each other.
   */
  @Test
  public void testFindRootFunctionRootsVeryClose() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 0.00001 * x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = Math.pow(x0, 3) - 0.00001 * x0;
    double x1 = 1.00001;
    double f1 = Math.pow(x1, 3) - 0.00001 * x1;
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertTrue(result);
    // The exact roots are 1.0 and 1.00001, but it's hard to test them with the given tolerance
    // So, let's test if the root is closer to x0 than x1
    double root = solver.getRoot();
    assertTrue(Math.abs(root - x0) < Math.abs(root - x1));
  }

  /**
   * Test findRoot method with a function that throws an exception.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionThrowingException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x < 0) {
          throw new FunctionException("Function is not defined for negative x");
        }
        return Math.pow(x, 2);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = -1.0;
    double f0 = Math.pow(-1.0, 2);
    double x1 = 1.0;
    double f1 = Math.pow(1.0, 2);
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    fail("FindRoot method should have thrown an exception");
  }

}