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
        return x * x - 2.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < Math.abs(fHigh))
          return ConvergenceChecker.LOW;
        else if (Math.abs(fHigh) < Math.abs(fLow))
          return ConvergenceChecker.HIGH;
        else
          return ConvergenceChecker.NONE;
      }
    };

    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    boolean foundRoot = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(foundRoot);

    double root = brentSolver.getRoot();
    assertEquals(Math.sqrt(2.0), root, 1e-10);
  }

  @Test
  public void testGetRoot() {
    double expectedRoot = Double.NaN;
    double actualRoot = brentSolver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.0);
  }

}