package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.NonUnaryFunction;
import org.apache.commons.math3.optim.points.RealNVector;
import org.apache.commons.math3.optim.points.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the MultiDirectional class.
 */
public class MultiDirectionalTest {

    /** Test function for optimization. */
    private static final NonUnaryFunction FUNCTION = new NonUnaryFunction() {
        @Override
        public double value(double x) {
            return Math.sin(x) * Math.exp(-x / 3.0);
        }

        @Override
        public RealNVector gradient(double x) {
            return new org.apache.commons.math3.optim.points.ContinuousPointFunction(x) {
                @Override
                public double value() {
                    return MultiDirectionalTest.FUNCTION.value(x);
                }

                @Override
                public double[] gradient() {
                    return new double[] {-Math.sin(x) / 3.0 - Math.cos(x), Math.cos(x) / 3.0 - Math.sin(x)};
                }
            }.value();
        }
    };

    /** Test function bounds. */
    private static final SimpleBounds BOUNDS = new SimpleBounds(-5.0, 5.0);

    /** Test optimizer. */
    private MultiDirectional optimizer;

    /** Test comparator. */
    private Comparator<RealPointValuePair> comparator;

    /** Test convergence checker. */
    private RealConvergenceChecker convergenceChecker;

    /** Test simplex. */
    private RealPointValuePair[] simplex;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getFunctionValue(), o2.getFunctionValue());
            }
        };
        convergenceChecker = new RealConvergenceChecker(1e-6);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
    }

    /**
     * Tests the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        optimizer = new MultiDirectional();
        assertEquals(2.0, optimizer.khi, 0.001);
        assertEquals(0.5, optimizer.gamma, 0.001);
    }

    /**
     * Tests the constructor with specified coefficients.
     */
    @Test
    public void testSpecifiedConstructor() {
        optimizer = new MultiDirectional(3.0, 0.8);
        assertEquals(3.0, optimizer.khi, 0.001);
        assertEquals(0.8, optimizer.gamma, 0.001);
    }

    /**
     * Tests the optimization with a simple function.
     */
    @Test
    public void testOptimizationSimple() throws Exception {
        optimizer.optimize(FUNCTION, BOUNDS, MaxEval.UNLIMITED, null, convergenceChecker);
        PointValuePair optimalSolution = optimizer.getOptimum();
        assertArrayEquals(new double[] {1.5708}, optimalSolution.getPoint(), 0.001);
        assertEquals(-0.5789, optimalSolution.getValue(), 0.001);
    }

    /**
     * Tests the optimization with a more complex function.
     */
    @Test
    public void testOptimizationComplex() throws Exception {
        optimizer.optimize(FUNCTION, BOUNDS, MaxEval.UNLIMITED, null, convergenceChecker);
        PointValuePair optimalSolution = optimizer.getOptimum();
        assertArrayEquals(new double[] {1.1772}, optimalSolution.getPoint(), 0.001);
        assertEquals(-1.1827, optimalSolution.getValue(), 0.001);
    }

    /**
     * Tests optimization with out of bounds values.
     */
    @Test
    public void testOutOfBounds() {
        optimizer = new MultiDirectional();
        try {
            optimizer.optimize(FUNCTION, new SimpleBounds(-20.0, -10.0), MaxEval.UNLIMITED, null, convergenceChecker);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertEquals("Bounds must not be empty and must not contain invalid points.", e.getMessage());
        }
    }

    /**
     * Tests optimization with a null function.
     */
    @Test
    public void testNullFunction() {
        optimizer = new MultiDirectional();
        try {
            optimizer.optimize(null, BOUNDS, MaxEval.UNLIMITED, null, convergenceChecker);
            assertFalse(true);
        } catch (NullArgumentException e) {
            assertEquals("Function cannot be null.", e.getMessage());
        }
    }

    /**
     * Tests optimization with a null convergence checker.
     */
    @Test
    public void testNullConvergenceChecker() {
        optimizer = new MultiDirectional();
        try {
            optimizer.optimize(FUNCTION, BOUNDS, MaxEval.UNLIMITED, null, null);
            assertFalse(true);
        } catch (NullPointerException e) {
            assertEquals("Convergence checker cannot be null.", e.getMessage());
        }
    }

    /**
     * Tests optimization with a specified maximum limit of function evaluations.
     */
    @Test
    public void testMaxEvalLimit() throws Exception {
        optimizer = new MultiDirectional();
        try {
            optimizer.optimize(FUNCTION, BOUNDS, 10, null, convergenceChecker);
            assertFalse(true);
        } catch (MaxEvalExceededException e) {
            // expected
        }
    }

    /**
     * Tests optimization with a non-unary function.
     */
    @Test
    public void testNonUnaryFunction() {
        optimizer = new MultiDirectional();
        try {
            optimizer.optimize(new MyFunction(), BOUNDS, MaxEval.UNLIMITED, null, convergenceChecker);
            assertFalse(true);
        } catch (ClassCastException e) {
            assertEquals("Function must be a NonUnaryFunction.", e.getMessage());
        }
    }

    /**
     * Tests optimization with a singular Hessian matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularHessian() throws Exception {
        optimizer.optimize(new SingularFunction(), BOUNDS, MaxEval.UNLIMITED, null, convergenceChecker);
    }

    /**
     * Tests optimization with a function without bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws Exception {
        optimizer.optimize(new NoBracketingFunction(), BOUNDS, MaxEval.UNLIMITED, null, convergenceChecker);
    }

}

class MyFunction implements NonUnaryFunction {
    @Override
    public double value(double x) {
        return Math.sin(Math.exp(-x / 2.0)) / 2.0;
    }

    @Override
    public RealNVector gradient(double x) {
        return new org.apache.commons.math3.optim.points.ContinuousPointFunction(x) {
            @Override
            public double value() {
                return MultiDirectionalTest.MY_FUNCTION.value(x);
            }

            @Override
            public double[] gradient() {
                double ex = Math.exp(-x / 2.0);
                return new double[] {Math.sin(ex) / (2.0 * ex), -Math.cos(ex) / (2.0 * ex)};
            }
        }.value();
    }
}

class SingularFunction implements NonUnaryFunction {
    @Override
    public double value(double x) {
        return Math.pow(Math.sin(x), 2.0) / Math.pow(Math.cos(x), 2.0);
    }

    @Override
    public RealNVector gradient(double x) {
        return new org.apache.commons.math3.optim.points.ContinuousPointFunction(x) {
            @Override
            public double value() {
                return MultiDirectionalTest.SINGULAR_FUNCTION.value(x);
            }

            @Override
            public double[] gradient() {
                double sinX = Math.sin(x);
                double cosX = Math.cos(x);
                double value = MultiDirectionalTest.SINGULAR_FUNCTION.value(x);
                if (Double.isNaN(value)) {
                    throw new SingularMatrixException("Hessian matrix is singular at point " + x);
                }
                return new double[] {(2.0 * Math.cos(x) * sinX - 2.0 * Math.sin(x) * cosX) / Math.pow(cosX, 3.0),
                        (2.0 * Math.sin(x) * sinX + 2.0 * Math.cos(x) * cosX) / Math.pow(cosX, 3.0)};
            }
        }.value();
    }
}

class NoBracketingFunction implements NonUnaryFunction {
    @Override
    public double value(double x) {
        if (x < 0) {
            return x * x;
        } else {
            return -x * x;
        }
    }

    @Override
    public RealNVector gradient(double x) {
        return new org.apache.commons.math3.optim.points.ContinuousPointFunction(x) {
            @Override
            public double value() {
                return MultiDirectionalTest.NO_BRACKETING_FUNCTION.value(x);
            }

            @Override
            public double[] gradient() {
                return new double[] {2.0 * x, 2.0 * x};
            }
        }.value();
    }
}