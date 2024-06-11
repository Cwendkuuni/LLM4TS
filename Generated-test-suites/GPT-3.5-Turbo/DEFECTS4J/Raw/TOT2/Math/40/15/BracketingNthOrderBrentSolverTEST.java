import static org.junit.Assert.*;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithParams() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(7, solver.getMaximalOrder());
    }

    @Test
    public void testSolveForPerfectRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 5; // Perfect root at x=5
            }
        };
        double root = solver.solve(100, function, 2, 8);
        assertEquals(5.0, root, 0.0);
    }

    @Test
    public void testSolveForInvalidInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        try {
            solver.solve(100, function, 8, 2); // Invalid interval
            fail("NoBracketingException should have been thrown");
        } catch (NoBracketingException e) {
            // Exception expected
        }
    }

    // Additional tests for edge cases and error scenarios can be added here

}