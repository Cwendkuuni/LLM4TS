
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 2;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return xLow == xHigh ? ConvergenceChecker.LOW : ConvergenceChecker.NO_CONVERGENCE;
            }
        };
        int maxIter = 100;
        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);

        boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertTrue(foundRoot);
        assertEquals(0, solver.getRoot(), 0.000001);
    }
}
