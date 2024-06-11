package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.PrecisionModifiedException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.fast.DoubleArray;
import org.apache.commons.math3.util.fast.MathUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    private final double absoluteAccuracy;
    private final UnivariateRealFunction function;
    private final double maxEval;
    private final double min;
    private final double max;
    private final AllowedSolution allowedSolution;
    private final BaseSecantSolver solver;

    public BaseSecantSolverTest(double absoluteAccuracy, UnivariateRealFunction function, int maxEval, double min, double max, AllowedSolution allowedSolution) {
        this.absoluteAccuracy = absoluteAccuracy;
        this.function = function;
        this.maxEval = maxEval;
        this.min = min;
        this.max = max;
        this.allowedSolution = allowedSolution;
        this.solver = new PegasusSolver(absoluteAccuracy, maxEval, this.function);
    }

    @Parameterized.Parameters(name = "{index}: test method={0}, absoluteAccuracy={1}, maxEval={2}, min={3}, max={4}, allowedSolution={5}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1e-8, new TestFunction(), 20, -2.5, 2.5, AllowedSolution.ANY_SIDE},
                {1e-8, new TestFunction(), 20, -2.5, 2.5, AllowedSolution.LEFT_SIDE},
                {1e-8, new TestFunction(), 20, -2.5, 2.5, AllowedSolution.RIGHT_SIDE},
                {1e-8, new TestFunction(), 20, -2.5, 2.5, AllowedSolution.BELOW_SIDE},
                {1e-8, new TestFunction(), 20, -2.5, 2.5, AllowedSolution.ABOVE_SIDE},

                {1e-12, new TestFunctionWithComplexRoots(), 20, -1.5, 1.5, AllowedSolution.ANY_SIDE},
                {1e-12, new TestFunctionWithComplexRoots(), 20, -1.5, 1.5, AllowedSolution.LEFT_SIDE},
                {1e-12, new TestFunctionWithComplexRoots(), 20, -1.5, 1.5, AllowedSolution.RIGHT_SIDE},
                {1e-12, new TestFunctionWithComplexRoots(), 20, -1.5, 1.5, AllowedSolution.BELOW_SIDE},
                {1e-12, new TestFunctionWithComplexRoots(), 20, -1.5, 1.5, AllowedSolution.ABOVE_SIDE},

                {1e-10, new TestFunctionWithZeroDerivative(), 20, -2.5, 2.5, AllowedSolution.ANY_SIDE},
                {1e-10, new TestFunctionWithZeroDerivative(), 20, -2.5, 2.5, AllowedSolution.LEFT_SIDE},
                {1e-10, new TestFunctionWithZeroDerivative(), 20, -2.5, 2.5, AllowedSolution.RIGHT_SIDE},
                {1e-10, new TestFunctionWithZeroDerivative(), 20, -2.5, 2.5, AllowedSolution.BELOW_SIDE},
                {1e-10, new TestFunctionWithZeroDerivative(), 20, -2.5, 2.5, AllowedSolution.ABOVE_SIDE},

                {1e-6, new TestFunctionWithMultipleRoots(), 20, -3.5, 3.5, AllowedSolution.ANY_SIDE},
                {1e-6, new TestFunctionWithMultipleRoots(), 20, -3.5, 3.5, AllowedSolution.LEFT_SIDE},
                {1e-6, new TestFunctionWithMultipleRoots(), 20, -3.5, 3.5, AllowedSolution.RIGHT_SIDE},
                {1e-6, new TestFunctionWithMultipleRoots(), 20, -3.5, 3.5, AllowedSolution.BELOW_SIDE},
                {1e-6, new TestFunctionWithMultipleRoots(), 20, -3.5, 3.5, AllowedSolution.ABOVE_SIDE}
        });
    }

    @Test
    public void testSolve() {
        try {
            double result = solver.solve(maxEval, function, min, max, allowedSolution);
            double expected = function.root(allowedSolution);
            assertEquals(expected, result, Precision.SAFE_EPSILON);
        } catch (TooManyIterationsException | MaxCountExceededException | PrecisionModifiedException e) {
            fail("Solver should have found the root within given tolerances and number of evaluations.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAllowedSolution() {
        new PegasusSolver(absoluteAccuracy, maxEval, function, AllowedSolution.INVALID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBracketing() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * x;
            }
        };
        new PegasusSolver(absoluteAccuracy, maxEval, function, min, max);
    }

    @Test
    public void testMaxEvalReached() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * x;
            }
        };
        try {
            solver.solve(0, function, min, max, allowedSolution);
            fail("Expected MathInternalError when maxEval is reached.");
        } catch (MathInternalError e) {
            // Expected
        }
    }

    private static class TestFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return x * x - 4.0;
        }

        public double root(AllowedSolution allowedSolution) {
            switch (allowedSolution) {
                case LEFT_SIDE:
                    return -2.0;
                case RIGHT_SIDE:
                    return 2.0;
                case ANY_SIDE:
                    return 0.0;
                case ABOVE_SIDE:
                    return 2.0;
                case BELOW_SIDE:
                    return -2.0;
                default:
                    throw new UnsupportedOperationException("Unsupported AllowedSolution: " + allowedSolution);
            }
        }

    }

    private static class TestFunctionWithComplexRoots implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return (Math.pow(x - 1.0, 2.0) + Math.pow(x + 1.0, 2.0)) / 4.0;
        }

        public double root(AllowedSolution allowedSolution) {
            switch (allowedSolution) {
                case LEFT_SIDE:
                    return -1.5 - Math.sqrt(0.25);
                case RIGHT_SIDE:
                    return 1.5 + Math.sqrt(0.25);
                case ANY_SIDE:
                    return 0.0;
                case ABOVE_SIDE:
                    return 1.5;
                case BELOW_SIDE:
                    return -1.5;
                default:
                    throw new UnsupportedOperationException("Unsupported AllowedSolution: " + allowedSolution);
            }
        }

    }

    private static class TestFunctionWithZeroDerivative implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x - 2.5, 2.0);
        }

        public double root(AllowedSolution allowedSolution) {
            switch (allowedSolution) {
                case LEFT_SIDE:
                    return -2.5;
                case RIGHT_SIDE:
                    return 2.5;
                case ANY_SIDE:
                    return 0.0;
                case ABOVE_SIDE:
                    return 2.5;
                case BELOW_SIDE:
                    return -2.5;
                default:
                    throw new UnsupportedOperationException("Unsupported AllowedSolution: " + allowedSolution);
            }
        }

        @Override
        public double derivative(double x) {
            return 4.0 * (x - 2.5);
        }

    }

    private static class TestFunctionWithMultipleRoots implements UnivariateRealFunction {

        private static final double EPSILON = 1e-8;

        @Override
        public double value(double x) {
            return Math.sin(x) * Math.cos(x) - 0.2;
        }

        public double root(AllowedSolution allowedSolution) {
            switch (allowedSolution) {
                case LEFT_SIDE:
                    return Math.PI - Math.PI / 2.0 - EPSILON;
                case RIGHT_SIDE:
                    return Math.PI / 2.0 + EPSILON;
                case ANY_SIDE:
                    return Math.PI;
                case ABOVE_SIDE:
                    return Math.PI / 2.0;
                case BELOW_SIDE:
                    return Math.PI - Math.PI / 2.0;
                default:
                    throw new UnsupportedOperationException("Unsupported AllowedSolution: " + allowedSolution);
            }
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x) * (Math.sin(x) - Math.cos(x)) + Math.sin(x) * (-Math.cos(x) - Math.sin(x));
        }

    }

}