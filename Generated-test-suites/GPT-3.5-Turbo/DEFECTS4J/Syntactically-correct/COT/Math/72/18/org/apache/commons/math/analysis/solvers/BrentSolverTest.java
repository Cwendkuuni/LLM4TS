import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow * fHigh < 0) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOTHING;
      }
    };

    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);

    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  // Additional test cases for edge cases and exception handling scenarios

  @Test(expected = FunctionException.class)
  public void testFunctionException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        throw new FunctionException("Function evaluation failed");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow * fHigh < 0) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOTHING;
      }
    };

    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }

  // Other JUnit test cases for other public methods in the BrentSolver class

}