package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimpleXPoint;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.direct.GoalType;
import org.apache.commons.math3.optim.direct.SimplexOptimizer;
import org.apache.commons.math3.optim.nonlinear.ScaledSphereFunction;
import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSimpleOptimization() throws Exception {
        MultivariateFunction f = new ScaledSphereFunction(2.0, 3.0);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
        double[] initPoint = new double[]{-1.0, -1.0};
        OptimizationData data = new OptimizationData(new SimpleXPoint(initPoint), optimizer.getSimplex());
        PointValuePair result = optimizer.optimize(100, f, goalType, data);
        Assert.assertNotNull(result);
        double[] optimalPoint = result.getPoint();
        double optimalValue = result.getValue();
        double expectedValue = -3.0 * Math.PI / 4.0;
        Assert.assertEquals(expectedValue, optimalValue, 1e-5);
        Assert.assertArrayEquals(new double[]{-Math.sqrt(3.0) / 2.0, -Math.sqrt(3.0) / 2.0}, optimalPoint, 1e-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        MultivariateFunction f = null;
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
        GoalType goalType = GoalType.MINIMIZE;
        double[] initPoint = new double[]{-1.0, -1.0};
        OptimizationData data = new OptimizationData(new SimpleXPoint(initPoint), optimizer.getSimplex());
        optimizer.optimize(100, f, goalType, data);
    }

    @Test(expected = UnboundedDomainException.class)
    public void testUnboundedFunction() throws Exception {
        MultivariateFunction f = new ScaledSphereFunction(2.0, 3.0);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
        GoalType goalType = GoalType.MINIMIZE;
        double[] initPoint = new double[]{1.0, 1.0};
        OptimizationData data = new OptimizationData(new SimpleXPoint(initPoint), optimizer.getSimplex());
        optimizer.optimize(100, f, goalType, data);
    }

    @Test
    public void testErrorConvergenceChecker() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair current, PointValuePair best) {
                return false;
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        MultivariateFunction f = new ScaledSphereFunction(2.0, 3.0);
        GoalType goalType = GoalType.MINIMIZE;
        double[] initPoint = new double[]{-1.0, -1.0};
        OptimizationData data = new OptimizationData(new SimpleXPoint(initPoint), optimizer.getSimplex());
        PointValuePair result = optimizer.optimize(100, f, goalType, data);
        Assert.assertNull(result);
    }

    @Test
    public void testSimplexNotSet() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = new ScaledSphereFunction(2.0, 3.0);
        GoalType goalType = GoalType.MINIMIZE;
        double[] initPoint = new double[]{-1.0, -1.0};
        OptimizationData data = new OptimizationData(new SimpleXPoint(initPoint), optimizer.getSimplex());
        try {
            optimizer.optimize(100, f, goalType, data);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }
}