import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_BasicFunctionality() {
    // Given
    ComputableFunction function = new MockComputableFunction();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NO_CONVERGENCY;
      }
    };
    BrentSolver solver = new BrentSolver();

    // When
    boolean result = solver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 1.0);

    // Then
    Assert.assertTrue(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_ThrowsFunctionException() {
    // Given
    ComputableFunction function = new MockComputableFunctionWithException();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NO_CONVERGENCY;
      }
    };
    BrentSolver solver = new BrentSolver();

    // When
    solver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 1.0);

    // Then (the test should fail with an exception)
  }

  @Test
  public void testGetRoot_ReturnsRootValue() {
    // Given
    ComputableFunction function = new MockComputableFunction();
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.LOW;
      }
    }, 100, 0.0, 1.0, 1.0, 1.0);

    // When
    double root = solver.getRoot();

    // Then
    Assert.assertNotNull(root);
  }

  // Add more test cases here for edge cases and exception handling

  // Example of an edge case test: testFindRoot_RootAtEndpoint()
  @Test
  public void testFindRoot_RootAtEndpoint() {
    // Given
    ComputableFunction function = new MockComputableFunctionWithRootAtEndpoint();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NO_CONVERGENCY;
      }
    };
    BrentSolver solver = new BrentSolver();

    // When
    boolean result = solver.findRoot(function, checker, 100, 0.0, 1.0, 0.0, 0.0); // root is at endpoint

    // Then
    Assert.assertTrue(result);
  }

  // Example of a test case for an exception: testFindRoot_FunctionException()
  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() {
    // Given
    ComputableFunction function = new MockComputableFunctionWithException();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NO_CONVERGENCY;
      }
    };
    BrentSolver solver = new BrentSolver();

    // When
    solver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 1.0);

    // Then (the test should fail with an exception)
  }

  // Add more test cases here for other edge cases and exception handling scenarios

}

// Additional classes for test cases
class MockComputableFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.pow(x, 2);
  }
}

class MockComputableFunctionWithRootAtEndpoint implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    if (x == 0.0) {
      throw new AssertionError("Root is at endpoint");
    }
    return Math.pow(x, 2);
  }
}

class MockComputableFunctionWithException implements ComputableFunction {
  @Override
  public double valueAt(double x) throws FunctionException {
    throw new FunctionException("Function exception");
  }
}