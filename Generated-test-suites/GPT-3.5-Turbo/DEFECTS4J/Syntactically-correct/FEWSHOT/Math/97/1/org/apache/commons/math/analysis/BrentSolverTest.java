import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BrentSolverTest {
    private ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
            return x * x - 4;
        }
    };
    
    private ConvergenceChecker checker = new ConvergenceChecker() {
        public int converged(double x1, double f1, double x2, double f2) {
            if (Math.abs(f1) < 0.01) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(f2) < 0.01) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    };

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, 1.0, -3.0, 2.0, 2.0);
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 0.01);
        
        rootFound = solver.findRoot(function, checker, 100, -3.0, -3.0, 0.0, -4.0);
        assertTrue(rootFound);
        assertEquals(-2.0, solver.getRoot(), 0.01);
        
        rootFound = solver.findRoot(function, checker, 100, -5.0, -10.0, -1.0, 2.0);
        assertFalse(rootFound);
        assertEquals(Double.NaN, solver.getRoot(), 0.01);
    }
    
}