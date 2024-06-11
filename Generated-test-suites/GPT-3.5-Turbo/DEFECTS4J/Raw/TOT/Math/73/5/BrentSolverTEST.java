import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot_withValidValues() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test with a simple linear function: f(x) = 2x -5
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return 2 * x - 5;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        // The function has a root at x = 2.5
        double x0 = 2;
        double f0 = 2 * x0 - 5;
        double x1 = 3;
        double f1 = 2 * x1 - 5;

        boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        // Check if a root has been found
        assertTrue(result);
        // Check the value of the root
        assertEquals(2.5, solver.getRoot(), 0.0001);
    }

    @Test
    public void testFindRoot_withInvalidValues() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test with a constant function: f(x) = 5
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return 5;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        // The function has no root in the given interval
        double x0 = 2;
        double f0 = 5;
        double x1 = 3;
        double f1 = 5;

        boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        // Check if no root has been found
        assertFalse(result);
    }

    // Add more test cases for other methods in the BrentSolver class

}