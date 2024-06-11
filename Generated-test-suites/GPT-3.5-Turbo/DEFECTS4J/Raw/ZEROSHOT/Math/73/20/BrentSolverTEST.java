import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver solver;
    private double epsilon;

    @Before
    public void setUp() {
        solver = new BrentSolver();
        epsilon = Math.pow(2.0, -52);
    }

    @Test
    public void testFindRoot() {
        double x0 = 0.0;
        double f0 = 1.0;
        double x1 = 2.0;
        double f1 = 3.0;
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x + 1.0;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(xHigh - xLow) < epsilon) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.HIGH;
                }
            }
        };
        int maxIter = 1000;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertTrue(result);
        assertEquals(1.0, solver.getRoot(), epsilon);
    }

    @Test
    public void testGetRoot() {
        solver.getRoot();
    }
}