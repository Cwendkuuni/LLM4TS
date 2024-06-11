package org.apache.commons.cli;

import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.functions.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test constructor with absolute and relative thresholds and checker
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);
        assertEquals("Expected constructor with absolute and relative thresholds and checker", 1e-5, optimizer.getAbsoluteThreshold(), 1e-8);
        assertEquals("Expected constructor with absolute and relative thresholds and checker", 1e-6, optimizer.getRelativeThreshold(), 1e-8);

        // Test constructor with only relative and absolute thresholds
        BrentOptimizer optimizer2 = new BrentOptimizer(1e-5, 1e-6);
        assertEquals("Expected constructor with only relative and absolute thresholds", 1e-5, optimizer2.getAbsoluteThreshold(), 1e-8);
        assertEquals("Expected constructor with only relative and absolute thresholds", 1e-6, optimizer2.getRelativeThreshold(), 1e-8);

        // Test if constructor throws NotStrictlyPositiveException if absolute threshold is not positive
        try {
            new BrentOptimizer(-1e-6, 1e-5);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        // Test if constructor throws NumberIsTooSmallException if relative threshold is less than 2 * Math.ulp(1d)
        try {
            new BrentOptimizer(1e-7, 1e-6);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }

        // Test optimization with a simple function
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        BrentOptimizer optimizer3 = new BrentOptimizer(1e-5, 1e-6);
        UnivariatePointValuePair result = optimizer3.optimize(testFunction, -3, 3);
        assertEquals("Expected result x = sqrt(3) = 1.73205080756888", 1.7320508075688772935, result.getPoint(), 1e-8);
        assertEquals("Expected result f(x) = x^2 = 3", 3, result.getValue(), 1e-8);

        // Test optimization with a non-differentiable function
        UnivariateFunction testFunction2 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    return x * x;
                } else {
                    return Math.sin(Math.PI / x);
                }
            }

            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException("Function is not differentiable at x = 0");
            }
        };
        BrentOptimizer optimizer4 = new BrentOptimizer(1e-5, 1e-6);
        UnivariatePointValuePair result2 = optimizess(optimizer4, testFunction2, -5, 5);
        assertEquals("Expected result x = 1.2732338305466", 1.273233830546599, result2.getPoint(), 1e-8);
        assertEquals("Expected result f(x) = 0.2874778166156", 0.2874778166156018, result2.getValue(), 1e-8);

        // Test optimization with a complex function
        UnivariateFunction testFunction3 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) * Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return -Math.exp(-x) * Math.sin(x) + Math.exp(-x) * Math.cos(x);
            }
        };
        BrentOptimizer optimizer5 = new BrentOptimizer(1e-5, 1e-6);
        UnivariatePointValuePair result3 = optimizer5.optimize(testFunction3, -5, 5);
        assertEquals("Expected result x = -1.1493354163328", -1.149335416332813, result3.getPoint(), 1e-8);
        assertEquals("Expected result f(x) = -1.8212453758734", -1.8212453758734273, result3.getValue(), 1e-8);
    }

    private UnivariatePointValuePair optimizess(BrentOptimizer optimizer, UnivariateFunction function, double lowerLimit, double upperLimit) {
        return optimizer.optimize(function, lowerLimit, upperLimit);
    }

    @Test
    public void testGoalType() {
        assertEquals("Expected GoalType MINIMIZE", GoalType.MINIMIZE, optimizer.getGoalType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetGoalType() {
        optimizer.setGoalType(GoalType.MAXIMIZE);
    }
}