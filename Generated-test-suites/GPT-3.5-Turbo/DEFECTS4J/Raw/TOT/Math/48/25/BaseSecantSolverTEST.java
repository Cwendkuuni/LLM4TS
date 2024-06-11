import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Create test function for solving
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - x - 1.0;
            }
        };

        // Create instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        try {
            // Test typical use case
            double result = solver.solve(100, f, 1, 2);
            assertEquals(1.6180339, result, 1e-6);

            // Test edge case
            result = solver.solve(100, f, 1, 2, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
            assertEquals(1.0, result, 1e-6);

            // Test error scenario
            result = solver.solve(100, f, 1, 2, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
            fail("ConvergenceException should have been thrown");

        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().startsWith("Illegal state"));
        } catch (MathInternalError e) {
            fail("MathInternalError should not have been thrown");
        }
    }

}