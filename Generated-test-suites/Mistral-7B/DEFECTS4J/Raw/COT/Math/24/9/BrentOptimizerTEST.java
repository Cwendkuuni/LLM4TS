package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.points.UnivariatePointValuePair;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private double epsilon = 1e-10;
    private UnivariateFunction function;
    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.exp(-Math.pow(x, 2));
            }

            @Override
            public double derivative(double x) {
                // Unsupported operation for this test
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void testBrentOptimizerConstructor() {
        optimizer = new BrentOptimizer(0.01, 0.001);
        assertNotNull(optimizer);

        optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertNotNull(optimizer);

        optimizer = new BrentOptimizer(0.01, 0.0);
        assertFalse(optimizer instanceof BrentOptimizer);

        optimizer = new BrentOptimizer(0.0, 0.01);
        assertFalse(optimizer instanceof BrentOptimizer);

        try {
            optimizer = new BrentOptimizer(-0.01, 0.01);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizer = new BrentOptimizer(0.01, -0.01);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizer = new BrentOptimizer(0.01, 0.01, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerMinimization() {
        optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, Double.POSITIVE_INFINITY, GoalType.MINIMIZE);

        assertEquals(Math.PI / 2, result.getPoint(), epsilon);
        assertEquals(E.EULER_NUMBER.doubleValue() * E.EULER_NUMBER.doubleValue() * E.E.doubleValue() / 2,
                result.getValue(), epsilon);

        optimizer = new BrentOptimizer(0.01, 0.001, new ValueInitializer() {
            @Override
            public double initialize() {
                return 1.0;
            }
        });

        result = optimizer.optimize(function, 0.0, Double.POSITIVE_INFINITY, GoalType.MINIMIZE);
        assertEquals(Math.PI / 2, result.getPoint(), epsilon);
        assertEquals(E.EULER_NUMBER.doubleValue() * E.EULER_NUMBER.doubleValue() * E.E.doubleValue() / 2,
                result.getValue(), epsilon);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair result = optimizer.optimize(new NegativeFunction(function), 0.0, Double.POSITIVE_INFINITY, GoalType.MAXIMIZE);

        assertEquals(-Math.PI / 2, result.getPoint(), epsilon);
        assertEquals(E.EULER_NUMBER.doubleValue() * E.EULER_NUMBER.doubleValue() * E.E.doubleValue() / 2,
                -result.getValue(), epsilon);

        optimizer = new BrentOptimizer(0.01, 0.001, new ValueInitializer() {
            @Override
            public double initialize() {
                return 1.0;
            }
        });

        result = optimizer.optimize(new NegativeFunction(function), 0.0, Double.POSITIVE_INFINITY, GoalType.MAXIMIZE);
        assertEquals(-Math.PI / 2, result.getPoint(), epsilon);
        assertEquals(E.EULER_NUMBER.doubleValue() * E.EULER_NUMBER.doubleValue() * E.E.doubleValue() / 2,
                -result.getValue(), epsilon);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidGoalType() {
        optimizer = new BrentOptimizer(0.01, 0.001);
        optimizer.optimize(function, 0.0, Double.POSITIVE_INFINITY, GoalType.MAX_MIN);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = (iter, previous, current) -> {
            if ((previous != null && current != null && previous.getValue() >= current.getValue())) {
                return true;
            }
            return false;
        };

        optimizer = new BrentOptimizer(0.01, 0.001, convergenceChecker);

        try {
            UnivariatePointValuePair result = optimizer.optimize(function, 0.0, Double.POSITIVE_INFINITY, GoalType.MINIMIZE);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        optimizer = new BrentOptimizer(0.01, 0.001, convergenceChecker);
        result = optimizer.optimize(function, 0.0, Double.POSITIVE_INFINITY, GoalType.MAXIMIZE);
        assertEquals(-Math.PI / 2, result.getPoint(), epsilon);
        assertEquals(E.EULER_NUMBER.doubleValue() * E.EULER_NUMBER.doubleValue() * E.E.doubleValue() / 2,
                -result.getValue(), epsilon);
    }

    private static class NegativeFunction implements UnivariateFunction {
        private final UnivariateFunction underlyingFunction;

        NegativeFunction(UnivariateFunction underlyingFunction) {
            this.underlyingFunction = underlyingFunction;
        }

        @Override
        public double value(double x) {
            return -underlyingFunction.value(x);
        }

        @Override
        public double derivative(double x) {
            return -underlyingFunction.derivative(x);
        }
    }
}