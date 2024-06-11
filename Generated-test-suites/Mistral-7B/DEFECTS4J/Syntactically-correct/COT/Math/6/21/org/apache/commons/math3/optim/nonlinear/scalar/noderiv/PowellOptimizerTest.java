package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.simple.SimpleValuesProvider;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    private PowellOptimizer optimizer;
    private double[] startPoint;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(1e-5, 1e-5);
        startPoint = new double[]{1.0, 2.0};
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 2 * Math.pow(x, 3) - 4 * x;
            }
        };
    }

    @Test
    public void testOptimize() {
        PointValuePair result = optimizer.optimize(startPoint, function);
        double expectedX = -1.0;
        double expectedFval = 4.0;
        Assert.assertArrayEquals(new double[]{expectedX, expectedX}, result.getPoint(), 0.01);
        Assert.assertEquals(expectedFval, result.getValue(), 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructor1() {
        new PowellOptimizer(0.0, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidConstructor2() {
        new PowellOptimizer(0.00001, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        optimizer.optimize(startPoint, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStartPoint() {
        optimizer.optimize(null, function);
    }

    @Test
    public void testUserDefinedConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return previous.getValue() > current.getValue();
            }
        };
        PowellOptimizer customOptimizer = new PowellOptimizer(1e-5, 1e-5, customChecker);
        PointValuePair result = customOptimizer.optimize(startPoint, function);
        Assert.assertArrayEquals(startPoint, result.getPoint(), 0.01);
        Assert.assertTrue(function.value(result.getPoint()) < 0);
    }

    @Test
    public void testBracketing() {
        BracketFinder bracketFinder = new BracketFinder();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 2 * Math.pow(x, 3) - 4 * x;
            }
        };
        PointValuePair[] brackets = new PointValuePair[3];
        bracketFinder.search(f, null, 0, 1);
        brackets[0] = bracketFinder.getLo();
        bracketFinder.search(f, null, brackets[0].getValue(), brackets[0].getValue() + 1);
        brackets[1] = bracketFinder.getLo();
        bracketFinder.search(f, null, brackets[1].getValue(), brackets[1].getValue() + 1);
        brackets[2] = bracketFinder.getLo();

        Assert.assertArrayEquals(new double[]{Arrays.stream(startPoint).sum() / 2.0, 1.0},
                new double[]{brackets[0].getPoint()[0], brackets[2].getPoint()[0]}, 0.01);
    }

    @Test(expected = org.apache.commons.math3.optim.MaxEvalException.class)
    public void testMaxEvalException() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1e-5, 1e-5, new SimpleUnivariateValueChecker(1e-5, 1e-5));
        brentOptimizer.optimize(function, null, null, -1.0, 1.0, 100000);
    }
}