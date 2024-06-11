package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractSimplexTest {
    
    @Test
    public void testAbstractSimplexConstructor() {
        // Test constructor with default side length
        AbstractSimplex simplex = new TestSimplex(3);
        assertEquals(3, simplex.getDimension());
        
        // Test constructor with custom side length
        simplex = new TestSimplex(4, 2.0);
        assertEquals(4, simplex.getDimension());
        
        // Test constructor with custom steps
        double[] steps = {1.0, 2.0, 3.0};
        simplex = new TestSimplex(steps);
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testBuild() {
        AbstractSimplex simplex = new TestSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        
        assertEquals(4, simplex.getSize());
    }
}

class TestSimplex extends AbstractSimplex {
    public TestSimplex(int n) {
        super(n);
    }
    
    public TestSimplex(int n, double sideLength) {
        super(n, sideLength);
    }
    
    public TestSimplex(double[] steps) {
        super(steps);
    }
    
    @Override
    public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
        // Do nothing for testing
    }
}