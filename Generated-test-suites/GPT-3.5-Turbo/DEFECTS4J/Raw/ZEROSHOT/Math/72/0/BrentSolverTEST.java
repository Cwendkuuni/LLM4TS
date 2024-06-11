import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {
    
    @Test
    public void testFindRoot() throws FunctionException {
        double epsilon = Math.pow(2.0, -52);
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < epsilon) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 10.0;
        double f1 = function.valueAt(x1);
        
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), epsilon);
    }
    
    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        solver.root = 10.0;
        assertEquals(10.0, solver.getRoot(), 0.0);
        
        solver.root = -5.6;
        assertEquals(-5.6, solver.getRoot(), 0.0);
    }
    
}