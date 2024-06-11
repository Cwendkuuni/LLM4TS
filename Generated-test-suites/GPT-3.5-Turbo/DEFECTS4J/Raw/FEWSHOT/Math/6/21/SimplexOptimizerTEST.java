package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimplexOptimizerTest {
    @Test
    public void testSetSimplex() {
        AbstractSimplex simplex = new AbstractSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        // Add assertions here
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        AbstractSimplex simplex = new AbstractSimplex();
        simplex.build(new double[]{});
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return 0;
            }
        }, new Comparator<PointValuePair>() {
            public int compare(final PointValuePair o1,
                               final PointValuePair o2) {
                return 0;
            }
        });
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.doOptimize();
        // Add assertions here
    }
}