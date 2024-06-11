import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testConstructor() {
        BrentSolver solver = new BrentSolver();
        assertNotNull(solver);
    }

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        }, new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        }, 100, 0, -4, 5, 21);
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot());
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        solver.findRoot(new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.sin(x);
            }
        }, new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        }, 100, 0, 0, 3, 0);
        assertEquals(0.0, solver.getRoot(), 1e-6);
    }

}