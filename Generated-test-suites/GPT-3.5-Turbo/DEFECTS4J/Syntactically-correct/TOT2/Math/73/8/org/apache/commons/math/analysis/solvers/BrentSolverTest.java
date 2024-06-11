import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test case for findRoot method
    // Typical use case
    // Square root function x*x - 9, interval 2, 4, expected root 3
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = x -> x * x - 9;
    ConvergenceChecker checker = new ConvergenceChecker();
    int maxIter = 100;
    double x0 = 2;
    double f0 = function.valueAt(x0);
    double x1 = 4;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(3, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootEdgeCases() {
    // Test case for findRoot method
    // Edge case when the function has multiple roots in the interval
    // Cubic root function x*x*x - 27, interval 2, 4, expected root 3
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = x -> x * x * x - 27;
    ConvergenceChecker checker = new ConvergenceChecker();
    int maxIter = 100;
    double x0 = 2;
    double f0 = function.valueAt(x0);
    double x1 = 4;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(3, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootExceptions() {
    // Test case for findRoot method
    // Error scenario when the function throws an exception
    // Invalid function causing a FunctionException
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = x -> { throw new FunctionException(); };
    ConvergenceChecker checker = new ConvergenceChecker();
    int maxIter = 100;
    double x0 = 2;
    double f0 = function.valueAt(x0);
    double x1 = 4;
    double f1 = function.valueAt(x1);

    try {
      solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Exception caught, test passed
    }
  }

}