package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BracketedRealRootFinder;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.junit.Test;

/**
 * JUnit 4 test file for the UnivariateRealSolverUtils class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testSolve() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double x0 = 2.0;
        double x1 = 1.0;
        double expected = -1.0;
        double actual = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(expected, actual, 1e-6);
        
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(1 + x) - x / 2;
            }
        };
        double x2 = 1.0;
        double x3 = 3.0;
        double absoluteAccuracy = 1e-6;
        double actual2 = UnivariateRealSolverUtils.solve(f2, x2, x3, absoluteAccuracy);
        assertEquals(Math.E, actual2, absoluteAccuracy);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testBracket() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        double[] expected = {1.5, 2.5};
        double[] actual = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertArrayEquals(expected, actual, 1e-6);
        
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(1 + x) - x / 2;
            }
        };
        double initial2 = 1.0;
        double lowerBound2 = 0.5;
        double upperBound2 = 2.0;
        double[] expected2 = {0.739085133345642, 1.260914866654358};
        double[] actual2 = UnivariateRealSolverUtils.bracket(f2, initial2, lowerBound2, upperBound2);
        assertArrayEquals(expected2, actual2, 1e-6);
        
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 3) - 1;
            }
        };
        double initial3 = 2.0;
        double lowerBound3 = 1.0;
        double upperBound3 = 3.0;
        try {
            UnivariateRealSolverUtils.bracket(f3, initial3, lowerBound3, upperBound3, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f3, initial3, lowerBound3, upperBound3, Integer.MAX_VALUE);
            fail();
        } catch (ConvergenceException e) {
            // expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f3, initial3, lowerBound3, upperBound3, 1);
            fail();
        } catch (TooManyIterationsException e) {
            // expected
        }

        try {
            UnivariateRealSolverUtils.bracket(null, initial3, lowerBound3, upperBound3, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f3, initial3, lowerBound3, upperBound3, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f3, initial3, lowerBound3, upperBound3, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f3, initial3, lowerBound3, upperBound3, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        UnivariateRealFunction f4 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x - 1;
            }
        };
        double initial4 = Math.PI;
        double lowerBound4 = 0.5;
        double upperBound4 = 2.0;
        try {
            UnivariateRealSolverUtils.bracket(f4, initial4, lowerBound4, upperBound4, 1);
            fail();
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }
}