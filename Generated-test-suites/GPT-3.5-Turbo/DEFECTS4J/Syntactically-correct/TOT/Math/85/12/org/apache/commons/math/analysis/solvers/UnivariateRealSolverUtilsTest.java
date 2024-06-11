import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UnivariateRealSolverUtilsTest {

    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() {
        try {
            double result = UnivariateRealSolverUtils.solve(function, 0, 5);
            assertEquals(2, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        try {
            double result = UnivariateRealSolverUtils.solve(function, 0, 5, 0.001);
            assertEquals(2, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testSolveWithNullFunction() {
        try {
            UnivariateRealFunction nullFunction = null;
            UnivariateRealSolverUtils.solve(nullFunction, 0, 5);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("function is null"));
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Wrong Exception thrown");
        }
    }

    @Test
    public void testSolveWithInvalidInterval() {
        try {
            UnivariateRealFunction invalidFunction = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return x;
                }
            };
            UnivariateRealSolverUtils.solve(invalidFunction, 5, 0);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("the endpoints do not specify a valid interval"));
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Wrong Exception thrown");
        }
    }

    @Test
    public void testBracket() {
        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, 2, 0, 5);
            assertEquals(1, result[0], 0.001);
            assertEquals(3, result[1], 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testBracketWithInvalidBounds() {
        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, 2, 5, 0);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("invalid bracketing parameters"));
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Wrong Exception thrown");
        }
    }

    @Test
    public void testBracketWithInvalidFunction() {
        try {
            UnivariateRealFunction nullFunction = null;
            UnivariateRealSolverUtils.bracket(nullFunction, 2, 0, 5);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("function is null"));
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Wrong Exception thrown");
        }
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(2, 5);
        assertEquals(3.5, result, 0.001);
    }

}