import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    
    // Test case 1: Basic functionality test
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) {
            return x*x - 4;
        }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
        public int converged(double x1, double f1, double x2, double f2) {
            return (Math.abs(f1) < 1e-9) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
        }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);
    
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 1e-9);
    
    // Test case 2: Edge case for infinite loop
    function = new ComputableFunction() {
        public double valueAt(double x) {
            return Double.NaN;
        }
    };
    assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    
    // Test case 3: Edge case for no root in the interval
    function = new ComputableFunction() {
        public double valueAt(double x) {
            return x*x + 4;
        }
    };
    assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    
    // Test case 4: Edge case for lower bound is root
    function = new ComputableFunction() {
        public double valueAt(double x) {
            return x;
        }
    };
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(x0, solver.getRoot(), 1e-9);
    
    // Test case 5: Edge case for higher bound is root
    function = new ComputableFunction() {
        public double valueAt(double x) {
            return x - x1;
        }
    };
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(x1, solver.getRoot(), 1e-9);
    
    // Test case 6: Exception handling test
    function = new ComputableFunction() {
        public double valueAt(double x) {
            throw new FunctionException();
        }
    };
    checker = new ConvergenceChecker() {
        public int converged(double x1, double f1, double x2, double f2) {
            throw new FunctionException();
        }
    };
    try {
        solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        fail("An exception should be thrown");
    } catch (FunctionException e) {
        // expected exception
    }
  }
}