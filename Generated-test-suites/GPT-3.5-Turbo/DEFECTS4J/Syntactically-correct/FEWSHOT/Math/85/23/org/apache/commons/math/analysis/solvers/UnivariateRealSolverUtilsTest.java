package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    // test solve() method with default solver
    @Test
    public void testSolveDefaultSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 3);
            assertEquals(2, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // test solve() method with absoluteAccuracy
    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 3, 0.01);
            assertEquals(2, result, 0.01);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // test solve() method with null function
    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.solve(f, 0, 3);
    }

    // test solve() method with invalid interval
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.solve(f, 3, 0);
    }

    // test bracket() method
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 1, 0, 3);
            assertEquals(0, result[0], 0.0001);
            assertEquals(3, result[1], 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // test bracket() method with invalid function
    @Test(expected = IllegalArgumentException.class)
    public void testBracketNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.bracket(f, 1, 0, 3);
    }

    // test bracket() method with invalid bracketing parameters
    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidParameters() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.bracket(f, 1, 3, 0);
    }

    // test midpoint() method
    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 3);
        assertEquals(1.5, result, 0.0001);
    }

    // test setup() method with null function
    @Test(expected = IllegalArgumentException.class)
    public void testSetupNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }
}