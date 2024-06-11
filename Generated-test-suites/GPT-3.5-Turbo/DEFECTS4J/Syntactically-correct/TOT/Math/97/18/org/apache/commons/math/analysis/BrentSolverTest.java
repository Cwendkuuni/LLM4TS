import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        // Function implementation
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Convergence checker implementation
      }
    };
  }

  @Test
  public void testFindRoot_TypicalCase() throws FunctionException {
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_LowerBoundRoot() throws FunctionException {
    double x0 = -10.0;
    double f0 = function.valueAt(x0);
    double x1 = -5.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_HigherBoundRoot() throws FunctionException {
    double x0 = 5.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_NoRootInInterval() throws FunctionException {
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

    assertFalse(result);
    assertTrue(Double.isNaN(solver.getRoot()));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() throws FunctionException {
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }

}