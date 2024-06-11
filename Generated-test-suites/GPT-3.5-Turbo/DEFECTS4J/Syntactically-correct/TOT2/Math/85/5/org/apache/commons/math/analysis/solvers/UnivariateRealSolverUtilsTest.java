import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithoutAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4; // Function x^2 - 4
            }
        };
        double result = UnivariateRealSolverUtils.solve(function, 1, 3);
        assertThat(result, is(2.0));
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4; // Function x^2 - 4
            }
        };
        double result = UnivariateRealSolverUtils.solve(function, 1, 3, 0.01);
        assertThat(result, closeTo(2.0, 0.01));
    }

    @Test
    public void testBracketSuccessful() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4; // Function x^2 - 4
            }
        };
        double[] bracketResult = UnivariateRealSolverUtils.bracket(function, 2.5, 1, 3);
        assertThat(bracketResult[0], is(2.0));
        assertThat(bracketResult[1], is(3.0));
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketFailure() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 4; // Function x^2 + 4 (no zero crossing)
            }
        };
        UnivariateRealSolverUtils.bracket(function, 2.5, 1, 3);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1.0, 3.0);
        assertThat(result, is(2.0));
    }
}