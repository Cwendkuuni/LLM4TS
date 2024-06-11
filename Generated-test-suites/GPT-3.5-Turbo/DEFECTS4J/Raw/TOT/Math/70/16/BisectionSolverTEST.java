import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.analysis.SinFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        function = new SinFunction(); // Example function for testing
    }

    @Test
    public void testSolve() {
        try {
            double result = solver.solve(function, 0.0, 1.0);
            assertEquals(result, 0.0, 1E-6); // Check if result is within the defined tolerance
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception occurred during solve()");
        }
    }

    @Test
    public void testSolveInvalidInterval() {
        try {
            solver.solve(function, 1.0, 0.0); // Invalid interval (max < min), should throw an exception
            fail("No exception thrown for invalid interval");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testSolveMaxIterationsExceeded() {
        try {
            // Use a function with a known root to test max iterations exceeded scenario
            UnivariateRealFunction functionWithKnownRoot = new QuinticFunction(); 
            solver.solve(functionWithKnownRoot, -5.0, 5.0); // Set a large interval to ensure max iterations are exceeded
            fail("No exception thrown for max iterations exceeded");
        } catch (MaxIterationsExceededException e) {
            assertTrue(e instanceof MaxIterationsExceededException);
        } catch (FunctionEvaluationException e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testSolveWithInitialGuess() {
        try {
            double result = solver.solve(function, 0.0, 1.0, 0.5); // Provide initial guess
            assertEquals(result, 0.0, 1E-6); // Check if result is within the defined tolerance
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception occurred during solve()");
        }
    }

    @Test
    public void testSolveWithoutInitialGuess() {
        try {
            double result = solver.solve(function, 0.0, 1.0); // Without initial guess
            assertEquals(result, 0.0, 1E-6); // Check if result is within the defined tolerance
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception occurred during solve()");
        }
    }

}