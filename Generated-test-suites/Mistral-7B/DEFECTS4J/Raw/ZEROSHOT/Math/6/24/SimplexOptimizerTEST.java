package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optim.AbstractOptimizer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for SimplexOptimizer class.
 */
public class SimplexOptimizerTest {

    /** Test function for optimization. */
    private static final MultivariateFunction TEST_FUNCTION = new MultivariateFunction() {
        public double value(double[] x) {
            double sum = 0.0;
            for (int i = 0; i < x.length; i++) {
                sum += Math.pow(x[i] - 1.0, 2.0);
            }
            return sum;
        }
    };

    /** Test goal type. */
    private static final GoalType GOAL_TYPE = GoalType.MINIMIZE;

    /** Test convergence checker. */
    private static final ConvergenceChecker<PointValuePair> CHECKER
        = new SimpleValueChecker(1.0e-6, 1.0e-6);

    /** Test simplex. */
    private static final AbstractSimplex SIMPLEX
        = new Amos11Simplex(2, CHECKER);

    /** Test optimization data. */
    private static final OptimizationData DATA = new OptimizationData() {
        @Override
        public AbstractSimplex getSimplex() {
            return SIMPLEX;
        }

        @Override
        public double[] getStartPoint() {
            return new double[] {1.0, 1.0};
        }
    };

    /** Test optimizer. */
    private static final SimplexOptimizer OPTIMIZER
        = new SimplexOptimizer(CHECKER);

    /** Test optimization. */
    @Test
    public void testOptimization() {
        OPTIMIZER.optimize(1000, TEST_FUNCTION, GOAL_TYPE, DATA);
        PointValuePair result = OPTIMIZER.getOptimum();
        assertNull(result);
    }
}