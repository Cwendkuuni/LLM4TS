
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        }
        if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        return UNDEFINED;
      }
    };
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, 0, -4, 0, 4);
    assert rootFound;
    double root = solver.getRoot();
    assert Math.abs(root - 2) < 1e-6;
  }
  
  @Test
  public void testFindRootWithNegativeInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        }
        if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        return UNDEFINED;
      }
    };
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, -4, 2, -2, -4);
    assert rootFound;
    double root = solver.getRoot();
    assert Math.abs(root - (-2)) < 1e-6;
  }
  
  @Test
  public void testFindRootWithInvalidInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return LOW;
        }
        if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        }
        return UNDEFINED;
      }
    };
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, 0, -4, 0, -4);
    assert !rootFound;
  }

}

