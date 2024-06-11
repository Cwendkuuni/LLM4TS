import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

    private static final double EPSILON = 1e-10;

    private static final ComputableFunction FUNCTION = new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
            return x * x - 4;
        }
    };

    private static final ConvergenceChecker CHECKER = new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fHigh) < EPSILON) {
                return ConvergenceChecker.HIGH;
            }
            if (Math.abs(fLow) < EPSILON) {
                return ConvergenceChecker.LOW;
            }
            return ConvergenceChecker.NOT_CONVERGED;
        }
    };

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        try {
            assertTrue(solver.findRoot(FUNCTION, CHECKER, 100, 0.0, -4.0, 5.0, 9.0));
            assertEquals(2.0, solver.getRoot(), EPSILON);

            assertTrue(solver.findRoot(FUNCTION, CHECKER, 100, -10.0, 96.0, 10.0, 76.0));
            assertEquals(-2.0, solver.getRoot(), EPSILON);

            assertFalse(solver.findRoot(FUNCTION, CHECKER, 100, -10.0, 96.0, 0.0, -4.0));

        } catch (FunctionException e) {
            fail(e.getMessage());
        }
    }

}