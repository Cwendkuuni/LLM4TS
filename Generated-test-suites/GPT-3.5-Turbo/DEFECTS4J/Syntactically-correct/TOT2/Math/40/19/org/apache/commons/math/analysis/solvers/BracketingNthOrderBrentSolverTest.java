import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

    @Test
    public void testDoSolve() {
        // Typical use case
        double result = solver.solve(100, Math::cos, 0.1, 1.0, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, 1e-6);

        // Edge case: Root at the boundaries
        double resultBoundary = solver.solve(100, Math::cos, 0.0, 1.0, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, resultBoundary, 1e-6);

        // Exception handling: No bracketing exception
        try {
            solver.solve(100, Math::cos, 0.0, 0.1, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Test passed
        }
    }

    @Test
    public void testGuessX() {
        // Typical use case
        double[] x = {0.0, 1.0, 2.0};
        double[] y = {1.0, 4.0, 9.0};
        double result = solver.guessX(4.0, x, y, 0, 3);
        assertEquals(2.0, result, 1e-6);

        // Edge case: Same y value
        double[] xSameY = {0.0, 1.0, 1.0};
        double[] ySameY = {1.0, 4.0, 4.0};
        double resultSameY = solver.guessX(4.0, xSameY, ySameY, 0, 3);
        assertTrue(Double.isNaN(resultSameY));
    }

    @Test
    public void testSolve() {
        // Typical use case
        double result = solver.solve(100, Math::sin, -1.0, 1.0, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, 1e-6);

        // Edge case: Only one point bracketing
        double resultOnePoint = solver.solve(100, Math::sin, 0.0, 0.0, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, resultOnePoint, 1e-6);

        // Exception handling: NumberIsTooSmallException for maximal order
        try {
            new BracketingNthOrderBrentSolver(1e-6, 1);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Test passed
        }
    }
}