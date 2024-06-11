package org.apache.commons.math3.ode.nonstiff;

import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
    
    class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        
        public TestEmbeddedRungeKuttaIntegrator() {
            super("TestEmbeddedRungeKuttaIntegrator", true, new double[]{}, new double[][]{}, new double[]{}, null, 0.0, 0.0, 0.0, 0.0);
        }
        
        @Override
        public int getOrder() {
            return 0;
        }
        
        @Override
        protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
            return 0.0;
        }
        
        @Override
        protected void computeDerivatives(final double p0, final double[] p1, final double[] p2) {
            
        }
    }

    @Test
    public void testGetSafety() {
        TestEmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expected = 0.9;
        double actual = integrator.getSafety();
        Assert.assertEquals(expected, actual, 0.0);
    }
    
    @Test
    public void testSetSafety() {
        TestEmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expected = 0.5;
        integrator.setSafety(expected);
        double actual = integrator.getSafety();
        Assert.assertEquals(expected, actual, 0.0);
    }
    
    @Test
    public void testGetMinReduction() {
        TestEmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expected = 0.2;
        double actual = integrator.getMinReduction();
        Assert.assertEquals(expected, actual, 0.0);
    }
    
    @Test
    public void testSetMinReduction() {
        TestEmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expected = 0.1;
        integrator.setMinReduction(expected);
        double actual = integrator.getMinReduction();
        Assert.assertEquals(expected, actual, 0.0);
    }
    
    @Test
    public void testGetMaxGrowth() {
        TestEmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expected = 10.0;
        double actual = integrator.getMaxGrowth();
        Assert.assertEquals(expected, actual, 0.0);
    }
    
    @Test
    public void testSetMaxGrowth() {
        TestEmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expected = 5.0;
        integrator.setMaxGrowth(expected);
        double actual = integrator.getMaxGrowth();
        Assert.assertEquals(expected, actual, 0.0);
    }
}