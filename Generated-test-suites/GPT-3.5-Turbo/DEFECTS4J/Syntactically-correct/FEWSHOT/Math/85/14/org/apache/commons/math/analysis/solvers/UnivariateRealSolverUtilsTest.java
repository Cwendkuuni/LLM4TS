
package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {
    private static final double EPSILON = 1e-10;

    @Test
    public void testSolve() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        assertEquals(2.0, UnivariateRealSolverUtils.solve(f, 1.0, 3.0), EPSILON);
        assertEquals(-2.0, UnivariateRealSolverUtils.solve(f, -3.0, -1.0), EPSILON);
    }

    @Test
    public void testSolveWithAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        assertEquals(2.0, UnivariateRealSolverUtils.solve(f, 1.0, 3.0, 1e-6), EPSILON);
        assertEquals(-2.0, UnivariateRealSolverUtils.solve(f, -3.0, -1.0, 1e-6), EPSILON);
    }

    @Test
    public void testBracket() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double[] bracket = UnivariateRealSolverUtils.bracket(f, 2.0, 1.0, 3.0);
        assertEquals(1.0, bracket[0], EPSILON);
        assertEquals(3.0, bracket[1], EPSILON);
    }

    @Test
    public void testBracketWithMaximumIterations() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        double[] bracket = UnivariateRealSolverUtils.bracket(f, 2.0, 1.0, 3.0, 100);
        assertEquals(1.0, bracket[0], EPSILON);
        assertEquals(3.0, bracket[1], EPSILON);
    }

    @Test
    public void testMidpoint() {
        assertEquals(2.5, UnivariateRealSolverUtils.midpoint(2.0, 3.0), EPSILON);
        assertEquals(-0.5, UnivariateRealSolverUtils.midpoint(-1.0, 0.0), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealSolverUtils.solve(null, 1.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.solve(f, 3.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidAccuracy() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.solve(f, 1.0, 2.0, -1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealSolverUtils.bracket(null, 2.0, 1.0, 3.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.bracket(f, 2.0, 3.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNegativeMaximumIterations() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.bracket(f, 2.0, 1.0, 3.0, -100);
    }
}
