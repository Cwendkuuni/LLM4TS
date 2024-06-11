import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {
    @Test
    public void testSolve() {
        // Test with a simple linear function
        UnivariateRealFunction linearFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 2 * x + 1;
            }
        };

        double solution = 0;

        try {
            solution = UnivariateRealSolverUtils.solve(linearFunction, -10, 10);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }

        assertEquals(-0.5, solution, 0.0001);

        // Test with a quadratic function
        UnivariateRealFunction quadraticFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            solution = UnivariateRealSolverUtils.solve(quadraticFunction, 0, 10);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }

        assertEquals(2, solution, 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() {
        // Test with a simple linear function and custom accuracy
        UnivariateRealFunction linearFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 2 * x + 1;
            }
        };

        double solution = 0;

        try {
            solution = UnivariateRealSolverUtils.solve(linearFunction, -10, 10, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }

        assertEquals(-0.5, solution, 0.001);

        // Test with a quadratic function and custom accuracy
        UnivariateRealFunction quadraticFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            solution = UnivariateRealSolverUtils.solve(quadraticFunction, 0, 10, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }

        assertEquals(2, solution, 0.001);
    }

    @Test
    public void testBracket() {
        // Test with a simple linear function
        UnivariateRealFunction linearFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 2 * x + 1;
            }
        };

        double[] bracket = null;

        try {
            bracket = UnivariateRealSolverUtils.bracket(linearFunction, 0, -10, 10);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }

        assertEquals(-1, bracket[0], 0.0001);
        assertEquals(1, bracket[1], 0.0001);

        // Test with a quadratic function
        UnivariateRealFunction quadraticFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        try {
            bracket = UnivariateRealSolverUtils.bracket(quadraticFunction, 0, -10, 10);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }

        assertEquals(-2, bracket[0], 0.0001);
        assertEquals(2, bracket[1], 0.0001);
    }

    @Test
    public void testMidpoint() {
        double midpoint = UnivariateRealSolverUtils.midpoint(0, 4);
        assertEquals(2, midpoint, 0.0001);

        midpoint = UnivariateRealSolverUtils.midpoint(-10, 10);
        assertEquals(0, midpoint, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunctionInSolve() {
        UnivariateRealFunction function = null;

        try {
            UnivariateRealSolverUtils.solve(function, -10, 10);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBoundsInSolve() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 2 * x + 1;
            }
        };

        try {
            UnivariateRealSolverUtils.solve(function, 10, -10);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunctionInBracket() {
        UnivariateRealFunction function = null;

        try {
            UnivariateRealSolverUtils.bracket(function, 0, -10, 10);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBoundsInBracket() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 2 * x + 1;
            }
        };

        try {
            UnivariateRealSolverUtils.bracket(function, 0, 10, -10);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidIterationsInBracket() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 2 * x + 1;
            }
        };

        try {
            UnivariateRealSolverUtils.bracket(function, 0, -10, 10, 0);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }
    }
}