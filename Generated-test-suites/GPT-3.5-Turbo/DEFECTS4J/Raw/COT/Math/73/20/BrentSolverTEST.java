import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {
    
    @Test
    public void testFindRoot() throws FunctionException {
        // Create a simple linear function
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return 3 * x + 2;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        
        boolean rootFound = solver.findRoot(function, checker, 100, -10, -28, 10, 32);
        assertTrue(rootFound);
        
        double root = solver.getRoot();
        assertEquals(-2.0/3.0, root, 1e-6);
    }

    // Test additional public methods here
    
}