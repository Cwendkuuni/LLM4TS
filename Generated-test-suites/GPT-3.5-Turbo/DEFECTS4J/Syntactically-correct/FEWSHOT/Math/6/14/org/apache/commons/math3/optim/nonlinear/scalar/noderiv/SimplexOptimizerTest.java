package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

public class SimplexOptimizerTest {
    
    @Test
    public void testOptimizeInternalNoSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        try {
            optimizer.optimizeInternal(100, new MultivariateFunction() {
                public double value(double[] point) {
                    return 0;
                }
            }, GoalType.MINIMIZE);
            fail("NullArgumentException expected");
        } catch (NullArgumentException ex) {
            // Expected
        }
    }
    
    @Test
    public void testOptimizeInternalConverge() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return iteration >= 10;
            }
        });
        PointValuePair result = optimizer.optimizeInternal(100, new MultivariateFunction() {
            public double value(double[] point) {
                return 0;
            }
        }, GoalType.MINIMIZE, new AbstractSimplex() {});
        
        assertEquals(0, result.getValue(), 0);
        assertEquals(0, result.getPoint()[0], 0);
    }
}