
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4 * x + 3; // (x-1)(x-3)
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 4.5);
            assertEquals(1.000000001, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getClass() + " thrown unexpectedly");
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4 * x + 3; // (x-1)(x-3)
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 4.5, 0.001);
            assertEquals(1.000000001, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getClass() + " thrown unexpectedly");
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4 * x + 3; // (x-1)(x-3)
            }
        };

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 2, 0, 4);
            assertEquals(1.0, result[0], 0.001);
            assertEquals(3.0, result[1], 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getClass() + " thrown unexpectedly");
        }
    }

    @Test
    public void testBracketWithIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4 * x + 3; // (x-1)(x-3)
            }
        };

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 2, 0, 4, 100);
            assertEquals(1.0, result[0], 0.001);
            assertEquals(3.0, result[1], 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getClass() + " thrown unexpectedly");
        }
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, result, 0.001);
    }
}
