import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    private static UnivariateRealFunction function;

    @BeforeClass
    public static void setUp() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(function, 0, 5);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        double result = UnivariateRealSolverUtils.solve(function, 0, 5, 0.001);
        assertEquals(2.0, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(function, 5, 0);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        double[] result = UnivariateRealSolverUtils.bracket(function, 0, -10, 10);
        assertEquals(-2.0, result[0], 0.0001);
        assertEquals(2.0, result[1], 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(function, 0, 5, 0);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 4);
        assertEquals(2.0, result, 0.0001);
    }
}