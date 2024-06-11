package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;

/**
 * Testing the BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    @Test(expected = UnboundedDomainException.class)
    public void testGetMaxEvaluationsWithNegativeValue() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public int getMaxEvaluations() {
                return -1;
            }
        };

        optimizer.getMaxEvaluations();
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public int getMaxEvaluations() {
                return MaxEval.ITERATIONS;
            }
        };

        assertEquals(MaxEval.ITERATIONS, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            int evaluations = 0;

            @Override
            public int getEvaluations() {
                return evaluations;
            }

            @Override
            public PointValuePair optimize(GoalType goalType, PointValuePair initialPoint, ConvergenceChecker<PointValuePair> convergenceChecker, SimpleBounds bounds) {
                evaluations++;
                return new PointValuePair(new double[]{1, 2}, 3.0);
            }
        };

        assertNull(optimizer.getEvaluations());

        optimizer.optimize(GoalType.MINIMIZE, new PointValuePair(new double[]{0, 0}, Double.NaN), new ConvergenceChecker<PointValuePair>() {}, new SimpleBounds(new double[]{0.0, 0.0}, new double[]{1.0, 1.0}));

        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return new ConvergenceChecker<PointValuePair>() {
                    @Override
                    public boolean isConverged(PointValuePair currentPoint, PointValuePair previousPoint, int iterationNumber) {
                        return true;
                    }
                };
            }
        };

        assertNull(optimizer.getConvergenceChecker());

        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
}