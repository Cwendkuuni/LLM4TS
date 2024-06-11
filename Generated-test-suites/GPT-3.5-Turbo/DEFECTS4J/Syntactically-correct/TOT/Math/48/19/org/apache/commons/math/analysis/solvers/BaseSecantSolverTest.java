import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-10;

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                // Define the function used for testing
                return ((point - 3) * (point - 5) * (point - 7));
            }
        };
    }

    @Test
    public void testSolve_withDefaultMaxEval_validSolutionReturned() {
        double solution = solver.solve(100, 0, 10);
        assertEquals(3, solution, EPSILON);
    }

    @Test
    public void testSolve_withMaxEvalReached_ConvergenceExceptionThrown() {
        try {
            solver.solve(2, 0, 10);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException e) {
            assertEquals("Maximum number of iterations (2) exceeded", e.getMessage());
        }
    }

    @Test
    public void testSolve_withFunctionValueEqualToZero_validSolutionReturned() {
        double solution = solver.solve(100, 3, 7);
        assertEquals(3, solution, EPSILON);

        solution = solver.solve(100, 5, 7);
        assertEquals(5, solution, EPSILON);

        solution = solver.solve(100, 3, 5);
        assertEquals(3, solution, EPSILON);
    }

    @Test
    public void testSolve_withNegativeBounds_validSolutionReturned() {
        double solution = solver.solve(100, -10, -5);
        assertEquals(7, solution, EPSILON);
    }

    @Test
    public void testSolve_withPositiveBounds_validSolutionReturned() {
        double solution = solver.solve(100, 10, 15);
        assertEquals(7, solution, EPSILON);
    }

    @Test
    public void testSolve_withStartValueInBounds_validSolutionReturned() {
        double solution = solver.solve(100, 0, 10, 5);
        assertEquals(5, solution, EPSILON);

        solution = solver.solve(100, 0, 10, 4);
        assertEquals(3, solution, EPSILON);
    }

    @Test
    public void testSolve_withStartValueOutOfRange_validSolutionReturned() {
        double solution = solver.solve(100, 0, 10, 15);
        assertEquals(7, solution, EPSILON);

        solution = solver.solve(100, 0, 10, -5);
        assertEquals(7, solution, EPSILON);
    }

    @Test
    public void testSolve_withAllowedSolutionLeftSide_validSolutionReturned() {
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        double solution = solver.solve(100, 0, 10);
        assertEquals(5, solution, EPSILON);
    }

    @Test
    public void testSolve_withAllowedSolutionRightSide_validSolutionReturned() {
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        double solution = solver.solve(100, 0, 10);
        assertEquals(3, solution, EPSILON);
    }

    @Test
    public void testSolve_withAllowedSolutionBelowSide_validSolutionReturned() {
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        double solution = solver.solve(100, 0, 10);
        assertEquals(3, solution, EPSILON);
    }

    @Test
    public void testSolve_withAllowedSolutionAboveSide_validSolutionReturned() {
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        double solution = solver.solve(100, 0, 10);
        assertEquals(5, solution, EPSILON);
    }

    @Test
    public void testSolve_withAllowedSolutionAnySide_validSolutionReturned() {
        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double solution = solver.solve(100, 0, 10);
        assertEquals(3, solution, EPSILON);
    }

}