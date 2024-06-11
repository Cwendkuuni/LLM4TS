import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot_withValidFunctionAndInterval_returnsTrue() {
        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(
                new ValidComputableFunction(),
                new ValidConvergenceChecker(),
                100,
                0.0, -10.0, 10.0, 10.0);

        assertTrue(result);
        assertEquals(0.0, solver.getRoot(), 1e-9);
    }

    @Test
    public void testFindRoot_withInvalidFunction_throwsException() {
        BrentSolver solver = new BrentSolver();
        try {
            solver.findRoot(
                    new InvalidComputableFunction(),
                    new ValidConvergenceChecker(),
                    100,
                    0.0, -10.0, 10.0, 10.0);
            fail("Expected FunctionException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof FunctionException);
            assertEquals("Invalid function", e.getMessage());
        }
    }

    @Test
    public void testFindRoot_withInvalidInterval_returnsFalse() {
        BrentSolver solver = new BrentSolver();
        boolean result = solver.findRoot(
                new ValidComputableFunction(),
                new ValidConvergenceChecker(),
                100,
                10.0, 10.0, 0.0, -10.0);

        assertFalse(result);
    }

    // Additional test cases...

}

class ValidComputableFunction implements ComputableFunction {

    @Override
    public double valueAt(double x) {
        // Valid function implementation
        return x * x - 4;
    }
}

class InvalidComputableFunction implements ComputableFunction {

    @Override
    public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Invalid function");
    }
}

class ValidConvergenceChecker implements ConvergenceChecker {

    @Override
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Valid convergence checker implementation
        if (Math.abs(fLow) < 1e-9) {
            return LOW;
        }
        if (Math.abs(fHigh) < 1e-9) {
            return HIGH;
        }
        return NO_CONVERGENCE;
    }
}