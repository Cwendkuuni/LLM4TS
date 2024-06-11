import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {
    private UnivariateRealFunction f;

    @Before
    public void setUp() {
        f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() {
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 5);
            assertEquals(2, result, 0.0001);
        } catch (ConvergenceException e) {
            fail("ConvergenceException: " + e.getMessage());
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 5, 0.0001);
            assertEquals(2, result, 0.0001);
        } catch (ConvergenceException e) {
            fail("ConvergenceException: " + e.getMessage());
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException: " + e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 2, 0, 5);
            assertArrayEquals(new double[] {1, 3}, result, 0.0001);
        } catch (ConvergenceException e) {
            fail("ConvergenceException: " + e.getMessage());
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException: " + e.getMessage());
        }
    }

    @Test
    public void testBracketWithIterations() {
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 2, 0, 5, 10);
            assertArrayEquals(new double[] {1, 3}, result, 0.0001);
        } catch (ConvergenceException e) {
            fail("ConvergenceException: " + e.getMessage());
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException: " + e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1, 3);
        assertEquals(2, result, 0.0001);
    }
}