import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {

  private BrentSolver solver;
  
  @Before
  public void setup() {
    solver = new BrentSolver();
  }
  
  @Test
  public void testFindRoot_typicalCase() {
    // Typical case: a root exists in the given interval
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        // Sample function: f(x) = x^2 - 4
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Convergence criterion: When function value is less than 0.01, consider it converged
        if (Math.abs(fLow) < 0.01) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 0.01) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = -10.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    
    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double root = solver.getRoot();
    
    Assert.assertTrue(foundRoot);
    Assert.assertEquals(2.0, root, 0.01); // Expected root is 2.0
  }
  
  @Test
  public void testFindRoot_edgeCase1() {
    // Edge case: x0 and x1 are the same, with the root at that point
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        // Sample function: f(x) = x^2 - 4
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Convergence criterion: When function value is less than 0.01, consider it converged
        if (Math.abs(fLow) < 0.01) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 0.01) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = 2.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);
    
    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double root = solver.getRoot();
    
    Assert.assertTrue(foundRoot);
    Assert.assertEquals(2.0, root, 0.01); // Expected root is 2.0
  }
  
  @Test
  public void testFindRoot_edgeCase2() {
    // Edge case: x0 and x1 are the same, with no root in that point
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        // Sample function: f(x) = x^2 - 4
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Convergence criterion: When function value is less than 0.01, consider it converged
        if (Math.abs(fLow) < 0.01) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 0.01) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = 5.0;
    double f0 = function.valueAt(x0);
    double x1 = 5.0;
    double f1 = function.valueAt(x1);
    
    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    Assert.assertFalse(foundRoot); // No root is found
  }
  
  @Test
  public void testFindRoot_errorCase1() {
    // Error case: Unconverged root due to insufficient iterations
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        // Sample function: f(x) = 1/x
        if (x == 0) {
          throw new FunctionException("Cannot divide by zero");
        }
        return 1 / x;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Convergence criterion: When function value is less than 0.01, consider it converged
        if (Math.abs(fLow) < 0.01) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 0.01) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 5; // Insufficient iterations
    double x0 = -10.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    
    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    Assert.assertFalse(foundRoot); // No root is found
  }
  
  @Test
  public void testFindRoot_errorCase2() {
    // Error case: Unconverged root due to non-existent root in the given interval
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        // Sample function: f(x) = x^2 - 4
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Convergence criterion: When function value is less than 0.01, consider it converged
        if (Math.abs(fLow) < 0.01) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 0.01) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    int maxIter = 100;
    double x0 = 10.0;
    double f0 = function.valueAt(x0);
    double x1 = 20.0;
    double f1 = function.valueAt(x1);
    
    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    Assert.assertFalse(foundRoot); // No root is found
  }

}