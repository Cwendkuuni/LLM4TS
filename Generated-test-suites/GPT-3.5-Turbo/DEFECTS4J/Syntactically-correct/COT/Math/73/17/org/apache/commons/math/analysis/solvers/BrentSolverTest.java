import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;  // example function
        }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-6) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-6) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -4.0;
    double x1 = 10.0;
    double f1 = 96.0;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }
  
  // additional test cases for edge cases and exceptions
  
  @Test
  public void testFindRoot_withZeroInterval() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;  // example function
        }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-6) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-6) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    };
    int maxIter = 100;
    double x0 = 2.0;
    double f0 = 0.0;
    double x1 = 2.0;
    double f1 = 0.0;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
    assertEquals(Double.NaN, solver.getRoot(), 1e-6);
  }
  
  @Test(expected = FunctionException.class)
  public void testFindRoot_withInvalidFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
            throw new FunctionException("Function is not defined");
        }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-6) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-6) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -4.0;
    double x1 = 10.0;
    double f1 = 96.0;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }
  
  @Test
  public void testFindRoot_withMaxIterations() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;  // example function
        }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-6) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-6) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    };
    int maxIter = 10;
    double x0 = 0.0;
    double f0 = -4.0;
    double x1 = 10.0;
    double f1 = 96.0;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
    assertEquals(Double.NaN, solver.getRoot(), 1e-6);
  }
}