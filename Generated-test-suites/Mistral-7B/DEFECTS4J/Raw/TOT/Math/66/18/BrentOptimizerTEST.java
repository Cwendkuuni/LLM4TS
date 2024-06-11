package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testMinimization() throws NotStrictlyPositiveException, MaxEvalException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - Math.pow(x, 3);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        Assert.assertEquals(0.0, result.getPoint(), 1e-8);
    }

    @Test
    public void testMaximization() throws NotStrictlyPositiveException, MaxEvalException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(x, 3);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        Assert.assertEquals(3.0, result.getPoint(), 1e-8);
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() throws NotStrictlyPositiveException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new MaxEvalException("Maximum number of evaluations reached.");
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveBounds() {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        }, GoalType.MINIMIZE, -5.0, -1.0, 0.0);
    }

    @Test
    public void testGoalTypeMaximization() throws NotStrictlyPositiveException, MaxEvalException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(x, 3);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        Assert.assertEquals(3.0, result.getPoint(), 1e-8);
    }

    @Test
    public void testBounds() throws NotStrictlyPositiveException, MaxEvalException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - Math.pow(x, 3);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 1.0, 5.0, 0.0);
        Assert.assertEquals(1.011755649346585, result.getPoint(), 1e-5);
    }
}