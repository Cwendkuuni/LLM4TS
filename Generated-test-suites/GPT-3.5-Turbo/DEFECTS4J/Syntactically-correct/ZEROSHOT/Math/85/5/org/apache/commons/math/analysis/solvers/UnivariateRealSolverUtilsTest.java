import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws FunctionEvaluationException, ConvergenceException {
        // Create a dummy function for testing
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        // Test default solve() method
        double result = UnivariateRealSolverUtils.solve(f, -2, 2);
        assertEquals(2.0, result, 1e-6);

        // Test solve() method with absolute accuracy
        double resultWithAccuracy = UnivariateRealSolverUtils.solve(f, -2, 2, 1e-4);
        assertEquals(2.0, resultWithAccuracy, 1e-4);

        // Test solve() method with invalid interval
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(f, 2, -2, 1e-6);
        });
    }

    @Test
    public void testBracket() throws FunctionEvaluationException, ConvergenceException {
        // Create a dummy function for testing
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        // Test bracket() method
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -3, 3);
        assertEquals(-2.0, result[0], 1e-6);
        assertEquals(2.0, result[1], 1e-6);

        // Test bracket() method with invalid initial value
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, -5, -3, 3);
        });

        // Test bracket() method with invalid interval
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, 0, 3, -3);
        });
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, midpoint, 1e-6);
    }

}