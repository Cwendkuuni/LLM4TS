import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveDefault() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5; // Simple linear function with zero at x = 5
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 4, 6);
            assertEquals(5.0, result, 0.0001);
        } catch (ConvergenceException e) {
            fail("ConvergenceException occurred");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException occurred");
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 25; // Quadratic function with zeroes at x = -5 and x = 5
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, -6, 0, 0.0001);
            assertEquals(-5.0, result, 0.0001);
        } catch (ConvergenceException e) {
            fail("ConvergenceException occurred");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException occurred");
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x * x; // Cubic function
            }
        };

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 0, -2, 2, 100);
            assertEquals(-1.0, result[0], 0.0001);
            assertEquals(1.0, result[1], 0.0001);
        } catch (ConvergenceException e) {
            fail("ConvergenceException occurred");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException occurred");
        }
    }

    @Test
    public void testMidpoint() {
        double mid = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5.0, mid, 0.0001);
    }

}