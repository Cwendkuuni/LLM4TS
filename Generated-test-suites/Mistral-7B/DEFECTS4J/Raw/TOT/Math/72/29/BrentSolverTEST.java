import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.001) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    function = FunctionFactory.createSinFunction();
  }

  @Test
  public void testFindRootSimple() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = Math.PI;
    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 0.01);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFail() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    solver.findRoot(new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new UnsupportedOperationException();
      }
    }, checker, 10, x0, 0.0, x1, 0.0);
  }

  @Test
  public void testFindRootEdgeCases() throws FunctionException {
    double x0 = 0.0;
    double x1 = 0.0;
    double root = 0.0;
    solver.findRoot(function, checker, 10, x0, 0.0, x1, 0.0);
    assertEquals(root, solver.getRoot(), 0.01);

    x0 = Double.POSITIVE_INFINITY;
    x1 = Double.NEGATIVE_INFINITY;
    solver.findRoot(function, checker, 10, x0, Double.POSITIVE_INFINITY, x1, Double.NEGATIVE_INFINITY);
    assertEquals(Double.NaN, solver.getRoot());
  }

}