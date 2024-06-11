import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    private static final double DELTA = 1e-15;

    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 4; // f(x) = x^2 - 4
            }
        };
    }

    @After
    public void tearDown() {
        function = null;
    }

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(function, 1, 3);
        Assert.assertEquals(2, result, DELTA);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(function, 1, 3, 1e-6);
        Assert.assertEquals(2, result, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(null, 1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(function, 3, 1); // lower bound > upper bound
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        double[] result = UnivariateRealSolverUtils.bracket(function, 1.5, 0, 2);
        Assert.assertArrayEquals(new double[]{1, 2}, result, DELTA);
    }

    @Test
    public void testBracketWithMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
        double[] result = UnivariateRealSolverUtils.bracket(function, 1.5, 0, 2, 10);
        Assert.assertArrayEquals(new double[]{1, 2}, result, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(null, 1.5, 0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInitial() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(function, -1, 0, 2); // initial < lower bound
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(function, 1.5, 2, 0); // lower bound > upper bound
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithNoBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(function, 0, 1, 2); // a and b do not bracket a root
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1, 3);
        Assert.assertEquals(2, result, DELTA);
    }

    @Test
    public void testSetUpWithValidFunction() {
        UnivariateRealSolverUtils.setup(function);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUpWithNullFunction() {
        UnivariateRealSolverUtils.setup(null);
    }
}