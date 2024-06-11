import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

public class UnivariateRealSolverUtilsTest {

    private UnivariateRealFunction function;

    @Before
    public void setUp() throws Exception {
        function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // Example function x^2 - 4
            }
        };
    }

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(function, 0.0, 10.0);
        assertEquals(2.0, result, 0.0001); // Expected root of x^2 - 4 is 2
    }

    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(function, 0.0, 10.0, 0.01);
        assertEquals(2.0, result, 0.01); // Expected root of x^2 - 4 is 2
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        double[] result = UnivariateRealSolverUtils.bracket(function, 1.0, -10.0, 10.0);
        assertEquals(-2.0, result[0], 0.0001); // Expected bracketing lower bound is -2
        assertEquals(2.0, result[1], 0.0001); // Expected bracketing upper bound is 2
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketInvalidParams() throws ConvergenceException, FunctionEvaluationException {
        double[] result = UnivariateRealSolverUtils.bracket(function, 1.0, 3.0, 2.0);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1.0, 3.0);
        assertEquals(2.0, result, 0.0001); // Expected midpoint is 2
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetupNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }
}