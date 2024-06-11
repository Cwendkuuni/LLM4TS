import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot_typical() {
        // Typical use case: function with a root
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x);
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return HIGH;
                } else {
                    return ON_GOING;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = -Math.PI / 2;
        double f0 = function.valueAt(x0);
        double x1 = Math.PI / 2;
        double f1 = function.valueAt(x1);
        int maxIter = 100;
        boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      
        assertTrue(found);
        assertEquals(0.0, solver.getRoot(), epsilon);
    }

    @Test
    public void testFindRoot_edge() {
        // Edge case: function with two roots
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return HIGH;
                } else {
                    return ON_GOING;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = -10.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);
        int maxIter = 100;
        boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      
        assertTrue(found);
        assertEquals(-2.0, solver.getRoot(), epsilon);
      
        found = solver.findRoot(function, checker, maxIter, -2.0, -4.0, 2.0, 0.0);
      
        assertTrue(found);
        assertEquals(2.0, solver.getRoot(), epsilon);
    }
  
    @Test
    public void testFindRoot_error() {
        // Error scenario: non-converging function
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.cos(x);
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return HIGH;
                } else {
                    return ON_GOING;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = -10.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);
        int maxIter = 100;
        boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      
        assertFalse(found);
    }

}