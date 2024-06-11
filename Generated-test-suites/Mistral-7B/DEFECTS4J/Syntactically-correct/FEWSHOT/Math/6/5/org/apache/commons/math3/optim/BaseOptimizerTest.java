package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new MaxEval(10);
        assertEquals(10, optimizer.getMaxEvaluations());

        BaseOptimizer<Object> optimizerNoMaxEval = new BaseOptimizer<Object>() {
            @Override
            public int getMaxEvaluations() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
        assertNotNull(optimizerNoMaxEval);
        try {
            optimizerNoMaxEval.getMaxEvaluations();
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            private int evaluations = 0;

            @Override
            public int getEvaluations() {
                return evaluations;
            }

            public void optimize(Object initialGuess) {}

            public void optimize(Object initialGuess, ConvergenceChecker<? super Object, ? extends Object> convergenceChecker) {
                evaluations++;
            }
        };

        optimizer.optimize(new Object[]{});
        optimizer.optimize(new Object[]{});
        optimizer.optimize(new Object[]{});
        assertEquals(3, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            private ConvergenceChecker<Object, Object> convergenceChecker = new MaxIter(5);

            @Override
            public ConvergenceChecker<Object, Object> getConvergenceChecker() {
                return convergenceChecker;
            }
        };

        assertEquals(MaxIter.class, optimizer.getConvergenceChecker().getClass());
    }
}