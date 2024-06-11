import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithParams() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertNotNull(solver);
    }

    @Test
    public void testMaximalOrderGetter() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, Math::sin, 0, Math.PI, AllowedSolution.RIGHT_SIDE);
        assertEquals(Math.PI, result, 1e-6);
    }

    @Test
    public void testSolveWithStartValueAndAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, Math::cos, 0, Math.PI, Math.PI / 2, AllowedSolution.LEFT_SIDE);
        assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolveWithManyPoints() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, Math::exp, 0, 10);
        assertTrue(result > 1 && result < 2);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, x -> x * x, -1, 1);
    }

}