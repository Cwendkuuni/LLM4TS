package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 5);
            assertEquals(2, result, 0.001);
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 5, 0.001);
            assertEquals(2, result, 0.001);
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 1, -2, 4);
            assertEquals(-1, result[0], 0.001);
            assertEquals(3, result[1], 0.001);
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 1, -2, 4, 10);
            assertEquals(-1, result[0], 0.001);
            assertEquals(3, result[1], 0.001);
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 4);
        assertEquals(2, result, 0.001);
    }
}