package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testSimpleMinimization() throws MaxEvalExceededException, NotStrictlyPositiveException {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 0.1);
        Assert.assertEquals(0.0, result.getPoint(), 1e-6);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceededException() throws NotStrictlyPositiveException {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(1.0 / (1.0 - x));
            }
        };
        optimizer.setMaxEvaluations(5);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() throws MaxEvalExceededException {
        optimizer.setMinIterations(0);
        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, -1, 1, 0.5);
    }

    @Test
    public void testMaximization() throws MaxEvalExceededException, NotStrictlyPositiveException {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.sin(x);
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10, 0.1);
        Assert.assertEquals(1.1, result.getPoint(), 1e-6);
    }
}