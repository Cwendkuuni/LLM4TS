import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.solvers.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 3);
            assertEquals(2, result, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 3, 0.001);
            assertEquals(2, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 1, -10, 10);
            assertEquals(-2, result[0], 0.0001);
            assertEquals(2, result[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5, result, 0.0001);
    }

}