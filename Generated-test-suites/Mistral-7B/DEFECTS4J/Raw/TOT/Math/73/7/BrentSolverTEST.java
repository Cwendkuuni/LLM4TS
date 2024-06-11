import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SimpleComputableFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double fx1, double x2, double fx2) {
        if (Math.abs(fx1) < 0.001 && Math.abs(fx2) < 0.001) {
          return ConvergenceChecker.BOTH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    function = new SimpleComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };
  }

  @Test
  public void testFindRoot_FunctionWithRootInInterval() throws FunctionException {
    double x0 = -1.0;
    double x1 = 1.0;
    double root = 0.0;
    assertTrue(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithNoRootInInterval() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    assertFalse(solver.findRoot(new SimpleComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    }, checker, 100, x0, 0.0, x1, Math.sin(x1)));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionWithError() throws FunctionException {
    double x0 = 0.0;
    double x1 = 0.0;
    double root = Double.NaN;
    assertFalse(solver.findRoot(new SimpleComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new ArithmeticException("Division by zero");
      }
    }, checker, 100, x0, 0.0, x1, 0.0));
    assertNull(solver.getRoot());
  }

  @Test
  public void testFindRoot_FunctionWithMultipleRoots() throws FunctionException {
    double x0 = -2.0;
    double x1 = 2.0;
    double root1 = -1.0;
    double root2 = 1.0;
    double foundRoot = solver.findRoot(new SimpleComputableFunction() {
      @Override
      public double valueAt(double x) {
        double result = Math.pow(x, 2) - 4;
        if (result > 0) {
          return result;
        } else {
          return 0;
        }
      }
    }, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)).getRoot();
    assertEquals(root1, foundRoot, 0.001);
    // Test if the other root is also found
    solver.reset();
    assertTrue(solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0)));
    assertEquals(root2, solver.getRoot(), 0.001);
  }

}