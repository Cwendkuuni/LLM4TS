package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** Test function. */
    private static final UnivariateFunction FUNCTION =
            new UnivariateFunction() {
                @Override
                public double value(double x) {
                    return x * x * x - 2.0 * x + 1.0;
                }
            };

    /**
     * Test constructor with default arguments and default method.
     */
    @Test
    public void testDefaultConstructorAndMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        assertEquals(0.0, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
        assertNotNull(solver.getMinimum());
        assertNotNull(solver.getMaximum());
    }

    /**
     * Test constructor with custom arguments and default method.
     */
    @Test
    public void testCustomConstructorAndMethod() {
        double absoluteAccuracy = 1e-8;
        double relativeAccuracy = 1e-5;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
        assertNotNull(solver.getMinimum());
        assertNotNull(solver.getMaximum());
    }

    /**
     * Test constructor with custom arguments and custom method.
     */
    @Test
    public void testCustomConstructorAndCustomMethod() {
        double absoluteAccuracy = 1e-8;
        double relativeAccuracy = 1e-5;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                BaseSecantSolver.Method.ILLINOIS);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
        assertNotNull(solver.getMinimum());
        assertNotNull(solver.getMaximum());
    }

    /**
     * Test solve method with default arguments and default method.
     */
    @Test
    public void testSolveDefaultConstructorAndMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        double result = solver.solve(FUNCTION, 0.0, 1.0);
        double expected = 0.75;
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test solve method with custom arguments and default method.
     */
    @Test
    public void testSolveCustomConstructorAndMethod() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-8;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy);
        double result = solver.solve(FUNCTION, 0.0, 1.0);
        double expected = 0.75;
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test solve method with custom arguments and custom method.
     */
    @Test
    public void testSolveCustomConstructorAndCustomMethod() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-8;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(FUNCTION, 0.0, 1.0);
        double expected = 0.75;
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test solve method with minimum and maximum out of order.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveMinMaxOutOfOrder() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        solver.solve(FUNCTION, 1.0, 0.0);
    }

    /**
     * Test solve method with minimum equal to maximum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveMinMaxEqual() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        solver.solve(FUNCTION, 0.0, 0.0);
    }

    /**
     * Test solve method with relative accuracy less than required.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveRelativeAccuracyLessThanRequired() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-9, 0.0);
        solver.solve(FUNCTION, 0.0, 1.0);
    }

    /**
     * Test solve method with absolute accuracy less than required.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveAbsoluteAccuracyLessThanRequired() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, 1e-9);
        solver.solve(FUNCTION, 0.0, 1.0);
    }

    /**
     * Test solve method with method not implemented.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveMethodNotImplemented() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        solver.solve(FUNCTION, 0.0, 1.0);
    }

    /**
     * Test solve method with function value accuracy less than required.
     */
    @Test
    public void testSolveFunctionValueAccuracyLessThanRequired() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-8;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                1e-9);
        double result = solver.solve(FUNCTION, 0.0, 1.0);
        double expected = 0.75;
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test solve method with maxIterationsExceeded exception.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-8;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                BaseSecantSolver.Method.REGULA_FALSI, 1) {
            @Override
            protected double doSolve() {
                throw new MaxIterationsExceededException();
            }
        };
        solver.solve(FUNCTION, 0.0, 1.0);
    }

    /**
     * Test solve method with convergence exception.
     */
    @Test
    public void testSolveConvergenceException() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-8;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new ConvergenceException();
            }
        };
        double result = solver.solve(FUNCTION, 0.0, 1.0);
        assertEquals(Double.NaN, result, 0.0);
    }

    /**
     * Test solve method with number is too small exception.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveNumberIsTooSmallException() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-8;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() {
                throw new NumberIsTooSmallException();
            }
        };
        solver.solve(FUNCTION, 0.0, 1.0);
    }

    /**
     * Test solve method with too many iterations exception.
     */
    @Test(expected = TooManyIterationsException.class)
    public void testSolveTooManyIterationsException() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-8;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                throw new TooManyIterationsException();
            }
        };
        solver.solve(FUNCTION, 0.0, 1.0);
    }

    /**
     * Test allowed solution types.
     */
    @Test
    public void testAllowedSolutions() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f,
                                double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue,
                        AllowedSolution.LEFT_SIDE);
            }
        };
        double result = solver.solve(FUNCTION, 0.0, 1.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test solve method with allowed solution types.
     */
    @Test
    public void testSolveAllowedSolutions() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-8;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                BaseSecantSolver.Method.ILLINOIS) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f,
                                double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue,
                        AllowedSolution.LEFT_SIDE);
            }
        };
        double result = solver.solve(FUNCTION, 0.0, 1.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-6);

        AllowedSolution allowed = AllowedSolution.RIGHT_SIDE;
        solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                BaseSecantSolver.Method.ILLINOIS) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f,
                                double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowed);
            }
        };
        result = solver.solve(FUNCTION, 0.0, 1.0);
        expected = 0.0;
        assertEquals(expected, result, 1e-6);
    }
}

enum AllowedSolution {
    LEFT_SIDE,
    RIGHT_SIDE,
    ANY_SIDE,
    BELOW_SIDE,
    ABOVE_SIDE;
}