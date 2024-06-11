import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Create a dummy ComputableFunction for testing
    ComputableFunction dummyFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 5;
      }
    };

    // Create a dummy ConvergenceChecker for testing
    ConvergenceChecker dummyChecker = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        if (f0 == 0) {
          return ConvergenceChecker.LOW;
        } else if (f1 == 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(dummyFunction, dummyChecker, 100, 2.0, 1.0, 3.0, 10.0);
    assertTrue(rootFound);
    assertEquals(Math.sqrt(5), solver.getRoot(), 1e-10);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.root = 3.0;
    assertEquals(3.0, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRootWithNegativeValues() {
    ComputableFunction dummyFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return x + 5;
      }
    };

    ConvergenceChecker dummyChecker = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        if (f0 == 0) {
          return ConvergenceChecker.LOW;
        } else if (f1 == 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(dummyFunction, dummyChecker, 100, -4.0, 1.0, -2.0, 10.0);
    assertTrue(rootFound);
    assertEquals(-5.0, solver.getRoot(), 1e-10);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithFunctionException() {
    ComputableFunction dummyFunction = new ComputableFunction() {
      public double valueAt(double x) {
        throw new FunctionException("Error in function");
      }
    };

    ConvergenceChecker dummyChecker = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return ConvergenceChecker.NONE;
      }
    };

    BrentSolver solver = new BrentSolver();
    solver.findRoot(dummyFunction, dummyChecker, 100, -4.0, 1.0, -2.0, 10.0);
  }

}