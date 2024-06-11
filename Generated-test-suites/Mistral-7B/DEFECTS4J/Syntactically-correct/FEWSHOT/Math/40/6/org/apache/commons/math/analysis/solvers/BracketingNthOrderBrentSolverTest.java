package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithIllegalMaximalOrder() {
        new BracketingNthOrderBrentSolver(1);
    }

    @Test
    public void testConstructorWithDefaults() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(BracketingNthOrderBrentSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0);
        assertEquals(BracketingNthOrderBrentSolver.DEFAULT_MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithSpecifiedAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 10);
        assertEquals(0.001, solver.getRelativeAccuracy(), 0);
        assertEquals(10, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithIllegalFunctionValueAccuracy() {
        new BracketingNthOrderBrentSolver(0.01, 10, 0.0);
    }

    @Test
    public void testDoSolveWithDefaults() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double result = solver.solve(10, f, -5, 5, AllowedSolution.ANY_SIDE);
        assertEquals(-1.4142135623730955, result, 1e-6);
    }

    @Test
    public void testDoSolveWithCustomSettings() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 8);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double result = solver.solve(10, f, -3, 3, AllowedSolution.ANY_SIDE);
        assertEquals(0.5396126656749373, result, 1e-9);
    }

    @Test(expected = NoBracketingException.class)
    public void testDoSolveWithNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        solver.solve(10, f, -2, 2);
    }

    @Test
    public void testGuessX() {
        double[] x = new double[10];
        double[] y = new double[10];
        double targetY = 0.5;
        x[0] = 1;
        y[0] = 2;
        x[1] = 2;
        y[1] = 1;
        x[2] = 3;
        y[2] = 0.5;
        x[3] = 4;
        y[3] = 0.5;
        x[4] = 5;
        y[4] = 1;
        x[5] = 6;
        y[5] = 2;
        x[6] = 7;
        y[6] = 3;
        x[7] = 8;
        y[7] = 4;
        x[8] = 9;
        y[8] = 5;

        double result = BracketingNthOrderBrentSolver.guessX(targetY, x, y, 0, 9);

        assertEquals(5.5, result, 1e-6);
    }

    @Test
    public void testSolveWithMinMaxValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double result = solver.solve(10, f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, AllowedSolution.ANY_SIDE);
        assertEquals(-1.4142135623730955, result, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double result = solver.solve(10, f, -3, 3, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(0.5396126656749373, result, 1e-6);
    }

    @Test
    public void testSolveWithIllegalAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        // illegal allowed solution
        try {
            solver.solve(10, f, -3, 3, 123, AllowedSolution.ABOVE_SIDE);
        } catch (MathIllegalArgumentException e) {
            assertEquals("Invalid allowed solution.", e.getMessage());
        }
    }

    @Test
    public void testSolveWithFunctionThrowsException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        try {
            solver.solve(10, f, -3, 3, AllowedSolution.ANY_SIDE);
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test
    public void testVerifySequence() {
        double[] x = new double[3];
        double[] y = new double[3];
        x[0] = -1;
        x[1] = 0;
        x[2] = 1;
        y[0] = 1;
        y[1] = 0;
        y[2] = -1;
        BracketingNthOrderBrentSolver.verifySequence(x[0], x[1], x[2]);
    }

}