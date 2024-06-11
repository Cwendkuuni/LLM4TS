package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SimplexOptimizer} class.
 */
public class SimplexOptimizerTest {
    /** Test function for optimization. */
    private static final MultivariateFunction TEST_FUNCTION = new MultivariateFunction() {
        public double value(double[] x) {
            return Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 3.0, 2.0);
        }
    };

    /**
     * Tests optimization with a simple goal, {@link GoalType#MINIMIZE}.
     */
    @Test
    public void testMinimize() {
        AbstractSimplex simplex = new NelsonSehgalSimplex(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(50, TEST_FUNCTION, GoalType.MINIMIZE, new double[] {1.0, 1.0});
        Assert.assertEquals(1.5, result.getPoint()[0], 1e-6);
        Assert.assertEquals(1.5, result.getPoint()[1], 1e-6);
        Assert.assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Tests optimization with a complex goal, {@link GoalType#MINIMIZE_WITH_MARGIN}.
     */
    @Test
    public void testMinimizeWithMargin() {
        AbstractSimplex simplex = new NelderMeadSimplex(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setSimplex(simplex);
        double[] startPoint = new double[] {4.0, 4.0};
        PointValuePair result = optimizer.optimize(50, TEST_FUNCTION, GoalType.MINIMIZE_WITH_MARGIN, startPoint, 2.0);
        Assert.assertEquals(2.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(2.0, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Tests optimization with a convergence checker.
     */
    @Test
    public void testConvergenceChecker() {
        AbstractSimplex simplex = new NelderMeadSimplex(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return (Math.abs(previous.getValue() - current.getValue()) < 1e-6);
            }
        });
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(50, TEST_FUNCTION, GoalType.MINIMIZE, new double[] {1.0, 1.0});
        Assert.assertEquals(1.5, result.getPoint()[0], 1e-6);
        Assert.assertEquals(1.5, result.getPoint()[1], 1e-6);
        Assert.assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Tests optimization with a null optimization function.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        AbstractSimplex simplex = new NelderMeadSimplex(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setSimplex(simplex);
        optimizer.optimize(50, null, GoalType.MINIMIZE, new double[] {1.0, 1.0});
    }

    /**
     * Tests optimization with a null optimization data.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullOptimizationData() {
        AbstractSimplex simplex = new NelsonSehgalSimplex(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setSimplex(simplex);
        optimizer.optimize(50, TEST_FUNCTION, null, new double[] {1.0, 1.0});
    }

    /**
     * Tests optimization with a null
     * {@link AbstractSimplex simplex}.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.optimize(50, TEST_FUNCTION, GoalType.MINIMIZE, null);
    }
}