package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with default constructor, {@link BaseSecantSolver.Method#REGULA_FALSI}.
     */
    @Test
    public void testRegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        try {
            double root = solver.solve(10, f, -2.5, 2.5, 0.0);
            assertEquals(0.0, root, 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("Solve should find root within 10 evaluations.");
        }

        try {
            double root = solver.solve(1, f, -2.5, 2.5, 0.0);
            fail("Solve should require more than 1 evaluation.");
        } catch (MaxEvalExceededException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, f, -2.5, 2.5, Double.POSITIVE_INFINITY);
            fail("Solve should throw NumberIsTooLargeException for infinite start value.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, f, -2.5, 2.5, Double.NEGATIVE_INFINITY);
            fail("Solve should throw NumberIsTooLargeException for negative start value.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, null, -2.5, 2.5, 0.0);
            fail("Solve should throw UnsupportedOperationException for null function.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, f, -2.5, 2.5, 0.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
            fail("Solve should throw UnsupportedOperationException for above solution.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with constructor with relative and absolute accuracies,
     * {@link BaseSecantSolver.Method#ILLINOIS}.
     */
    @Test
    public void testIllinois() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-8, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };

        try {
            double root = solver.solve(10, f, -4, 4, 0.0);
            assertEquals(0.0, root, 1e-9);
        } catch (MaxEvalExceededException e) {
            fail("Solve should find root within 10 evaluations.");
        }

        try {
            double root = solver.solve(1, f, -4, 4, 0.0);
            fail("Solve should require more than 1 evaluation.");
        } catch (MaxEvalExceededException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, f, -4, 4, Double.POSITIVE_INFINITY);
            fail("Solve should throw NumberIsTooLargeException for infinite start value.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, f, -4, 4, Double.NEGATIVE_INFINITY);
            fail("Solve should throw NumberIsTooLargeException for negative start value.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, null, -4, 4, 0.0);
            fail("Solve should throw UnsupportedOperationException for null function.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, f, -4, 4, 0.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
            fail("Solve should throw UnsupportedOperationException for above solution.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, double)}
     * with constructor with relative, absolute, and function value accuracies,
     * {@link BaseSecantSolver.Method#PEGASUS}.
     */
    @Test
    public void testPegasus() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-8, 1e-8, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.pow(x, 2);
            }
        };

        try {
            double root = solver.solve(10, f, -3, 3, 0.0);
            assertEquals(1.0, root, 1e-9);
        } catch (MaxEvalExceededException e) {
            fail("Solve should find root within 10 evaluations.");
        }

        try {
            double root = solver.solve(1, f, -3, 3, 0.0);
            fail("Solve should require more than 1 evaluation.");
        } catch (MaxEvalExceededException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, f, -3, 3, Double.POSITIVE_INFINITY);
            fail("Solve should throw NumberIsTooLargeException for infinite start value.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, f, -3, 3, Double.NEGATIVE_INFINITY);
            fail("Solve should throw NumberIsTooLargeException for negative start value.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, null, -3, 3, 0.0);
            fail("Solve should throw UnsupportedOperationException for null function.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, f, -3, 3, 0.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
            fail("Solve should throw UnsupportedOperationException for above solution.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}