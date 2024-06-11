package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalinationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testMinimization() throws NotStrictlyPositiveException, MaxEvalinationsExceededException, PrecisionException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double min = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 6.0).getPoint();
        Assert.assertEquals(2.0, min, 0.001);
    }

    @Test
    public void testMaximization() throws NotStrictlyPositiveException, MaxEvalinationsExceededException, PrecisionException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double max = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 6.0).getPoint();
        Assert.assertEquals(4.0, max, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeRelativeAccuracy() throws NotStrictlyPositiveException {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(-0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsolutAccuracy() throws NotStrictlyPositiveException {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-0.01);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxEvaluationsExceededException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 2 * Math.PI);
    }
}