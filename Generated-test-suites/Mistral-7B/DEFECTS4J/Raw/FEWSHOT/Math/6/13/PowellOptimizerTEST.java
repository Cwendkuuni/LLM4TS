package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionAdapter;
import org.apache.commons.math3.optim.nonlinear.SimpleBoundingMethod;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePairChecker;
import org.apache.commons.math3.optim.InitialGuess.RandomInitialStrategies;

public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FastMath.pow(x[0], 2) + FastMath.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01);
        PointValuePair result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{0, 0}, 0), result);

        optimizer = new PowellOptimizer(0.01, 0.01, new SimpleBoundingMethod(0, 10, 0, 10));
        result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{1, 1}, 2), result);

        optimizer = new PowellOptimizer(0.01, 0.01, new BrentOptimizer(0.001, 0.001, new SimpleUnivariateValueChecker(0.01, 0.01)));
        result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{1.2, 1.2}, 2.25), result, 0.001);

        optimizer = new PowellOptimizer(0.01, 0.01, null, null, new PointValuePairChecker(new PointValuePair(new double[]{1.2, 1.2}, 2.25)));
        result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{1.2, 1.2}, 2.25), result);

        optimizer = new PowellOptimizer(0.01, 0.01, 0.001, 0.001);
        result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{1.2, 1.2}, 2.25), result, 0.001);

        optimizer = new PowellOptimizer(0.01, 0.01, 0.0001, 0.0001);
        result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{1.2, 1.2}, 2.25), result, 0.0001);

        optimizer = new PowellOptimizer(0.01, 0.01, 0.00001, 0.00001);
        result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{1.2, 1.2}, 2.25), result, 0.00001);

        optimizer = new PowellOptimizer(0.01, 0.01, 0.000001, 0.000001);
        result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{1.2, 1.2}, 2.25), result, 0.000001);

        optimizer = new PowellOptimizer(0.01, 0.01, 0.0000001, 0.0000001);
        result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{1.2, 1.2}, 2.25), result, 0.0000001);

        optimizer = new PowellOptimizer(0.01, 0.01, 0.00000001, 0.00000001);
        result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{1.2, 1.2}, 2.25), result, 0.00000001);

        optimizer = new PowellOptimizer(0.01, 0.01, 0.000000001, 0.000000001);
        result = optimizer.optimize(function);
        assertEquals(new PointValuePair(new double[]{1.2, 1.2}, 2.25), result, 0.000000001);

        optimizer = new PowellOptimizer(0.01, 0.01, 0.0, 0.0);
        try {
            result = optimizer.optimize(function);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        optimizer = new PowellOptimizer(0, 0);
        try {
            result = optimizer.optimize(function);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        optimizer = new PowellOptimizer(0.000000001, 0);
        try {
            result = optimizer.optimize(function);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        optimizer = new PowellOptimizer(-1, 0);
        try {
            result = optimizer.optimize(function);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        optimizer = new PowellOptimizer(0.01, -1);
        try {
            result = optimizer.optimize(function);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        optimizer = new PowellOptimizer(0.01, 0.01, new SimpleBoundingMethod(-1, 10, -1, 10));
        try {
            result = optimizer.optimize(function);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        optimizer = new PowellOptimizer(0.01, 0.01, new BrentOptimizer(-1, 0, new SimpleUnivariateValueChecker(0.01, 0.01)));
        try {
            result = optimizer.optimize(function);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        optimizer = new PowellOptimizer(0.01, 0.01, 0.01, 0.01, new PointValuePairChecker(new PointValuePair(new double[]{-1, -1}, 2.25)));
        try {
            result = optimizer.optimize(function);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLineSearch() {
        UnivariateFunction function = new UnivariateFunctionAdapter(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FastMath.pow(x[0], 2) + FastMath.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        });

        double[] p = new double[]{0, 0};
        double[] d = new double[]{1, 1};

        LineSearch lineSearch = new LineSearch(0.01, 0.01);
        UnivariatePointValuePair result = lineSearch.search(p, d);
        assertEquals(new PointValuePair(p[0] + result.getPoint() * d[0], result.getValue()), new PointValuePair(p[0] + result.getPoint(), result.getValue()), 0.01);

        lineSearch = new LineSearch(0.001, 0.001);
        result = lineSearch.search(p, d);
        assertEquals(new PointValuePair(p[0] + result.getPoint() * d[0], result.getValue()), new PointValuePair(p[0] + result.getPoint(), result.getValue()), 0.001);

        lineSearch = new LineSearch(0.0001, 0.0001);
        result = lineSearch.search(p, d);
        assertEquals(new PointValuePair(p[0] + result.getPoint() * d[0], result.getValue()), new PointValuePair(p[0] + result.getPoint(), result.getValue()), 0.0001);

        lineSearch = new LineSearch(0.00001, 0.00001);
        result = lineSearch.search(p, d);
        assertEquals(new PointValuePair(p[0] + result.getPoint() * d[0], result.getValue()), new PointValuePair(p[0] + result.getPoint(), result.getValue()), 0.00001);

        lineSearch = new LineSearch(0.000001, 0.000001);
        result = lineSearch.search(p, d);
        assertEquals(new PointValuePair(p[0] + result.getPoint() * d[0], result.getValue()), new PointValuePair(p[0] + result.getPoint(), result.getValue()), 0.000001);

        lineSearch = new LineSearch(0.0000001, 0.0000001);
        result = lineSearch.search(p, d);
        assertEquals(new PointValuePair(p[0] + result.getPoint() * d[0], result.getValue()), new PointValuePair(p[0] + result.getPoint(), result.getValue()), 0.0000001);

        lineSearch = new LineSearch(0.00000001, 0.00000001);
        result = lineSearch.search(p, d);
        assertEquals(new PointValuePair(p[0] + result.getPoint() * d[0], result.getValue()), new PointValuePair(p[0] + result.getPoint(), result.getValue()), 0.00000001);

        lineSearch = new LineSearch(0.000000001, 0.000000001);
        result = lineSearch.search(p, d);
        assertEquals(new PointValuePair(p[0] + result.getPoint() * d[0], result.getValue()), new PointValuePair(p[0] + result.getPoint(), result.getValue()), 0.000000001);

        lineSearch = new LineSearch(0.0000000001, 0.0000000001);
        result = lineSearch.search(p, d);
        assertEquals(new PointValuePair(p[0] + result.getPoint() * d[0], result.getValue()), new PointValuePair(p[0] + result.getPoint(), result.getValue()), 0.0000000001);
    }
}