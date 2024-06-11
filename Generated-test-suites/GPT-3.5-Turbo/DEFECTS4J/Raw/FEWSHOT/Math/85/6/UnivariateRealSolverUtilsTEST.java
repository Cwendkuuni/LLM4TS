package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        // Test case where a root exists
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5.0;
            }
        };
        try {
            double result1 = UnivariateRealSolverUtils.solve(f1, 0.0, 10.0);
            assertEquals(5.0, result1, 0.000001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test case where a root does not exist
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) + 1;
            }
        };
        try {
            UnivariateRealSolverUtils.solve(f2, 0.0, 10.0);
            fail("ConvergenceException should have been thrown");
        } catch (ConvergenceException e) {
            assertNotNull(e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test case where endpoints do not specify a valid interval
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5.0;
            }
        };
        try {
            UnivariateRealSolverUtils.solve(f3, 10.0, 0.0);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5.0;
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0.0, 10.0, 0.000001);
            assertEquals(5.0, result, 0.000001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5.0;
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 2.0, 0.0, 10.0);
            assertEquals(1.0, result[0], 0.000001);
            assertEquals(3.0, result[1], 0.000001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5.0;
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 2.0, 0.0, 10.0, 100);
            assertEquals(1.0, result[0], 0.000001);
            assertEquals(3.0, result[1], 0.000001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBracketWithInvalidParameters() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 5.0;
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f, 2.0, 10.0, 0.0);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double midpoint = UnivariateRealSolverUtils.midpoint(5.0, 10.0);
        assertEquals(7.5, midpoint, 0.000001);
    }

}