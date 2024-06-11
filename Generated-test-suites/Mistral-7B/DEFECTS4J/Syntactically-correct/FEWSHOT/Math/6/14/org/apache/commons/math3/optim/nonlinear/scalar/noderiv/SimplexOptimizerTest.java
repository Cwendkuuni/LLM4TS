package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexDirectSearch;
import org.junit.Test;

public class SimplexOptimizerTest {

    private static final double EPS = 1e-9;

    @Test
    public void testSimpleOptimization() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        AbstractSimplex simplex = new SimplexDirectSearch();
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(EPS, EPS), simplex);
        PointValuePair opt = optimizer.optimize(1000, f, GoalType.MINIMIZE, new double[]{1., 1.});
        assertEquals(0., opt.getValue(), EPS);
        assertEquals(new double[]{0., 0.}, opt.getPoint(), EPS);
    }

    @Test
    public void testSimpleOptimizationWithInitialGuess() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        AbstractSimplex simplex = new SimplexDirectSearch();
        OptimizationData data = new OptimizationData() {
            @Override
            public AbstractSimplex getSimplex() {
                return simplex;
            }

            @Override
            public double[] getInitialGuess() {
                return new double[]{0.5, 0.5};
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(EPS, EPS), data);
        PointValuePair opt = optimizer.optimize(1000, f, GoalType.MINIMIZE, new OptimizationData[0]);
        assertEquals(0., opt.getValue(), EPS);
        assertEquals(new double[]{0., 0.}, opt.getPoint(), EPS);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(EPS, EPS));
        optimizer.optimize(1000, null, GoalType.MINIMIZE, new OptimizationData[0]);
    }

    @Test
    public void testMaximization() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return -FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };
        AbstractSimplex simplex = new SimplexDirectSearch();
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(EPS, EPS), simplex);
        PointValuePair opt = optimizer.optimize(1000, f, GoalType.MAXIMIZE, new double[]{1., 1.});
        assertEquals(0., -opt.getValue(), EPS);
        assertEquals(new double[]{0., 0.}, opt.getPoint(), EPS);
    }

    @Test
    public void testConvergenceChecker() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.abs(FunctionUtils.sum(x) - 1.);
            }
        };
        AbstractSimplex simplex = new SimplexDirectSearch();
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(0.1, 0.1), simplex);
        PointValuePair opt = optimizer.optimize(1000, f, GoalType.MINIMIZE, new double[]{1., 1.});
        assertTrue(optimizer.getConvergenceChecker().isConverged(1, opt, new PointValuePair[]{new PointValuePair(1., 1., 1.), opt}));
    }
}