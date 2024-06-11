import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Typical use case
    ComputableFunction function = new TestFunction();
    ConvergenceChecker checker = new TestChecker();
    int maxIter = 100;
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    BrentSolver solver = new BrentSolver();
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRootWithInvalidMaxIter() {
    // Error scenario - Invalid maximum iteration
    ComputableFunction function = new TestFunction();
    ConvergenceChecker checker = new TestChecker();
    int maxIter = -1; // Invalid maximum iteration
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    BrentSolver solver = new BrentSolver();

    try {
      solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      fail(); // Test should fail if no exception is thrown
    } catch (FunctionException e) {
      assertEquals("Invalid maximum iteration: " + maxIter, e.getMessage());
    }
  }

  @Test
  public void testFindRootWithInvalidInterval() {
    // Error scenario - Invalid interval
    ComputableFunction function = new TestFunction();
    ConvergenceChecker checker = new TestChecker();
    int maxIter = 100;
    double x0 = 1.0; // Invalid interval
    double f0 = function.valueAt(x0);
    double x1 = -1.0; // Invalid interval
    double f1 = function.valueAt(x1);

    BrentSolver solver = new BrentSolver();

    try {
      solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      fail(); // Test should fail if no exception is thrown
    } catch (FunctionException e) {
      assertEquals("Invalid interval: [" + x0 + ", " + x1 + "]", e.getMessage());
    }
  }

  @Test
  public void testFindRootWithUnconvergingFunction() {
    // Error scenario - Unconverging function
    ComputableFunction function = new UnconvergingFunction();
    ConvergenceChecker checker = new TestChecker();
    int maxIter = 100;
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    BrentSolver solver = new BrentSolver();

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
    assertEquals(Double.NaN, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRootWithConvergingFunctionWithInfiniteRoot() {
    // Edge case - Converging function with infinite root
    ComputableFunction function = new InfiniteRootFunction();
    ConvergenceChecker checker = new TestChecker();
    int maxIter = 100;
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    BrentSolver solver = new BrentSolver();

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(Double.POSITIVE_INFINITY, solver.getRoot(), 1e-6);
  }

  // Additional test methods for other class methods...

}