import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {
    private BrentSolver brentSolver;
    
    @Before
    public void setUp() {
        brentSolver = new BrentSolver();
    }
    
    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x*x - 4;
            }
        };
        
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return Math.abs(fLow) < 1e-6 ? LOW : UNCONVERGED;
            }
        };
        
        assertTrue(brentSolver.findRoot(function, checker, 100, 1, -3, 2, 4));
        assertEquals(2, brentSolver.getRoot(), 1e-6);
        
        assertFalse(brentSolver.findRoot(function, checker, 100, 1, -1, 2, 3));
    }
}