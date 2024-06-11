package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.junit.Test;
import org.junit.Assert;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        double result = optimizer.optimize(100, (x) -> x * x, GoalType.MINIMIZE, 0, 10).getPoint();
        Assert.assertEquals(0.0, result, 1e-6);

        result = optimizer.optimize(100, (x) -> Math.sin(x), GoalType.MAXIMIZE, 0, Math.PI).getPoint();
        Assert.assertEquals(Math.PI / 2, result, 1e-6);
    }

    @Test
    public void testBrentOptimizerWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        double result = optimizer.optimize(100, (x) -> x * x, GoalType.MINIMIZE, 0, 10, 5).getPoint();
        Assert.assertEquals(0.0, result, 1e-6);

        result = optimizer.optimize(100, (x) -> Math.sin(x), GoalType.MAXIMIZE, 0, Math.PI, Math.PI / 2).getPoint();
        Assert.assertEquals(Math.PI / 2, result, 1e-6);
    }

    @Test
    public void testBrentOptimizerWithInvalidParameters() {
        try {
            BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (Exception e) {
            // Expected exception
        }

        try {
            BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
            double result = optimizer.optimize(100, (x) -> x * x, GoalType.MINIMIZE, 10, 0).getPoint();
            Assert.fail("Expected IllegalArgumentException");
        } catch (Exception e) {
            // Expected exception
        }
    }

}