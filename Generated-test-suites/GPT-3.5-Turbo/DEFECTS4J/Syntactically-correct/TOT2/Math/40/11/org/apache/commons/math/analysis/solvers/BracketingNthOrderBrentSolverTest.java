import static org.junit.Assert.*;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithRelativeAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1e-15, 5);
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1e-15, 1e-10, 5);
        assertNotNull(solver);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x * x - 4, 0, 4);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x * x - 4, 0, 4, AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, x -> x * x - 4, 0, 1);
    }

}