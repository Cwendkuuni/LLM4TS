/*
Test case for the SimplexOptimizer class
*/

package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullSimplex() {
        optimizer.optimizeInternal(100, new MultivariateFunction() {
            public double value(double[] point) {
                return 0;
            }
        }, GoalType.MINIMIZE, new OptimizationData[] {});
    }

    @Test
    public void testDoOptimize_Converges() {
        final PointValuePair result = optimizer.optimizeInternal(100,
            new MultivariateFunction() {
                public double value(double[] point) {
                    return point[0] * point[0];
                }
            }, GoalType.MINIMIZE, new OptimizationData[] {
                new AbstractSimplex() {
                    public void build(double[] startPoint) {}
                    public void evaluate(MultivariateFunction evalFunc,
                                         Comparator<PointValuePair> comparator) {}
                    public void iterate(MultivariateFunction evalFunc,
                                        Comparator<PointValuePair> comparator) {}
                }
            });

        assertEquals(0.0, result.getPoint()[0], 0.0);
    }

    // More test cases can be added here

}