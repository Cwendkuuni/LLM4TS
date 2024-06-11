import org.junit.*;
import static org.junit.Assert.*;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    double result = solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < epsilon) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.CONTINUE;
        }
      }
    }, 100, 0, -5, 10, 5);
    assertEquals(5.0, result, 0.0001);
  }
  
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x - 5;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < epsilon) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.CONTINUE;
        }
      }
    }, 100, 0, -5, 10, 5);
    double result = solver.getRoot();
    assertEquals(5.0, result, 0.0001);
  }

  @Test
  public void testFindRootWithException() {
    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
          if (x == 0) {
            throw new FunctionException("Cannot evaluate function at 0");
          }
          return x - 5;
        }
      }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
          if (Math.abs(fLow) < epsilon) {
            return ConvergenceChecker.LOW;
          } else if (Math.abs(fHigh) < epsilon) {
            return ConvergenceChecker.HIGH;
          } else {
            return ConvergenceChecker.CONTINUE;
          }
        }
      }, 100, 0, -5, 10, 5);
      fail("Expected FunctionException to be thrown");
    } catch (FunctionException e) {
      assertEquals("Cannot evaluate function at 0", e.getMessage());
    }
  }
  
}