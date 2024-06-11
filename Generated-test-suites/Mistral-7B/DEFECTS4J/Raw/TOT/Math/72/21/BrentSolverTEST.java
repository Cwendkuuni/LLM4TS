import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.TestFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
  }

  @Test
  public void testFindRoot_simple() throws FunctionException {
    ComputableFunction function = TestFunctions.SIMPLE_FUNCTION;
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_roots_close() throws FunctionException {
    ComputableFunction function = TestFunctions.SIMPLE_FUNCTION;
    double x0 = 0.5;
    double x1 = 1.5;
    double root = 1.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_roots_far() throws FunctionException {
    ComputableFunction function = TestFunctions.SIMPLE_FUNCTION;
    double x0 = 0.1;
    double x1 = 4.0;
    double root = 2.0;

    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_division_by_zero() throws FunctionException {
    ComputableFunction function = TestFunctions.DIVISION_BY_ZERO_FUNCTION;
    double x0 = 0.1;
    double x1 = 1.0;
    double root = 0.5;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}