
package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexWithNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.getSimplex());
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        optimizer.setConvergenceChecker(checker);
        optimizer.setStartPoint(new double[]{1.0, 1.0});
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimize(100, function, goalType);
        assertNotNull(result);
        assertEquals(2.0, result.getValue(), 0.0);
        assertEquals(0.0, result.getPoint()[0], 0.0);
        assertEquals(2.0, result.getPoint()[1], 0.0);
    }
}
