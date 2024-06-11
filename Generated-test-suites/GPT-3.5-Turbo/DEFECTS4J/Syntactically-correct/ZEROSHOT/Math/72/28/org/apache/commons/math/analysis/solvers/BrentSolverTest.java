import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.NONE;
            }
        };
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = -4.0;
        double x1 = 2.0;
        double f1 = 0.0;

        boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

        assertTrue(result);
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

}