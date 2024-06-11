import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    private UnivariateRealFunction f;

    @Before
    public void setup() {
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(f, 0, 5);
        assertEquals(2.0, result, 0.0);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(f, 0, 5, 0.01);
        assertEquals(2.0, result, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(null, 0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(f, 5, 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithExceededIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(f, 0, 5, 0.001);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        double[] result = UnivariateRealSolverUtils.bracket(f, 2, -10, 10);
        assertEquals(1.0, result[0], 0.0);
        assertEquals(3.0, result[1], 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(null, 2, -10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(f, 2, 10, -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(f, 2, -10, 10, -1);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithExceededIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(f, 10, -10, 10, 10);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5.0, result, 0.0);
    }
}