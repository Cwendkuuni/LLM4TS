import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) { return x - 2; }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (xLow > xHigh) return ConvergenceChecker.HIGH;
        else if (xLow < xHigh) return ConvergenceChecker.LOW;
        else return ConvergenceChecker.NONE;
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = -2;
    double x1 = 4;
    double f1 = 2;

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 0.0001);
  }

  @Test
  public void testFindRootNoSolution() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) { return x - 2; }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (xLow > xHigh) return ConvergenceChecker.HIGH;
        else if (xLow < xHigh) return ConvergenceChecker.LOW;
        else return ConvergenceChecker.NONE;
      }
    };
    int maxIter = 10;
    double x0 = 0;
    double f0 = -2;
    double x1 = 4;
    double f1 = 2;

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
  }

  @Test
  public void testFindRootFunctionException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function error");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (xLow > xHigh) return ConvergenceChecker.HIGH;
        else if (xLow < xHigh) return ConvergenceChecker.LOW;
        else return ConvergenceChecker.NONE;
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = -2;
    double x1 = 4;
    double f1 = 2;

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) { return x - 2; }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (xLow > xHigh) return ConvergenceChecker.HIGH;
        else if (xLow < xHigh) return ConvergenceChecker.LOW;
        else return ConvergenceChecker.NONE;
      }
    }, 100, 0, -2, 4, 2);

    assertEquals(2.0, solver.getRoot(), 0.0001);
  }

}