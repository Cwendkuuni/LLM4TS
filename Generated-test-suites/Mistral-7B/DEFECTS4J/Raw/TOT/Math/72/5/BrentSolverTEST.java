import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.SineFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SineFunction sineFunction;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < epsilon) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    sineFunction = new SineFunction();
  }

  @Test
  public void testFindRootInInterval() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double expectedRoot = 2.0;

    boolean result = solver.findRoot(sineFunction, checker, 100, x0, sineFunction.valueAt(x0), x1, sineFunction.valueAt(x1));

    assertEquals(expectedRoot, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootAtZero() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double expectedRoot = 0.0;

    boolean result = solver.findRoot(sineFunction, checker, 100, x0, sineFunction.valueAt(x0), x1, sineFunction.valueAt(x1));

    assertEquals(expectedRoot, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFail() throws FunctionException {
    double x0 = 1.0;
    double x1 = 0.0;
    double expectedRoot = 1.0; // this interval does not contain a root

    boolean result = solver.findRoot(sineFunction, checker, 100, x0, sineFunction.valueAt(x0), x1, sineFunction.valueAt(x1));

    // the test should fail because the function does not find a root in this interval
  }

  @Test
  public void testFindRootWithNonMonotonicFunction() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;

    boolean result = solver.findRoot(new FunctionConstants().square(), checker, 100, x0, Math.pow(x0, 2), x1, Math.pow(x1, 2));

    assertEquals(expectedRoot, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFunctionException() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = null;
    ComputableFunction function = null;

    solver.findRoot(function, checker, 100, 0.0, 1.0, 1.0, 1.0);

    // the test should fail because the checker is null
  }

  @Test(expected = FunctionException.class)
  public void testNullFunctionValueException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    ComputableFunction function = () -> { throw new FunctionException(); };

    solver.findRoot(function, checker, 100, 0.0, Double.NaN, 1.0, Double.NaN);

    // the test should fail because the function throws an exception
  }
}