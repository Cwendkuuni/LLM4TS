import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.junit.Test;

import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Set up test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x < 0) {
                    throw new FunctionEvaluationException(x);
                }
                return x * x - 4;
            }
        };

        // Create solver instance
        BisectionSolver solver = new BisectionSolver(f);

        // Test typical use case
        double result = solver.solve(0, 3);
        assertEquals(2.0, result, 1e-6);

        // Test edge cases
        result = solver.solve(-2, 2);
        assertEquals(0, result, 1e-6);

        result = solver.solve(-4, -1);
        assertEquals(-2, result, 1e-6);

        // Test error scenario
        try {
            result = solver.solve(-5, -1);
            fail("Expected FunctionEvaluationException to be thrown");
        } catch (FunctionEvaluationException e) {
            // Exception expected
        }

        try {
            result = solver.solve(-4, 1);
            fail("Expected MaxIterationsExceededException to be thrown");
        } catch (MaxIterationsExceededException e) {
            // Exception expected
        }
    }
}