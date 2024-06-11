import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

  // Test for findRoot() method

  // Test for typical use case
  @Test
  public void testFindRoot_typical() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fHigh) < 0.001) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    int maxIter = 100;
    double x0 = -10.0;
    double f0 = 96.0;
    double x1 = 10.0;
    double f1 = -76.0;
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  // Test for edge case
  @Test
  public void testFindRoot_edge() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fHigh) < 0.001) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    int maxIter = 100;
    double x0 = -Double.MAX_VALUE;
    double f0 = Double.MAX_VALUE * 0.5;
    double x1 = Double.MAX_VALUE;
    double f1 = Double.MAX_VALUE;
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  // Test for error scenario
  @Test(expected = FunctionException.class)
  public void testFindRoot_error() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Error in function");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fHigh) < 0.001) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
    int maxIter = 100;
    double x0 = -10.0;
    double f0 = 96.0;
    double x1 = 10.0;
    double f1 = -76.0;
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    // expecting an exception here
  }

  // Test for getRoot() method

  // Test for typical use case
  @Test
  public void testGetRoot_typical() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fHigh) < 0.001) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    }, 100, -10.0, 96.0, 10.0, -76.0);
    double root = solver.getRoot();
    assertEquals(2.0, root, 0.001);
  }

  // Test for edge case
  @Test
  public void testGetRoot_edge() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    }, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 0.001) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(fHigh) < 0.001) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    }, 100, -Double.MAX_VALUE, Double.MAX_VALUE * 0.5, Double.MAX_VALUE, Double.MAX_VALUE);
    double root = solver.getRoot();
    assertEquals(2.0, root, 0.001);
  }

  // Test for error scenario
  @Test
  public void testGetRoot_error() {
    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
          throw new FunctionException("Error in function");
        }
      }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
          if (Math.abs(xHigh - xLow) < 0.001) {
            return ConvergenceChecker.HIGH;
          } else if (Math.abs(fHigh) < 0.001) {
            return ConvergenceChecker.LOW;
          } else {
            return ConvergenceChecker.NOT_CONVERGED;
          }
        }
      }, 100, -10.0, 96.0, 10.0, -76.0);
      double root = solver.getRoot();
    } catch (FunctionException e) {
      // expecting an exception here
    }
  }

}