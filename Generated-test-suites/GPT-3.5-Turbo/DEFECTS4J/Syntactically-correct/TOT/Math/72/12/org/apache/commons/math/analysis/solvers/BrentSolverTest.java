import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRootTypical() throws Exception {
        BrentSolver solver = new BrentSolver();
        double epsilon = 1e-6;
        
        // Create a simple function: f(x) = x^2 - 4
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x*x - 4;
            }
        };
        
        // Test finding root in interval (-5, -3)
        double x0 = -5;
        double x1 = -3;
        double f0 = function.valueAt(x0);
        double f1 = function.valueAt(x1);

        assertTrue("Root not found", solver.findRoot(function, ConvergenceChecker.DEFAULT, 100, x0, f0, x1, f1));
        assertTrue("Incorrect root value", Math.abs(solver.getRoot() - (-2)) <= epsilon);
    }
    
    @Test
    public void testFindRootNoRoot() throws Exception {
        BrentSolver solver = new BrentSolver();
        double epsilon = 1e-6;
        
        // Create a simple function: f(x) = x^2 + 1
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x*x + 1;
            }
        };
        
        // Test finding root in interval (-5, -3)
        double x0 = -5;
        double x1 = -3;
        double f0 = function.valueAt(x0);
        double f1 = function.valueAt(x1);

        assertFalse("Root found when no root exists", solver.findRoot(function, ConvergenceChecker.DEFAULT, 100, x0, f0, x1, f1));
    }
    
    @Test
    public void testFindRootEdge() throws Exception {
        BrentSolver solver = new BrentSolver();
        double epsilon = 1e-6;
        
        // Create a simple function: f(x) = x^2 + 1
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x*x + 1;
            }
        };
        
        // Test finding root in interval (0, 1)
        double x0 = 0;
        double x1 = 1;
        double f0 = function.valueAt(x0);
        double f1 = function.valueAt(x1);

        assertFalse("Root found when no root exists", solver.findRoot(function, ConvergenceChecker.DEFAULT, 100, x0, f0, x1, f1));
        
        // Test finding root at edge of interval (1, 3)
        x0 = 1;
        x1 = 3;
        f0 = function.valueAt(x0);
        f1 = function.valueAt(x1);
        
        assertTrue("Root not found", solver.findRoot(function, ConvergenceChecker.DEFAULT, 100, x0, f0, x1, f1));
        assertTrue("Incorrect root value", Math.abs(solver.getRoot() - Math.sqrt(3)) <= epsilon);
    }
    
    @Test
    public void testFindRootError() throws Exception {
        BrentSolver solver = new BrentSolver();
        
        // Create a simple function: f(x) = 1/x
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return 1/x;
            }
        };
        
        // Set lower bound higher than upper bound
        double x0 = 5;
        double x1 = 1;
        double f0 = function.valueAt(x0);
        double f1 = function.valueAt(x1);
        
        try {
            solver.findRoot(function, ConvergenceChecker.DEFAULT, 100, x0, f0, x1, f1);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
        
    }
}