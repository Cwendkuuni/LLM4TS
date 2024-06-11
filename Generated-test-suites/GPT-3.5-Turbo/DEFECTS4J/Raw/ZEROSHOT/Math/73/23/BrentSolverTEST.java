import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {
    
    private BrentSolver solver;
    
    @Before
    public void setup() {
        solver = new BrentSolver();
    }
    
    @Test
    public void testFindRoot() throws FunctionException {
        double expectedRoot = 0.5;
        double x0 = 0.0;
        double f0 = -0.5;
        double x1 = 1.0;
        double f1 = 0.5;
        
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x - expectedRoot;
            }
        };
        
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-10) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-10) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        };
        
        int maxIter = 100;
        boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
        
        assertTrue(rootFound);
        assertEquals(expectedRoot, solver.getRoot(), 1e-10);
    }
    
}