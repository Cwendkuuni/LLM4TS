package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    /**
     * Test constructor with function.
     */
    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        assertNotNull(solver);
        assertEquals(function, solver.getFunction());
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double)} with valid inputs.
     */
    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 0.5;
            }
        };

        double expected = 1.00115;
        double result = new BisectionSolver().solve(function, -1.0, 1.0);
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double)} with initial value inside interval.
     */
    @Test
    public void testSolveWithInitialValue() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 4.0;
            }
        };

        double expected = 2.0;
        double result = new BisectionSolver().solve(function, -3.0, 3.0, 1.0);
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double)} with function throwing NumberIsTooSmallException.
     */
    @Test
    public void testSolveWithNumberIsTooSmallException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    throw new NumberIsTooSmallException(x, 1.0);
                }
                return x;
            }
        };

        assertThrows(MaxIterationsExceededException.class, () -> new BisectionSolver().solve(function, -1.0, 1.0));
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double)} with function throwing UnsupportedOperationException.
     */
    @Test
    public void testSolveWithUnsupportedOperationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Not supported.");
            }
            @Override
            public double derivative(double x) {
                return 0.0;
            }
        };

        assertThrows(UnsupportedOperationException.class, () -> new BisectionSolver().solve(function, -1.0, 1.0));
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double)} with function not bracketing the root.
     */
    @Test
    public void testSolveWithFunctionNotBracketingTheRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        assertThrows(MaxIterationsExceededException.class, () -> new BisectionSolver().solve(function, -1.0, 1.0));
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double)} with function not continuous.
     */
    @Test
    public void testSolveWithFunctionNotContinuous() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    return Math.cos(x) / x;
                } else {
                    return -Math.cos(-x) / x;
                }
            }
        };

        assertThrows(UnsupportedOperationException.class, () -> new BisectionSolver().solve(function, -1.0, 1.0));
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double, double)} with valid inputs.
     */
    @Test
    public void testSolveWithInitialValueAndPrecision() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 0.5;
            }
        };

        double expected = 1.00115;
        double initial = 0.1;
        double precision = 1e-6;
        double result = new BisectionSolver().solve(function, -1.0, 1.0, initial, precision);
        assertEquals(expected, result, precision);
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double)} with initial value outside interval.
     */
    @Test
    public void testSolveWithInitialValueOutsideInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 4.0;
            }
        };

        assertThrows(IllegalArgumentException.class, () -> new BisectionSolver().solve(function, 3.0, 3.0));
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double)} with interval too small.
     */
    @Test
    public void testSolveWithIntervalTooSmall() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 4.0;
            }
        };

        assertThrows(IllegalArgumentException.class, () -> new BisectionSolver().solve(function, 0.0, 0.0));
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double)} with interval not bracketing the root.
     */
    @Test
    public void testSolveWithIntervalNotBracketingTheRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> new BisectionSolver().solve(function, -1.0, 1.0));
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double, double)} with initial value outside interval and precision.
     */
    @Test
    public void testSolveWithInitialValueOutsideIntervalAndPrecision() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 4.0;
            }
        };

        assertThrows(IllegalArgumentException.class, () -> new BisectionSolver().solve(function, 3.0, 5.0, 0.2));
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double, double)} with precision too small.
     */
    @Test
    public void testSolveWithPrecisionTooSmall() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 4.0;
            }
        };

        assertThrows(IllegalArgumentException.class, () -> new BisectionSolver().solve(function, -1.0, 3.0, 0.0));
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double, double)} with function not continuous.
     */
    @Test
    public void testSolveWithFunctionNotContinuousAndPrecision() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    return Math.cos(x) / x;
                } else {
                    return -Math.cos(-x) / x;
                }
            }
        };

        assertThrows(UnsupportedOperationException.class, () -> new BisectionSolver().solve(function, -1.0, 1.0, 0.01));
    }

    /**
     * Test {@link BisectionSolver#solve(UnivariateRealFunction, double, double, double)} with function not bracketing the root.
     */
    @Test
    public void testSolveWithFunctionNotBracketingTheRootAndPrecision() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        assertThrows(MaxIterationsExceededException.class, () -> new BisectionSolver().solve(function, -1.0, 1.0, 0.01));
    }
}