import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolverDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testBracketingNthOrderBrentSolverCustomConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x - 2, 1, 3, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, x -> x, 1, 3);
    }

    @Test
    public void testPerfectRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-10, 4);
        double result = solver.solve(100, x -> Math.sin(x), 1, 4);
        assertEquals(Math.PI, result, 1e-6);
    }

}