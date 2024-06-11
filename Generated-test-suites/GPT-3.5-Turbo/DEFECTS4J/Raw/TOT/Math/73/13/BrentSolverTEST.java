import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot_typicalUseCase() throws FunctionException {
    // typical use case with a function that has a root in the given interval
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        return (Math.abs(f1) < 1e-6) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, -10, 96, 4, -16);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_edgeCase() throws FunctionException {
    // edge case with a function that has a root at one of the bounds of the interval
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        return (Math.abs(f2) < 1e-6) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, -10, 96, 4, -4);
    assertTrue(rootFound);
    assertEquals(-2.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testFindRoot_errorScenario() throws FunctionException {
    // error scenario with invalid function that throws exception
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function evaluation failed");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      boolean rootFound = solver.findRoot(function, checker, 100, -10, 96, 4, -16);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected exception
    }
  }

}