
package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        // Test case 1
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 3;
            }
        };
        double result = UnivariateRealSolverUtils.solve(function, 2, 4);
        assertEquals(3.0, result, 0.0001);

        // Test case 2
        function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        result = UnivariateRealSolverUtils.solve(function, 2, 4);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(null, 2, 4);
    }

    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(function, 2, 4, 0.01);
        assertEquals(2.0, result, 0.01);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(function, 2, 0, 5);
        assertNotNull(result);
        assertEquals(1.0, result[0], 0.0001);
        assertEquals(3.0, result[1], 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(null, 2, 0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidParameters() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        UnivariateRealSolverUtils.bracket(function, 6, 2, 5);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(2, 4);
        assertEquals(3.0, result, 0.0001);
    }
}
