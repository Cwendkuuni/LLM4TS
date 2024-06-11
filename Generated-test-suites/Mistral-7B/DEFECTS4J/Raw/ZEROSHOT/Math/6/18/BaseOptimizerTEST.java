package org.apache.commons.math3.optimization.base;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

    private BaseOptimizer<PAIR> optimizer;
    private ConvergenceChecker<PAIR> convergenceChecker = new ConvergenceChecker<PAIR>() {
        @Override
        public boolean isConverged(PAIR current, PAIR best) {
            return false;
        }
    };

    @Test
    public void testGetMaxEvaluations() {
        optimizer = new BaseOptimizer<PAIR>() {
            @Override
            public int getMaxEvaluations() {
                return 10;
            }
        };
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        optimizer = new BaseOptimizer<PAIR>() {
            private int evaluations = 0;
            @Override
            public int getEvaluations() {
                return evaluations;
            }
            @Override
            public void optimize(OptimizationData<PAIR> optimizationData) {
                evaluations++;
            }
        };
        assertEquals(0, optimizer.getEvaluations());
        optimizer.optimize(new OptimizationData<PAIR>() {
            @Override
            public PAIR getPoint() {
                return null;
            }
        });
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        optimizer = new BaseOptimizer<PAIR>() {
            @Override
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return convergenceChecker;
            }
        };
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() {
        optimizer = new BaseOptimizer<PAIR>() {
            @Override
            public void optimize(OptimizationData<PAIR> optimizationData) {
                // empty implementation
            }
        };
        optimizer.optimize(new OptimizationData<PAIR>() {
            @Override
            public PAIR getPoint() {
                return null;
            }
        });
        optimizer.getPoint();
        optimizer.getValue();
    }

    @Test
    public void testGetMaxEvaluationWithMaxEvalAnnotation() {
        optimizer = new MaxEval(10) {
            @Override
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return convergenceChecker;
            }
        };
        assertEquals(10, optimizer.getMaxEvaluations());
    }
}