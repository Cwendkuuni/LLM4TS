package org.apache.commons.math3.ode.nonstiff;

import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        RungeKuttaIntegrator integrator = new DummyEmbeddedRungeKuttaIntegrator();
        Assert.assertEquals(0, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        RungeKuttaIntegrator integrator = new DummyEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.5);
        Assert.assertEquals(0.5, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        RungeKuttaIntegrator integrator = new DummyEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.7);
        Assert.assertEquals(0.7, integrator.getSafety(), 0.0);
    }

    @Test
    public void testIntegrate() {
        // Generate test case for integrate method.
        // TODO: Add test case here.
    }

    @Test
    public void testGetMinReduction() {
        RungeKuttaIntegrator integrator = new DummyEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        Assert.assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        RungeKuttaIntegrator integrator = new DummyEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.8);
        Assert.assertEquals(0.8, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        RungeKuttaIntegrator integrator = new DummyEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(2.0);
        Assert.assertEquals(2.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        RungeKuttaIntegrator integrator = new DummyEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(3.0);
        Assert.assertEquals(3.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testEstimateError() {
        // Generate test case for estimateError method.
        // TODO: Add test case here.
    }

    class DummyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public DummyEmbeddedRungeKuttaIntegrator() {
            super("Dummy", false, new double[]{}, new double[][]{}, new double[]{}, null, 0.0, 0.0, 0.0, 0.0);
        }

        @Override
        public int getOrder() {
            return 0;
        }
	
        @Override
        protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
            // Dummy implementation
            return 0.0;
        }
    }
}