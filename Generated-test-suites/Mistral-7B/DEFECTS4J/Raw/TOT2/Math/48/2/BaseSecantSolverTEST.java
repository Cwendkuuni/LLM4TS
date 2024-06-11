package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A simple quadratic function for testing. */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(double x) {
            return x * x - 4;
        }

        @Override
        public double derivative(double x) {
             return 2 * x;
        }
    };

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with default constructor and default method.
     */
    @Test
    public void testDefaultConstructorAndMethod() {
        final double root = Math.sqrt(2.0);
        final BaseSecantSolver solver = new BaseSecantSolver(FUNCTION);
        assertEquals(root, solver.solve(100), Precision.EPSILON);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with custom constructions.
     */
    @Test
    public void testCustomConstruction() {
        final double root = Math.sqrt(2.0);
        final double relativeAccuracy = 0.0001;
        final double absoluteAccuracy = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, FUNCTION);
        assertEquals(root, solver.solve(100), Precision.EPSILON);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with an incorrect function.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethod() {
        final UnivariateRealFunction unsupportedFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double derivative(double x) {
                return 0.0;
            }
        };
        new BaseSecantSolver(unsupportedFunction).solve(100);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a null function.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        new BaseSecantSolver(null).solve(100);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that does not bracket the root.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoBracketing() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };
        new BaseSecantSolver(function).solve(100);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that does not converge.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testNoConvergence() {
        final UnivariateRealFunction nonConvergentFunction = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }

            @Override
            public double derivative(double x) {
                return (Math.cos(x) / x - Math.sin(x)) / x;
            }
        };
        new BaseSecantSolver(nonConvergentFunction).solve(1000);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has multiple roots.
     */
    @Test(expected = ConvergenceException.class)
    public void testMultipleRoots() {
        final UnivariateRealFunction multipleRootFunction = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return x * (x - 1);
            }

            @Override
            public double derivative(double x) {
                return 2 * x - 1;
            }
        };
        new BaseSecantSolver(multipleRootFunction).solve(100);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with a function that has multiple roots and a specific solution.
     */
    @Test
    public void testMultipleRootsWithSpecificSolution() {
        final UnivariateRealFunction multipleRootFunction = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return x * (x - 1);
            }

            @Override
            public double derivative(double x) {
                return 2 * x - 1;
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(multipleRootFunction, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, solver.solve(100), Precision.EPSILON);
    }
}