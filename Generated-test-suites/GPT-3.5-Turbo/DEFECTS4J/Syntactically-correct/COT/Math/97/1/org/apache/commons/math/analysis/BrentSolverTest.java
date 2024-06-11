import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    int maxIter = 1000;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    // assertion for root value
    assert Math.abs(Math.sin(solver.getRoot())) < 1e-6;
    // assertion for success of finding root
    assert result;
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();

    // assertion for default root value
    assert Double.isNaN(solver.getRoot());
  }
}