package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionAdapter;
import org.apache.commons.math3.optim.univariate.ValueChecker;
import org.apache.commons.math3.optim.univariate.noderiv.BrentNewtonRaphsonOptimizer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PowellOptimizerTest {

    private double relativeThreshold;
    private double absoluteThreshold;
    private double lineRelativeThreshold;
    private double lineAbsoluteThreshold;
    private ConvergenceChecker<PointValuePair> customChecker;

    public PowellOptimizerTest(double rel, double abs, double lineRel, double lineAbs, ConvergenceChecker<PointValuePair> customChecker) {
        this.relativeThreshold = rel;
        this.absoluteThreshold = abs;
        this.lineRelativeThreshold = lineRel;
        this.lineAbsoluteThreshold = lineAbs;
        this.customChecker = customChecker;
    }

    @Parameterized.Parameters(name = "PowellOptimizer({0}, {1}, {2}, {3}, {4})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0.001, 1e-6, 0.0001, 1e-8, null},
                {0.01, 1e-4, 0.001, 1e-6, new SimpleValueChecker(0.01, 1e-6)},
                {0.1, 1e-2, 0.1, 1e-1, new SimpleValueChecker(0.1, 1e-1)}
        });
    }

    @Test
    public void testCreate() throws NumberIsTooSmallException {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);
        assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(RelativeThresholdType.VALUE, optimizer.getRelativeThresholdType());
        assertEquals(AbsoluteThresholdType.VALUE, optimizer.getAbsoluteThresholdType());
    }

    @Test
    public void testCreateWithCustomChecker() throws NumberIsTooSmallException {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold, customChecker);
        assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(customChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testCreateWithLineSearch() throws NumberIsTooSmallException {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold, lineRelativeThreshold, lineAbsoluteThreshold);
        assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(lineRelativeThreshold, optimizer.getLineSearchRelativeThreshold(), 0.0);
        assertEquals(lineAbsoluteThreshold, optimizer.getLineSearchAbsoluteThreshold(), 0.0);
        assertEquals(BrentNewtonRaphsonOptimizer.class, optimizer.getLineSearch().getClass());
    }

    @Test
    public void testCreateWithCustomLineSearch() throws NumberIsTooSmallException {
        UnivariateFunctionAdapter function = new UnivariateFunctionAdapter(x -> Math.pow(Math.sin(x), 2));
        BrentOptimizer lineSearch = new BrentOptimizer(lineRelativeThreshold, lineAbsoluteThreshold, new SimpleValueChecker(0.0, 1e-6));
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold, lineSearch);
        assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(lineSearch, optimizer.getLineSearch());
    }

    @Test
    public void testOptimizeUnevenDimensions() {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);
        try {
            optimizer.optimize(new MultivariateFunction() {
                @Override
                public PointValuePair value(double[] x) {
                    return new PointValuePair(new double[]{}, 0);
                }
            });
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Number of variables must be greater than zero", e.getMessage());
        }
    }

    @Test
    public void testMinimizeSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);
        double[] start = new double[]{3.0, 3.0};
        double[] result = optimizer.optimize(new MultivariateFunction() {
            @Override
            public PointValuePair value(double[] x) {
                double f = 100.0 * (Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 2.0, 2.0));
                return new PointValuePair(x, f);
            }
        }, start);
        assertEquals(2.0, result[0][0], 0.0);
        assertEquals(2.0, result[0][1], 0.0);
        assertEquals(1.0e-8, result[1], 1e-8);
    }

    @Test
    public void testMinimizeFunctionWithMovingStartPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold, new SimpleValueChecker(0.0, 1e-6));
        double[] start = new double[]{3.0, 3.0};
        double[] result = optimizer.optimize(new MultivariateFunction() {
            @Override
            public PointValuePair value(double[] x) {
                double f = Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2.0);
                return new PointValuePair(x, f);
            }
        }, start);
        assertTrue(result[1] < 1e-6);
    }

    @Test
    public void testMinimizeFunctionWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold, customChecker);
        double[] start = new double[]{3.0, 3.0};
        double[] result = optimizer.optimize(new MultivariateFunction() {
            @Override
            public PointValuePair value(double[] x) {
                double f = Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2.0);
                return new PointValuePair(x, f);
            }
        }, start);
        assertTrue(customChecker.converged(1, new PointValuePair[]{new PointValuePair(start, 1.0), new PointValuePair(result, result[1])}));
    }

    @Test
    public void testMinimizeFunctionWithCustomLineSearch() {
        UnivariateFunction function = new UnivariateFunctionAdapter(x -> Math.pow(Math.sin(x), 2.0));
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold, lineAbsoluteThreshold, new CustomLineSearch(lineRelativeThreshold, new SimpleValueChecker(0.0, 1e-6)));
        double[] start = new double[]{3.0, 3.0};
        double[] result = optimizer.optimize(new MultivariateFunction() {
            @Override
            public PointValuePair value(double[] x) {
                double f = function.value(x[0]);
                return new PointValuePair(x, f);
            }
        }, start);
        assertTrue(lineAbsoluteThreshold <= result[1] && result[1] <= Math.pow(Math.E, 2.0));
    }

    private static class CustomLineSearch extends BrentOptimizer {
        CustomLineSearch(double rel, ValueChecker valueChecker) {
            super(rel, Double.POSITIVE_INFINITY, valueChecker);
        }

        @Override
        protected UnivariateFunction getFunctionToMinimize() {
            return new UnivariateFunction() {
                @Override
                public double value(double x) {
                    return Math.pow(Math.sin(x), 2.0);
                }
            };
        }
    }
}