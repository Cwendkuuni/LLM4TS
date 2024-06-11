import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver brentSolver;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
  }

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return 2 * x - 5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(fHigh) < 1e-6) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    };
    int maxIter = 100;
    double x0 = 1;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    boolean rootFound = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(2.5, brentSolver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRootNoRootInInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x + 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    int maxIter = 100;
    double x0 = 1;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    boolean rootFound = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(rootFound);
  }
}