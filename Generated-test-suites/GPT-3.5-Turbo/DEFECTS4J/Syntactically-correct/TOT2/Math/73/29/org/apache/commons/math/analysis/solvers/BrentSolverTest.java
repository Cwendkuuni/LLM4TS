import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 16;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    try {
      assertTrue(solver.findRoot(function, checker, 100, 2.0, -12.0, 4.0, 0.0));
    } catch (FunctionException e) {
      fail("Exception thrown while finding root");
    }

    assertEquals(4.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 16;
      }
    }, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    }, 100, 2.0, -12.0, 4.0, 0.0);

    assertEquals(4.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootException() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        throw new RuntimeException("Error in function evaluation");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };

    try {
      solver.findRoot(function, checker, 100, 1.0, -15.0, 3.0, 1.0);
      fail("Exception not thrown when function evaluation failed");
    } catch (FunctionException e) {
      // Exception expected
    }
  }

}