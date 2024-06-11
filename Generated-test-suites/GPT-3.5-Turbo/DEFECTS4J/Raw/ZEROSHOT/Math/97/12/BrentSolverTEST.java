import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {
    
    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(f0) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(f1) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.KEEP_GOING;
                }
            }
        };
        int maxIter = 100;
        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 3.0;
        double f1 = function.valueAt(x1);
        
        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }
    
}