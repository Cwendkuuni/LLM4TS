import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver solver = new BrentSolver();

  @Test
  public void testFindRoot() throws FunctionException {
    // Typical use case
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLower, double fLower, double xHigher, double fHigher) {
        if (Math.abs(fLower) <= 0.01) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigher) <= 0.01) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };

    int maxIter = 100;
    double x0 = 0.9;
    double f0 = function.valueAt(x0);
    double x1 = 4.1;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRootNegativeRange() throws FunctionException {
    // Edge case: Negative range
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLower, double fLower, double xHigher, double fHigher) {
        if (Math.abs(fLower) <= 0.01) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigher) <= 0.01) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };

    int maxIter = 100;
    double x0 = -4.1;
    double f0 = function.valueAt(x0);
    double x1 = -0.9;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(-2.0, solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRootZeroFunctionValue() throws FunctionException {
    // Edge case: Function value is zero
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 2;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLower, double fLower, double xHigher, double fHigher) {
        if (Math.abs(fLower) <= 0.01) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigher) <= 0.01) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };

    int maxIter = 100;
    double x0 = 0.9;
    double f0 = function.valueAt(x0);
    double x1 = 4.1;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRootNoConvergence() throws FunctionException {
    // Error scenario: No convergence
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLower, double fLower, double xHigher, double fHigher) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    int maxIter = 100;
    double x0 = 0.9;
    double f0 = function.valueAt(x0);
    double x1 = 4.1;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(rootFound);
    assertEquals(Double.NaN, solver.getRoot(), 0.01);
  }

}