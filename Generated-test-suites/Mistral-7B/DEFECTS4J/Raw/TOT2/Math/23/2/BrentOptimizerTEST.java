package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = new MyUnivariateFunction();
        optimizer = new BrentOptimizer(0.001, 0.01);
    }

    @Test
    public void testOptimizeBasic() {
        PointValuePair result = optimizer.optimize(function, -10.0, 10.0);
        double expected = 0.75; // The minimum value of the test function.
        assertEquals(expected, result.getPoint(), 0.001);
        assertEquals(expected, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeMinimumOutsideRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            optimizer.optimize(function, -100.0, -5.0);
        });
    }

    @Test
    public void testOptimizeMaximumOutsideRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            optimizer.optimize(function, 50.0, 100.0);
        });
    }

    @Test
    public void testOptimizeFunctionThrowsException() {
        UnivariateFunction functionWithException = new MyUnivariateFunctionWithException();
        assertThrows(ArithmeticException.class, () -> {
            optimizer.optimize(functionWithException, -10.0, 10.0);
        });
    }

    @Test
    public void testOptimizeInvalidGoalType() {
        assertThrows(IllegalArgumentException.class, () -> {
            optimizer = new BrentOptimizer(0.001, 0.01, null);
        });
    }

    @Test
    public void testOptimizeConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = (iter, current, previous) -> {
            if (iter > 10) {
                return true;
            }
            return false;
        };

        optimizer = new BrentOptimizer(0.001, 0.01, customChecker);
        PointValuePair result = optimizer.optimize(function, -10.0, 10.0);
        assertEquals(11, optimizer.getIterations());
    }

    @Test
    public void testOptimizeGoalTypeIsMaximize() {
        optimizer = new BrentOptimizer(0.001, 0.01, new SimpleGoalType(SimpleGoalType.MAXIMIZE));
        PointValuePair result = optimizer.optimize(function, -10.0, 10.0);
        double expected = -0.75; // The maximum value of the test function.
        assertEquals(expected, result.getValue(), 0.001);
        assertEquals(-expected, result.getPoint(), 0.001);
    }
}

class MyUnivariateFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return 2 * Math.pow(Math.E, -Math.abs(x)) - Math.pow(Math.abs(x), 3) / 3;
    }
}

class MyUnivariateFunctionWithException implements UnivariateFunction {
    @Override
    public double value(double x) throws ArithmeticException {
        if (x < 0) {
            throw new ArithmeticException();
        }
        return Math.pow(Math.abs(x), 3) / 3;
    }
}