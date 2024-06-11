import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        
        double x0 = 0.0;
        double f0 = -1.0;
        double x1 = 1.0;
        double f1 = 1.0;
        
        boolean rootFound = solver.findRoot(new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 1;
            }
        }, new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return ConvergenceChecker.HIGH;
            }
        }, 100, x0, f0, x1, f1);
        
        assertTrue(rootFound);
        assertEquals(1.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        
        double expectedRoot = 0.0;
        
        assertEquals(expectedRoot, solver.getRoot(), 0.0);
    }
}