package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class BrentOptimizerTest {

    /**
     * Test optimization of a simple function.
     */
    @Test
    public void testBrentOptimizerSimpleFunction() {
        ObjectiveFunction f = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        PointValuePair result = optimizer.optimize(f, 0.0, 1.0);
        assertEquals(0.0, result.getPoint(), 1e-5);
    }

    /**
     * Test optimization of a function with two local minima.
     */
    @Test
    public void testBrentOptimizerTwoLocalMinima() {
        ObjectiveFunction f = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(Math.sin(Math.PI * x), 2) - 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        PointValuePair result = optimizer.optimize(f, -1.5, 1.5);
        assertEquals(-1, result.getPoint(), 1e-5);
    }

    /**
     * Test optimization of a function with a global minimum at infinity.
     */
    @Test
    public void testBrentOptimizerInfinityMinimum() {
        ObjectiveFunction f = new ObjectiveFunction() {
            public double value(double x) {
                return 1 / x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        PointValuePair result = optimizer.optimize(f, -10, 10);
        assertNull(result);
    }

    /**
     * Test optimization of a function with a minimum at a NaN value.
     */
    @Test
    public void testBrentOptimizerNaNMinimum() {
        ObjectiveFunction f = new ObjectiveFunction() {
            public double value(double x) {
                if (x > 1) {
                    return x * Math.log(x);
                } else {
                    return Double.NaN;
                }
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        PointValuePair result = optimizer.optimize(f, 0.0, 2.0);
        assertNull(result);
    }

    /**
     * Test optimization of a function with a minimum at infinity and a
     * local minimum at a finite value.
     */
    @Test
    public void testBrentOptimizerInfinityAndFiniteMinima() {
        ObjectiveFunction f = new ObjectiveFunction() {
            public double value(double x) {
                if (x > 0) {
                    return Math.pow(x, 2) - 2 * Math.log(x);
                } else {
                    return Double.POSITIVE_INFINITY;
                }
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        PointValuePair result = optimizer.optimize(f, -10, 10);
        assertNull(result);

        ValueInitializer initializer = new ValueInitializer(1.0);
        optimizer = new BrentOptimizer(1e-5, 1e-5, optimizer);
        result = optimizer.optimize(f, initializer);
        assertEquals(1.0, result.getPoint(), 1e-5);
    }

    /**
     * Test optimization of a function with a minimum at negative infinity.
     */
    @Test
    public void testBrentOptimizerNegativeInfinityMinimum() {
        ObjectiveFunction f = new ObjectiveFunction() {
            public double value(double x) {
                return Math.log(Math.abs(x)) + Math.PI * Math.sin(Math.PI * x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        PointValuePair result = optimizer.optimize(f, -10, 10);
        assertNull(result);

        ValueInitializer initializer = new ValueInitializer(-1.0);
        optimizer = new BrentOptimizer(1e-5, 1e-5, optimizer);
        result = optimizer.optimize(f, initializer);
        assertEquals(-1.0, result.getPoint(), 1e-5);
    }

    /**
     * Test optimization of a function with a goal type of MAXIMUM.
     */
    @Test
    public void testBrentOptimizerMaximum() {
        ObjectiveFunction f = new ObjectiveFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(f, 0.0, 1.0);
        assertEquals(1.0, result.getPoint(), 1e-5);
    }
}