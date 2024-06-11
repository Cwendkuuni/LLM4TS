package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.junit.Test;
import org.junit.Assert;

public class AbstractSimplexTest {

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        Assert.assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        Assert.assertEquals(0, simplex.getSize());
        simplex.build(new double[]{1.0, 2.0, 3.0});
        Assert.assertEquals(4, simplex.getSize());
    }

    // Implement tests for other methods
    
}