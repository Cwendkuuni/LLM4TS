import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SimpleFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < 1e-6) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NEITHER;
      }
    };
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
  }

  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    double x0 = -3.0;
    double x1 = 3.0;
    int maxIter = 50;

    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(-1.0, solver.getRoot(), 1e-6);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    double x0 = -3.0;
    double x1 = 3.0;
    int maxIter = 50;

    SimpleFunction functionThrowingException = new SimpleFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x < 0) {
          throw new FunctionException("Function not defined for negative x.");
        }
        return x * x - 2;
      }
    };

    solver.findRoot(functionThrowingException, checker, maxIter, x0, functionThrowingException.valueAt(x0), x1, functionThrowingException.valueAt(x1));
  }

  @Test
  public void testFindRootConvergenceFailure() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 5;

    SimpleFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) / x;
      }
    };

    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(Double.NaN, solver.getRoot(), 1e-6);
  }
}