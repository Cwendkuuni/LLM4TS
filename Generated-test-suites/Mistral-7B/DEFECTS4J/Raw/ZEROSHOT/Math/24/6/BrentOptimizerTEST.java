package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateFunction f = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        UnivariateOptimizer.OptimizationResult result = optimizer.optimize(f, -10, 10);
        PointValuePair optimum = result.getPoint();
        double expected = 0.66666668653488;
        assertEquals("Expected optimal value is not correct.", expected, optimum.getValue(), 1e-8);
        assertEquals("Expected optimal point is not correct.", 0.66666668653488, optimum.getPoint(), 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeThreshold() {
        new BrentOptimizer(0, 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidAbsoluteThreshold() {
        new BrentOptimizer(1e-8, 0);
    }

    @Test
    public void testBrentOptimizerWithAdditionalConvergenceChecker() {
        UnivariateFunction f = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, new MyConvergenceChecker());
        UnivariateOptimizer.OptimizationResult result = optimizer.optimize(f, -10, 10);
        PointValuePair optimum = result.getPoint();
        double expected = 0.66666668653488;
        assertEquals("Expected optimal value is not correct.", expected, optimum.getValue(), 1e-8);
        assertEquals("Expected optimal point is not correct.", 0.66666668653488, optimum.getPoint(), 1e-8);
    }

    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2 * x + 1;
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
            double delta = Math.abs(current.getValue() - previous.getValue());
            return delta < 1e-8;
        }
    }
}