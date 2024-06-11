import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    private static final UnivariateFunction FUNCTION = f -> Math.pow(f, 3) - 2;

    @org.junit.Test
    public void testBracketingNthOrderBrentSolver() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);

        // Test with default bracketing interval
        double root = solver.solve(100, FUNCTION, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root found outside of bracketing interval", 0.0, root, 1e-7);

        // Test with custom start value
        root = solver.solve(100, FUNCTION, -2.0, 2.0, -1.5, AllowedSolution.ANY_SIDE);
        assertEquals("Root not found or not in the expected side", 0.0, root, 1e-7);

        // Test with no bracketing interval
        try {
            solver.solve(100, FUNCTION, 0.0, 0.0, AllowedSolution.ANY_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with custom allowed solution
        root = solver.solve(100, FUNCTION, -2.0, 2.0, -1.5, AllowedSolution.BELOW_SIDE);
        assertEquals("Root not found below the bracketing interval", 1.0, root, 1e-7);

        // Test with custom maximal order
        solver = new BracketingNthOrderBrentSolver(1e-6, 10);
        root = solver.solve(100, FUNCTION, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root not found or not very close to the expected value", 0.0, root, 1e-10);
    }

}