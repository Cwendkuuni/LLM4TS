import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;

public class UnivariateRealSolverUtilsTest {

    private UnivariateRealFunction constantFunction;
    private UnivariateRealFunction linearFunction;
    private UnivariateRealFunction quadraticFunction;

    @Before
    public void setUp() {
        constantFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return 5.0;
            }
        };

        linearFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x + 3;
            }
        };

        quadraticFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return 3 * x * x + 2 * x + 1;
            }
        };
    }

    @Test
    public void testSolveWithDefaultSolver() throws ConvergenceException, FunctionEvaluationException {
        assertEquals(-1.0, UnivariateRealSolverUtils.solve(linearFunction, -10, 0), 0.0001);
        assertEquals(1.0, UnivariateRealSolverUtils.solve(linearFunction, 0, 10), 0.0001);
        assertEquals(0.0, UnivariateRealSolverUtils.solve(quadraticFunction, -10, 10), 0.0001);
        assertEquals(5.0, UnivariateRealSolverUtils.solve(constantFunction, -10, 10), 0.0001);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        assertEquals(-1.0, UnivariateRealSolverUtils.solve(linearFunction, -10, 0, 0.001), 0.0001);
        assertEquals(1.0, UnivariateRealSolverUtils.solve(linearFunction, 0, 10, 0.001), 0.0001);
        assertEquals(0.0, UnivariateRealSolverUtils.solve(quadraticFunction, -10, 10, 0.001), 0.0001);
        assertEquals(5.0, UnivariateRealSolverUtils.solve(constantFunction, -10, 10, 0.001), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(null, -10, 10, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(linearFunction, 10, -10, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(linearFunction, -10, 10, -0.001);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        double[] result = UnivariateRealSolverUtils.bracket(linearFunction, 0, -10, 10);
        assertEquals(-1.0, result[0], 0.0001);
        assertEquals(1.0, result[1], 0.0001);

        result = UnivariateRealSolverUtils.bracket(quadraticFunction, 0, -10, 10);
        assertEquals(-5.0, result[0], 0.0001);
        assertEquals(5.0, result[1], 0.0001);

        result = UnivariateRealSolverUtils.bracket(constantFunction, 0, -10, 10);
        assertEquals(-2.0, result[0], 0.0001);
        assertEquals(4.0, result[1], 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithInvalidFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(null, 0, -10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(linearFunction, 10, -10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(linearFunction, 0, -10, 10, -1);
    }

    @Test
    public void testMidpoint() {
        assertEquals(0.0, UnivariateRealSolverUtils.midpoint(-1, 1), 0.0001);
        assertEquals(2.5, UnivariateRealSolverUtils.midpoint(2, 3), 0.0001);
        assertEquals(-2.5, UnivariateRealSolverUtils.midpoint(-2, -3), 0.0001);
    }
}