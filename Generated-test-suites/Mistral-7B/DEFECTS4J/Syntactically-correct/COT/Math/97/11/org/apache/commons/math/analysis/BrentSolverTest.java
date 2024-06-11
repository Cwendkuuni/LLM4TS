import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for BrentSolver.
 */
public class BrentSolverTest {

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
    assertEquals(Double.NaN, solver.getRoot(), 0.0);
  }

  /**
   * Test findRoot method with basic usage.
   */
  @Test
  public void testFindRootBasic() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    ComputableFunction function = new DummyFunction(1.0);
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertEquals(true, result);
    assertEquals(0.5, solver.getRoot(), 0.001);
  }

  /**
   * Test findRoot method with convergence at high point.
   */
  @Test
  public void testFindRootHigh() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.HIGH;
      }
    };
    ComputableFunction function = new DummyFunction(-1.0);
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertEquals(true, result);
    assertEquals(1.0, solver.getRoot(), 0.001);
  }

  /**
   * Test findRoot method with convergence at root.
   */
  @Test
  public void testFindRootRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    ComputableFunction function = new DummyFunction(0.0);
    double x0 = 0.5;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    assertEquals(true, result);
    assertEquals(x0, solver.getRoot(), 0.001);
  }

  /**
   * Test findRoot method with convergence failure.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFailure() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    ComputableFunction function = new DummyFunction(-1.0);
    double x0 = 0.0;
    double f0 = Double.NaN;
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    fail("FindRoot method should have thrown an exception.");
  }

  /**
   * Test findRoot method with interval width too small.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootTooSmall() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    ComputableFunction function = new DummyFunction(0.0);
    double x0 = 0.0;
    double f0 = 0.0;
    double x1 = x0;
    double f1 = function.valueAt(x1);
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    fail("FindRoot method should have thrown an exception.");
  }

  /**
   * Test findRoot method with function exception.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    ComputableFunction function = new DummyFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException();
      }
    };
    double x0 = 0.0;
    double f0 = Double.NaN;
    double x1 = 1.0;
    double f1 = Double.NaN;
    boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    fail("FindRoot method should have thrown an exception.");
  }

}