import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  // Edge case: Empty function, should return false
  @Test
  public void testEmptyFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return 0.0; // empty function returns 0 at all points
      }
    };
    ConvergenceChecker checker = new DefaultConvergenceChecker();

    assertFalse(solver.findRoot(function, checker, 100, -1, 0, 1, 1));
  }

  // Typical use case: Linear function with a root at 1.0, should return true
  @Test
  public void testLinearFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x - 1.0; // linear function with root at 1.0
      }
    };
    ConvergenceChecker checker = new DefaultConvergenceChecker();

    assertTrue(solver.findRoot(function, checker, 100, 0, -1, 2, 1));
    assertEquals(1.0, solver.getRoot(), 1e-10);
  }

  // Error scenario: Non-converging function, should return false
  @Test
  public void testNonConvergingFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x; // non-converging function
      }
    };
    ConvergenceChecker checker = new DefaultConvergenceChecker();

    assertFalse(solver.findRoot(function, checker, 5, -1, 1, 2, 4));
  }

  // Edge case: Function with multiple roots, should return the first root
  @Test
  public void testMultipleRoots() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        if (x < 0) {
          return -1; // root at x = -1
        } else if (x > 0) {
          return 1; // root at x = 1
        } else {
          return 0; // non-root
        }
      }
    };
    ConvergenceChecker checker = new DefaultConvergenceChecker();

    assertTrue(solver.findRoot(function, checker, 100, -2, -1, 2, 1));
    assertEquals(-1.0, solver.getRoot(), 1e-10);
  }

  // Typical use case: Sine function with a root at pi, should return true
  @Test
  public void testSineFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return Math.sin(x); // sine function with root at pi
      }
    };
    ConvergenceChecker checker = new DefaultConvergenceChecker();

    assertTrue(solver.findRoot(function, checker, 100, 3, -1, 4, 1));
    assertEquals(Math.PI, solver.getRoot(), 1e-10);
  }

}