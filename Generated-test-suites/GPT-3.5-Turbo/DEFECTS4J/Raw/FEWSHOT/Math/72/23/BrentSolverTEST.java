import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {
    private BrentSolver solver;
    private ComputableFunction function;
    private ConvergenceChecker checker;
    private int maxIter;
    private double x0;
    private double f0;
    private double x1;
    private double f1;
    
    @Before
    public void setUp() {
        solver = new BrentSolver();
        function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(xHigh - xLow) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NO_CONVERGENCE;
                }
            }
        };
        maxIter = 100;
        x0  = 1.0;
        f0 = -3.0;
        x1 = 3.0;
        f1 = 5.0;
    }
    
    @Test
    public void testFindRoot() {
        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        double root = solver.getRoot();
        assertEquals(2.0, root, 1e-6);
        
        assertFalse(solver.findRoot(function, checker, maxIter, 2.5, -1.5, 4.0, 8.0));
        assertNull(solver.getRoot());
        
        assertFalse(solver.findRoot(function, checker, maxIter, 0.0, -1.0, 1.0, 3.0));
        assertNull(solver.getRoot());
    }
}