import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver() throws Exception {

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        double err = Math.abs(xHigh - xLow);
        if (err < epsilon && Math.abs(fLow) < Double.MIN_VALUE) {
          return ConvergenceChecker.LOW;
        } else if (err < epsilon && Math.abs(fHigh) < Double.MIN_VALUE) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.UNDECIDED;
        }
      }
    };

    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;

    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertFalse(Double.isNaN(solver.getRoot()));
    Assert.assertEquals(1.0, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFunctionException() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x < 0) {
          throw new FunctionException("Function is not defined for negative x value");
        }
        return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.UNDECIDED;
      }
    };

    double x0 = -1.0;
    double x1 = 1.0;
    int maxIter = 100;

    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }
}