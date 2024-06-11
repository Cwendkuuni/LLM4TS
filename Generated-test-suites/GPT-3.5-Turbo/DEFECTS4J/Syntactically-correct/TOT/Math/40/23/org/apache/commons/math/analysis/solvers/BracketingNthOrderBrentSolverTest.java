import org.junit.Test;

import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.exception.NoBracketingException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.util.Precision;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x + 2;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // Typical Use Case
        double result = solver.solve(100, f, -1, 1, AllowedSolution.ANY_SIDE);
        assertEquals(-2.0, result, 1e-12);

        // Edge Cases
        result = solver.solve(100, f, Double.MIN_VALUE, Double.MAX_VALUE, AllowedSolution.ANY_SIDE);
        assertEquals(-2.0, result, 1e-12);

        // Error Scenarios
        try {
            result = solver.solve(100, f, 1, -1, AllowedSolution.ANY_SIDE);
            fail("Should have thrown NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected exception
        }

        try {
            result = solver.solve(100, f, 1, 1, AllowedSolution.ANY_SIDE);
            fail("Should have thrown NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected exception
        }

        try {
            result = solver.solve(100, f, 1, 1, 1, AllowedSolution.ANY_SIDE);
            fail("Should have thrown NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected exception
        }

    }

}