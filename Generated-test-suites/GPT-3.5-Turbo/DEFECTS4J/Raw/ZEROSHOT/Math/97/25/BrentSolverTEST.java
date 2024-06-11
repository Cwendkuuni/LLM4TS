import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow,
                           double xHigh, double fHigh) {
        if (fLow == 0) {
          return LOW;
        } else if (fHigh == 0) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    int maxIter = 1000;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);

    double expectedRoot = 5;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.00001);
  }
  
  @Test
  public void testFindRootZeroFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return 0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow,
                           double xHigh, double fHigh) {
        if (fLow == 0) {
          return LOW;
        } else if (fHigh == 0) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    int maxIter = 1000;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);

    double expectedRoot = 0;
    double actualRoot = solver.getRoot();
    assertEquals(expectedRoot, actualRoot, 0.00001);
  }
  
  @Test
  public void testFindRootNoRootInInterval() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x + 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow,
                           double xHigh, double fHigh) {
        if (fLow == 0) {
          return LOW;
        } else if (fHigh == 0) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    int maxIter = 1000;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
  }

}