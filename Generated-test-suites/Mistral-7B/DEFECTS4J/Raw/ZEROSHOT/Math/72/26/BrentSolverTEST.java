import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionsTest;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        double x = (xHigh + xLow) / 2.0;
        double f = FunctionsTest.sin(x) * Math.cos(x);
        if (Math.abs(f) < 1e-8) {
          return ConvergenceChecker.BOTH;
        }
        if (Math.signum(f) == Math.signum(fLow)) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.HIGH;
      }
    };
    function = FunctionsTest.sinCos;
  }

  @Test
  public void testBrentSolver() throws FunctionException {
    double x0 = 0.0;
    double x1 = Math.PI / 2.0;
    double root = Math.PI / 4.0;

    boolean result = solver.findRoot(function, checker, 100, x0, FunctionsTest.sin(x0), x1, FunctionsTest.sin(x1));
    assertEquals(root, solver.getRoot(), 1e-8);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testBrentSolverNoRootInInterval() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    solver.findRoot(FunctionsTest.zeros, checker, 100, x0, FunctionsTest.zeros(x0), x1, FunctionsTest.zeros(x1));
  }

  @Test(expected = FunctionException.class)
  public void testBrentSolverFunctionException() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        throw new FunctionException("Test exception");
      }
    };
    solver.findRoot(FunctionsTest.sinCos, checker, 100, x0, FunctionsTest.sin(x0), x1, FunctionsTest.sin(x1));
  }

}