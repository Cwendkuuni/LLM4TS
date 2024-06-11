import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        // Test case for findRoot method with typical input
        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double f0 = -2.0;
        double x1 = 2.0;
        double f1 = 3.0;
        boolean result = solver.findRoot(x -> x * x - 4, (xLow, fLow, xHigh, fHigh) -> {
            if (fLow * fHigh <= 0) {
                return ConvergenceChecker.LOW;
            } else {
                return ConvergenceChecker.HIGH;
            }
        }, 100, x0, f0, x1, f1);
        assertTrue(result);
        assertEquals(2.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testGetRoot() {
        // Test case for getRoot method
        BrentSolver solver = new BrentSolver();
        assertEquals(Double.NaN, solver.getRoot(), 1e-10);
    }

    @Test
    public void testExceptionHandling() {
        // Test case for exception handling when function throws an exception
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = x -> { throw new FunctionException("Function not available"); };
        try {
            solver.findRoot(function, (xLow, fLow, xHigh, fHigh) -> ConvergenceChecker.NONE, 100, 0.0, 0.0, 1.0, 1.0);
            fail("Exception not thrown");
        } catch (FunctionException e) {
            // Exception expected
        }
    }
}