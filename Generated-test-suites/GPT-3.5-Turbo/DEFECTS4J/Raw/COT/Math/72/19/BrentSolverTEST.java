import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new TestFunction();
        ConvergenceChecker checker = new TestChecker();
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 1.0;
        double f1 = function.valueAt(x1);
        
        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        
        assertTrue(result);
        assertEquals(0.5, solver.getRoot(), epsilon);
    }
    
    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        double expected = 0.5;
        
        double result = solver.getRoot();
        
        assertEquals(expected, result, epsilon);
    }
    
    private class TestFunction implements ComputableFunction {
        public double valueAt(double x) {
            return x - 0.5;
        }
    }
    
    private class TestChecker implements ConvergenceChecker {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < epsilon) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < epsilon) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    }
}