import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveDefault() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 10);
            assertEquals(5.0, result, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 10, 0.1);
            assertEquals(5.0, result, 0.1);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };

        try {
            double[] bracket = UnivariateRealSolverUtils.bracket(f, 3.0, 0, 10);
            assertEquals(2.0, bracket[0], 0.0001);
            assertEquals(4.0, bracket[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown while bracketing: " + e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(2.0, 4.0);
        assertEquals(3.0, result, 0.0001);
    }
}