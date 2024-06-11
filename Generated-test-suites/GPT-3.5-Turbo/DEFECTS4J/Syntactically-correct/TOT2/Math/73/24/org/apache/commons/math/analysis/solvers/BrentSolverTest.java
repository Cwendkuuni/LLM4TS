import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4; // Function x^2 - 4
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) {
                return (Math.abs(f1) < 1e-6) ? ConvergenceChecker.HIGH : ConvergenceChecker.NOT_CONVERGED;
            }
        };
        int maxIter = 100;
        double x0 = 0.0;
        double f0 = function.valueAt(x0);
        double x1 = 5.0;
        double f1 = function.valueAt(x1);

        try {
            assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
            assertEquals(2.0, solver.getRoot(), 1e-6); // Expecting root at x = 2.0
        } catch (FunctionException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        double expectedRoot = 0.0;
        assertEquals(expectedRoot, solver.getRoot(), 1e-6);
    }
}