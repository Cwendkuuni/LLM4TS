import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;
  private final int maxIter = 100;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x0, double f0, double x1, double f1) {
        if (Math.abs(x1 - x0) < 1e-5) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
  }

  @Test
  public void testFindRoot_typicalCase() throws FunctionException {
    assertTrue(solver.findRoot(function, checker, maxIter, 1.0, -3.0, 4.0, 12.0));
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_edgeCase() throws FunctionException {
    assertTrue(solver.findRoot(function, checker, maxIter, 0.0, -4.0, 5.0, 16.0));
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_errorCase() throws FunctionException {
    assertFalse(solver.findRoot(function, checker, maxIter, 0.0, 4.0, 5.0, 16.0));
    assertEquals(Double.NaN, solver.getRoot(), 1e-6);
  }

}