package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.multivariate.RankingSimplex;
import org.apache.commons.math3.optim.multivariate.RankingSimplex.Strategy;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.OptimizationData;
import org.junit.Test;

/**
 * Test cases for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /** Test function. */
    private static final MultivariateFunction TEST_FUNCTION = new MultivariateFunction() {
        public double value(double[] x) {
            return FunctionUtils.pow(FunctionUtils.sum(x), 2) + Math.sin(FunctionUtils.sum(x));
        }
    };

    /**
     * Test optimize method with a simple goal type.
     */
    @Test
    public void testOptimizeSimpleGoalType() {
        RankingSimplex simplex = new RankingSimplex(3, Strategy.RANDOM_SEED);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 100));
        PointValuePair opt = optimizer.optimize(100, TEST_FUNCTION, GoalType.MINIMIZE, null);
        assertNotNull(opt);
        double[] optPoint = opt.getPoint();
        double optValue = opt.getValue();
        // Manually check if the optimization was successful.
        assertTrue(TEST_FUNCTION.value(optPoint) < 1e-6);
    }

    /**
     * Test optimize method with a custom optimization data.
     */
    @Test
    public void testOptimizeCustomData() {
        RankingSimplex simplex = new RankingSimplex(3, Strategy.RANDOM_SEED);
        double[] initialGuess = new double[] { 1.0, 2.0, 3.0 };
        OptimizationData data = new OptimizationData() {
            @Override
            public double[] getInitialGuess() {
                return initialGuess;
            }

            @Override
            public AbstractSimplex getSimplex() {
                return simplex;
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 100));
        PointValuePair opt = optimizer.optimize(100, TEST_FUNCTION, GoalType.MINIMIZE, data);
        assertNotNull(opt);
        double[] optPoint = opt.getPoint();
        double optValue = opt.getValue();
        // Manually check if the optimization was successful.
        assertTrue(TEST_FUNCTION.value(optPoint) < 1e-6);
    }
}