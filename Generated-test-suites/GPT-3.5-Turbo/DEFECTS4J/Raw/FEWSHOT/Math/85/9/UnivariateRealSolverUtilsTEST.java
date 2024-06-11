package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, -2, 2);
            assertEquals(2.0, result, 0.0001);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.solve(f, -2, 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        UnivariateRealSolverUtils.solve(f, 2, -2);
    }
    
    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, -2, 2, 0.01);
            assertEquals(2.0, result, 0.01);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 2, -4, 4);
            assertNotNull(result);
            assertEquals(1.0, result[0], 0.0001);
            assertEquals(3.0, result[1], 0.0001);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testBracketInvalidParameters() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f, 2, 4, -4);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Wrong exception type");
        }
    }
    
    @Test
    public void testBrackedMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 2, -10, 10, 5);
            assertNotNull(result);
            assertTrue(result[0] <= result[1]);
        } catch (ConvergenceException e) {
            // This is the expected behavior
        } catch (Exception e) {
            fail("Wrong exception type");
        }
    }
    
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;
        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, midpoint, 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetupNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }
    
}