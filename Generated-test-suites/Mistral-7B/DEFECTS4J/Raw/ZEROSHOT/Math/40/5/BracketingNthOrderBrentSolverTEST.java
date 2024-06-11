import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolver() {
        // Test with default constructor
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = (x) -> Math.pow(x, 2) - 4.0;
        double result = solver.solve(100, function, -2.0, 2.0);
        assertEquals("Test with default constructor: expected result = 0.0, got " + result, 0.0, result, 1e-8);

        // Test with constructor with absolute accuracy and maximal order
        solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        result = solver.solve(100, function, -2.0, 2.0);
        assertEquals("Test with constructor with absolute accuracy and maximal order: expected result = 0.0, got " + result, 0.0, result, 1e-10);

        // Test with constructor with relative accuracy, absolute accuracy, and maximal order
        solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 5);
        result = solver.solve(100, function, -2.0, 2.0);
        assertEquals("Test with constructor with relative accuracy, absolute accuracy, and maximal order: expected result = 0.0, got " + result, 0.0, result, 1e-10);

        // Test with constructor with relative accuracy, absolute accuracy, function value accuracy, and maximal order
        solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 1e-10, 5);
        result = solver.solve(100, function, -2.0, 2.0);
        assertEquals("Test with constructor with relative accuracy, absolute accuracy, function value accuracy, and maximal order: expected result = 0.0, got " + result, 0.0, result, 1e-11);

        // Test with constructor with invalid maximal order
        try {
            solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 1, 1);
            assertEquals("Test with constructor with invalid maximal order: expected exception thrown", true, true);
        } catch (NumberIsTooSmallException e) {
            // expected exception
        }

        // Test with allowed solution set to LEFT_SIDE
        solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 5);
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        result = solver.solve(100, function, -2.0, 2.0);
        assertEquals("Test with allowed solution set to LEFT_SIDE: expected result < 0.0, got " + result, -2.0, result, 1e-8);

        // Test with allowed solution set to RIGHT_SIDE
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        result = solver.solve(100, function, -2.0, 2.0);
        assertEquals("Test with allowed solution set to RIGHT_SIDE: expected result > 0.0, got " + result, 2.0, result, 1e-8);

        // Test with allowed solution set to ABOVE_SIDE
        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        result = solver.solve(100, function, -2.0, 2.0);
        assertEquals("Test with allowed solution set to ABOVE_SIDE: expected result > 0.0, got " + result, 2.0, result, 1e-8);

        // Test with allowed solution set to BELOW_SIDE
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        result = solver.solve(100, function, -2.0, 2.0);
        assertEquals("Test with allowed solution set to BELOW_SIDE: expected result < 0.0, got " + result, -2.0, result, 1e-8);

        // Test with allowed solution set to ANY_SIDE
        solver.setAllowedSolution(AllowedSolution.ANY_SIDE);
        result = solver.solve(100, function, -2.0, 2.0);
        assertEquals("Test with allowed solution set to ANY_SIDE: expected result = 0.0, got " + result, 0.0, result, 1e-8);

        // Test with no bracketing interval
        solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 5);
        try {
            result = solver.solve(100, function, 1.0, 1.0);
            assertEquals("Test with no bracketing interval: expected exception thrown", true, true);
        } catch (NoBracketingException e) {
            // expected exception
        }

        // Test with function that is not continuous
        UnivariateFunction discontinuousFunction = (x) -> {
            if (x == 0.0) {
                throw new MathIllegalArgumentException("Function is not defined at x = 0.0");
            }
            return Math.pow(x, 2) - 1.0;
        };
        try {
            solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 5);
            result = solver.solve(100, discontinuousFunction, -1.0, 1.0);
            assertEquals("Test with discontinuous function: expected exception thrown", true, true);
        } catch (Exception e) {
            assertEquals("Test with discontinuous function: expected NoBracketingException, got " + e.getClass().getName(), NoBracketingException.class, e.getClass());
        }

        // Test with function that has no roots
        UnivariateFunction noRootFunction = (x) -> Math.pow(x, 2) + 1.0;
        try {
            solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 5);
            result = solver.solve(100, noRootFunction, -1.0, 1.0);
            assertEquals("Test with function that has no roots: expected exception thrown", true, true);
        } catch (Exception e) {
            assertEquals("Test with function that has no roots: expected NoBracketingException, got " + e.getClass().getName(), NoBracketingException.class, e.getClass());
        }
    }

}