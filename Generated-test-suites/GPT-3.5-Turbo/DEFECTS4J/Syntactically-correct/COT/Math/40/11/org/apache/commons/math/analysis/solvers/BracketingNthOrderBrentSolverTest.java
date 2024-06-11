import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    // create an instance of BracketingNthOrderBrentSolver
    private BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

    // helper method for computing the square of a number
    private double square(double x) {
        return x * x;
    }

    @Test
    public void testSolve() {
        // test a simple case where the function is a parabola and the root is known
        double root = solver.solve(100, x -> square(x - 2), 0, 4);
        assertEquals(2, root, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        // test a case where there is a root outside the specified range
        double root = solver.solve(100, x -> square(x - 2), 0, 4, BracketingNthOrderBrentSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(3, root, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        // test the constructor with an invalid maximal order
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketing() {
        // test a case where no bracketing of the root is possible
        solver.solve(100, x -> x, 0, 1);
    }
}