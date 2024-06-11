import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDoSolve() {
        // Test basic functionality of doSolve method
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(1000, x -> x * x - 4, 0, 4);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithMaxEval() {
        // Test solving with maximum evaluations limit
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(5, x -> x * x - 2, 0, 2);
        assertEquals(1.4142, result, 0.0001);
    }

    @Test
    public void testSolveWithStartValue() {
        // Test solving with a specified start value
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(1000, x -> x * x - 4, 0, 4, 3);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        // Test for NoBracketingException when interval does not bracket the root
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(1000, x -> x * x - 4, 4, 5);
    }

    @Test
    public void testBoundaryValues() {
        // Test for boundary values (min, max, and start value)
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(1000, x -> x * x - 4, -2, 2);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = MathInternalError.class)
    public void testMathInternalError() {
        // Test for MathInternalError when an unexpected situation occurs
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 2);
        solver.solve(1000, x -> x * x - 4, 0, 1);
    }
}