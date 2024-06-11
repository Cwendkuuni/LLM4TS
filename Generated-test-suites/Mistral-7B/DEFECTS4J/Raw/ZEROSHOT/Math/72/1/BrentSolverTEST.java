import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private RationalFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < Math.abs(f2)) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < Math.abs(f1)) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.EQUAL;
        }
      }
    };
    function = new RationalFunction("x^3 - 4 * x^2 + 4 * x - 2");
  }

  @Test
  public void testFindRootWithSimpleFunction() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;

    assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(1.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithComplexFunction() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    int maxIter = 100;

    assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(1.5, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithFunctionNotConverged() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    int maxIter = 1;

    assertFalse(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }
}