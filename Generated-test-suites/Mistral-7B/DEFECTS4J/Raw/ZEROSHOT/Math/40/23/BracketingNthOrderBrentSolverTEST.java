import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolver() throws NumberIsTooSmallException, NoBracketingException {

        // Test with default settings
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.E, x) - 1;
        double min = -5.0;
        double max = 5.0;
        double result = solver.solve(100, function, min, max);
        assertEquals(0.0, result, 1e-6);

        // Test with specified accuracy and maximal order
        BracketingNthOrderBrentSolver solver1 = new BracketingNthOrderBrentSolver(1e-8, 10);
        double result1 = solver1.solve(100, function, min, max);
        assertEquals(0.0, result1, 1e-8);

        // Test with specified relative and absolute accuracies
        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver(0.01, 1e-8, 1e-8);
        double result2 = solver2.solve(100, function, min, max);
        assertEquals(0.0, result2, 1e-8);

        // Test with specified function value accuracy
        BracketingNthOrderBrentSolver solver3 = new BracketingNthOrderBrentSolver(0.01, 1e-8, 1e-12);
        double result3 = solver3.solve(100, function, min, max);
        assertEquals(0.0, result3, 1e-12);

        // Test with custom allowed solution
        BracketingNthOrderBrentSolver solver4 = new BracketingNthOrderBrentSolver(0.01, 1e-8, 1e-12);
        solver4.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        double result4 = solver4.solve(100, function, min, max);
        assertTrue(result4 < 0);

        // Test with non-bracketing interval
        UnivariateFunction functionNotBracketing = x -> Math.sin(x) - Math.cos(x);
        BracketingNthOrderBrentSolver solver5 = new BracketingNthOrderBrentSolver(0.01, 1e-8, 1e-12);
        double minNotBracketing = -5.0;
        double maxNotBracketing = 4.0;
        try {
            solver5.solve(100, functionNotBracketing, minNotBracketing, maxNotBracketing);
            assertTrue(false);
        } catch (NoBracketingException e) {
            // Expected
        }

    }

}