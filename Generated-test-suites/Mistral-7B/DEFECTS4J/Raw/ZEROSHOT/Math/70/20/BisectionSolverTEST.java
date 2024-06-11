import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -3.0, 3.0);
        assertEquals("Root should be approximately 1.41421", 1.41421356237, result, 0.001);

        try {
            solver.solve(function, -3.0, 3.0, 0.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        try {
            solver.solve(function, -3.0, 3.0, Double.NaN);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        try {
            solver.solve(function, -3.0, 3.0, new Double("NaN"));
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        try {
            solver.solve(function, -3.0, 3.0, null);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        try {
            solver.solve(function, -3.0, 3.0, new Object());
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        try {
            solver.solve(function, -3.0, 3.0, new String());
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(0);
        solver.solve(function, -3.0, 3.0);
    }
}