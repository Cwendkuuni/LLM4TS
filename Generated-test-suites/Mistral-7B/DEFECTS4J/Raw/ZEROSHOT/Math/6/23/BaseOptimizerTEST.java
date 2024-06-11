package org.apache.commons.math3.optimization.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.StoppingCriteria;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

/**
 * Test class for {@link BaseOptimizer}.
 *
 * @author Chris Bartley (bartley@apache.org)
 */
public class BaseOptimizerTest {
    /**
     * Test getMaxEvaluations method.
     */
    @org.junit.Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public int getMaxEvaluations() {
                return MaxEval.MAX_EVAL;
            }
        };
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(MaxEval.MAX_EVAL, maxEvaluations);
    }

    /**
     * Test getEvaluations method.
     */
    @org.junit.Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            private int evaluations = 0;

            @Override
            public int getEvaluations() {
                return evaluations;
            }

            public void optimize() {
                evaluations++;
            }
        };
        optimizer.optimize();
        optimizer.optimize();
        int evaluations = optimizer.getEvaluations();
        assertEquals(2, evaluations);
    }

    /**
     * Test getConvergenceChecker method.
     */
    @org.junit.Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return new SimpleValueChecker(1.0e-6);
            }
        };
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertTrue(convergenceChecker instanceof SimpleValueChecker);
        assertEquals(1.0e-6, ((SimpleValueChecker) convergenceChecker).getTolerance(), 1.0e-8);

        conversionsChecker = optimizer.getConvergenceChecker();
        assertSame(convergenceChecker, conversionsChecker);
    }

    /**
     * Test getConvergenceChecker method with null return.
     */
    @org.junit.Test
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return null;
            }
        };
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test getConvergenceChecker method with no getter.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerNoGetter() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                throw new UnsupportedOperationException();
            }
        };
        optimizer.getConvergenceChecker();
    }

    /**
     * Test setConvergenceChecker method with null argument.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testSetConvergenceCheckerNull() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            private ConvergenceChecker<PointValuePair> convergenceChecker;

            @Override
            public void setConvergenceChecker(ConvergenceChecker<PointValuePair> convergenceChecker) {
                this.convergenceChecker = convergenceChecker;
            }

            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return convergenceChecker;
            }
        };
        optimizer.setConvergenceChecker(null);
    }

    /**
     * Test setConvergenceChecker method.
     */
    @org.junit.Test
    public void testSetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new BaseOptimizer<PointValuePair>() {
            private ConvergenceChecker<PointValuePair> convergenceChecker;

            @Override
            public void setConvergenceChecker(ConvergenceChecker<PointValuePair> convergenceChecker) {
                this.convergenceChecker = convergenceChecker;
            }

            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return convergenceChecker;
            }
        };
        ConvergenceChecker<PointValuePair> convergenceChecker = new StoppingCriteria(10);
        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
}