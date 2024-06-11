package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optimization.UnivariatePointValues.MINIMUM;
import static org.apache.commons.math3.optimization.UnivariatePointValues.MAXIMUM;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-3);

        FunctionUnarySolver<Double, Double> solver = optimizer.optimize(new Function1<Double, Double>() {
            @Override
            public Double value(Double x) {
                return Math.pow(Math.sin(x), 2);
            }
        }, -5.0, 5.0);

        UnivariatePointValuePair result = solver.getSolution();
        Assert.assertEquals(MINIMUM, result.getPointValueType());
        Assert.assertEquals(Math.PI / 2, result.getXValue(), 1e-5);
        Assert.assertEquals(-1, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-3);

        FunctionUnarySolver<Double, Double> solver = optimizer.optimize(new Function1<Double, Double>() {
            @Override
            public Double value(Double x) {
                return Math.pow(-Math.sin(x), 2);
            }
        }, -5.0, 5.0);

        UnivariatePointValuePair result = solver.getSolution();
        Assert.assertEquals(MAXIMUM, result.getPointValueType());
        Assert.assertEquals(-Math.PI / 2, result.getXValue(), 1e-5);
        Assert.assertEquals(1, result.getValue(), 1e-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNegativeRelativeThreshold() {
        new BrentOptimizer(-1e-5, 1e-3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(1e-5, -1e-3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerZeroRelativeThreshold() {
        new BrentOptimizer(0, 1e-3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerZeroAbsoluteThreshold() {
        new BrentOptimizer(1e-5, 0);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-3, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() <= previous.getValue();
            }
        });

        FunctionUnarySolver<Double, Double> solver = optimizer.optimize(new Function1<Double, Double>() {
            @Override
            public Double value(Double x) {
                return Math.pow(Math.sin(x), 2);
            }
        }, -5.0, 5.0);

        UnivariatePointValuePair result = solver.getSolution();
        Assert.assertEquals(MINIMUM, result.getPointValueType());
        Assert.assertEquals(Math.PI / 2, result.getXValue(), 1e-5);
        Assert.assertEquals(-1, result.getValue(), 1e-5);
    }
}