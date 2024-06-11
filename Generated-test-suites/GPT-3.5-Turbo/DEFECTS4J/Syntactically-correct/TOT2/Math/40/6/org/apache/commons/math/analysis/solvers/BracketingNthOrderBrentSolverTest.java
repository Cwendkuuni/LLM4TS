import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithParameters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertNotNull(solver);
    }

    @Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x - 5, 0, 10);
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x - 5, 0, 10, 2.0);
        assertEquals(5.0, result, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, x -> x + 5, 0, 10);
    }

    @Test
    public void testMaximalOrderException() {
        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Test passed
        }
    }
}