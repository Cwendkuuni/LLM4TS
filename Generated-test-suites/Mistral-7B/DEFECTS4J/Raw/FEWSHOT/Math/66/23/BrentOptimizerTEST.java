package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNull(optimizer.getResult());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test
    public void testBrentOptimizerOptimizeStringFunction() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.sin(x)) + 0.1 * Math.abs(x);
            }
        };

        double min = 0.0;
        double max = 2.0 * Math.PI;
        double startValue = 1.5;

        double result = optimizer.optimize(func, GoalType.MINIMIZE, min, max, startValue);

        assertEquals(Math.PI, result, 1e-5);
    }

    @Test
    public void testBrentOptimizerOptimizePositiveFunction() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        double min = 0.0;
        double max = 4.0;
        double startValue = 2.0;

        double result = optimizer.optimize(func, GoalType.MINIMIZE, min, max, startValue);

        assertEquals(0.0, result, 1e-5);
    }

    @Test
    public void testBrentOptimizerOptimizeNegativeFunction() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x, 2);
            }
        };

        double min = -4.0;
        double max = 0.0;
        double startValue = -2.0;

        double result = optimizer.optimize(func, GoalType.MAXIMIZE, min, max, startValue);

        assertEquals(0.0, result, 1e-5);
    }

    @Test
    public void testBrentOptimizerOutOfBounds() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        double min = -Math.PI;
        double max = Math.PI;
        double startValue = 2.0 * Math.PI;

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(func, GoalType.MINIMIZE, min, max, startValue));
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionFails() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x >= 0) {
                    return Math.pow(x, 2);
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        };

        double min = 0.0;
        double max = 2.0;
        double startValue = 1.0;

        assertThrows(UnsupportedOperationException.class, () -> optimizer.optimize(func, GoalType.MINIMIZE, min, max, startValue));
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionFails() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x >= 0) {
                    return Math.pow(x, 2);
                } else {
                    return Math.pow(-x, 2);
                }
            };

            @Override
            public double value(Double x) {
                if (x == null) {
                    throw new UnsupportedOperationException();
                }
                return value(x.doubleValue());
            }
        };

        double min = -2.0;
        double max = 0.0;
        double startValue = -1.0;

        assertThrows(UnsupportedOperationException.class, () -> optimizer.optimize(func, GoalType.MAXIMIZE, min, max, startValue));
    }

    @Test
    public void testBrentOptimizerMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };

        double min = 0.0;
        double max = 2.0 * Math.PI;
        double startValue = 1.5 * Math.PI;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);

        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(func, GoalType.MINIMIZE, min, max, startValue));
    }

    @Test
    public void testBrentOptimizerNotStrictlyPositiveAccuracy() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };

        double min = 0.0;
        double max = 2.0 * Math.PI;
        double startValue = 1.5 * Math.PI;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-0.1);

        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.optimize(func, GoalType.MINIMIZE, min, max, startValue));
    }

    @Test
    public void testBrentOptimizerNotStrictlyPositiveRelativeAccuracy() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x) * Math.exp(-x);
            }
        };

        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(-0.1);

        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.optimize(func, GoalType.MINIMIZE, min, max, startValue));
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithDerivative() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.pow(Math.sin(x), 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x + 2 * Math.cos(x);
            }
        };

        double min = 1.5;
        double max = 3.0;
        double startValue = 2.0;

        double result = optimizer.optimize(func, GoalType.MINIMIZE, min, max, startValue);

        assertEquals(1.5707963267948966, result, 1e-5);
    }

    @Test
    public void testBrentOptimizerMaximizeFunctionWithDerivative() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - Math.pow(Math.sin(x), 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x - 2 * Math.cos(x);
            }
        };

        double min = -3.0;
        double max = 0.0;
        double startValue = -2.0;

        double result = optimizer.optimize(func, GoalType.MAXIMIZE, min, max, startValue);

        assertEquals(1.5707963267948966, result, 1e-5);
    }

    @Test
    public void testBrentOptimizerMaxIterationsExceededWithDerivative() throws Exception {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x) * (-Math.cos(x) + Math.sin(x));
            }
        };

        double min = 0.0;
        double max = 2.0 * Math.PI;
        double startValue = 1.5 * Math.PI;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);

        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(func, GoalType.MINIMIZE, min, max, startValue));
    }
}