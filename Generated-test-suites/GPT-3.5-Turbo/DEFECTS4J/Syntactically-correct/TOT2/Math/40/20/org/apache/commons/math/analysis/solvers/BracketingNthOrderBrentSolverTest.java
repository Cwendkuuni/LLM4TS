import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testConstructorDefaultAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
    }

    @Test
    public void testConstructorCustomAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertNotNull(solver);
    }

    @Test
    public void testConstructorMaxOrderException() {
        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 3);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 3, 1);
        assertEquals(2.0, result, 1e-6);
    }
}