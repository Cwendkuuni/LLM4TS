import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import static org.junit.Assert.*;

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
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                }else if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                }else {
                    return ConvergenceChecker.NONE;
                }
            }
        };
        int maxIter = 100;
        double x0 = 1;
        double f0 = function.valueAt(x0);
        double x1 = 3;
        double f1 = function.valueAt(x1);
        
        boolean rootFound = brentSolver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        assertTrue(rootFound);
        assertEquals(2.0, brentSolver.getRoot(), 1e-6);
    }
}