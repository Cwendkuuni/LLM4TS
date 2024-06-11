package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimplexOptimizerTest {
    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }
    
    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex(2);
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }
    
    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimizeInternal(100, new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        }, GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 0.00001);
        assertEquals(0.0, result.getPoint()[0], 0.00001);
        assertEquals(0.0, result.getPoint()[1], 0.00001);
    }
    
    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new Simplex(2);
        simplex.setStartConfiguration(new double[] {1.0, 1.0}, 1.0);
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        assertEquals(2.0, result.getValue(), 0.00001);
        assertEquals(1.0, result.getPoint()[0], 0.00001);
        assertEquals(1.0, result.getPoint()[1], 0.00001);
    }
}