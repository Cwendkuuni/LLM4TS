import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test case 1: root exists in the interval [-10, 10]
        double x0 = -10;
        double f0 = -101;
        double x1 = 10;
        double f1 = 99;
        boolean result = solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 100;
            }
        }, new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(f0) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(f1) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        }, 100, x0, f0, x1, f1);

        assertTrue(result);
        assertEquals(10.0, solver.getRoot(), 1e-6);

        // Test case 2: root does not exist in the interval [-10, 10]
        x0 = -10;
        f0 = -1000;
        x1 = 10;
        f1 = 900;
        result = solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 100;
            }
        }, new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                if (Math.abs(f0) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(f1) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        }, 100, x0, f0, x1, f1);

        assertFalse(result);
    }
}