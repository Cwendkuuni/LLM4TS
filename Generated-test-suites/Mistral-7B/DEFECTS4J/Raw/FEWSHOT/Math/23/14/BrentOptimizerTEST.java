package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithDefaultArguments() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        double function = Math.exp(Math.log(2) / 3);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(Math.E, Math.log(2) / (3 * x)));
        assertEquals(function, result.getValue(), 1e-6);
        assertEquals(result.getPoint(), Math.log(2) / 3, 1e-6);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, a, b) -> Math.abs(a.getValue() - b.getValue()) < 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, checker);
        double function = Math.exp(Math.log(2) / 3);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(Math.E, Math.log(2) / (3 * x)));
        assertEquals(function, result.getValue(), 1e-6);
        assertEquals(result.getPoint(), Math.log(2) / 3, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsoluteThreshold() {
        new BrentOptimizer(1e-5, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeThreshold() {
        new BrentOptimizer(0, 1e-5);
    }

    @Test
    public void testBrentOptimizerWithMinimizeGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        double function = Math.pow(Math.E, Math.log(2) / 3);
        UnivariatePointValuePair result = optimizer.optimize(x -> -Math.pow(Math.E, Math.log(2) / (3 * x)));
        assertEquals(-function, result.getValue(), 1e-6);
        assertEquals(result.getPoint(), Math.log(2) / 3, 1e-6);
    }

    @Test
    public void testBrentOptimizerWithMaximizeGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        double function = Math.pow(Math.E, Math.log(2) / 3);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(Math.E, Math.log(2) / (3 * x)));
        assertEquals(function, result.getValue(), 1e-6);
        assertEquals(result.getPoint(), Math.log(2) / 3, 1e-6);
    }

    @Test
    public void testBrentOptimizerWithBoundariesOutOfOrder() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        double function = Math.pow(Math.E, Math.log(2) / 3);
        optimizer.setMin(1.0);
        optimizer.setMax(0.0);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(Math.E, Math.log(2) / (3 * x)));
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsExceptionAtMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        Exception thrownFunction = assertThrows(RuntimeException.class, () -> {
            optimizer.optimize(x -> {
                throw new RuntimeException();
            });
        });
        assertTrue(thrownFunction.getMessage().contains("Function value is not computed"));
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsExceptionAtMinimumWithCustomMessage() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        Exception thrownFunction = assertThrows(RuntimeException.class, () -> {
            optimizer.optimize(x -> {
                throw new RuntimeException("Custom message");
            });
        });
        assertEquals("Custom message", thrownFunction.getMessage());
    }

}