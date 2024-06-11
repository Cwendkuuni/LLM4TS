package org.apache.commons.math3.ode.nonstiff;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegratorImpl();
    }

    @Test
    public void testGetSafety() {
        Assert.assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.5);
        Assert.assertEquals(0.5, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        Assert.assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.1);
        Assert.assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        Assert.assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        Assert.assertEquals(5.0, integrator.getMaxGrowth(), 0.0);
    }

    private class EmbeddedRungeKuttaIntegratorImpl extends EmbeddedRungeKuttaIntegrator {

        public EmbeddedRungeKuttaIntegratorImpl() {
            super("testIntegrator", false, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        }

        @Override
        public int getOrder() {
            return 0;
        }

        @Override
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 0.0;
        }
    }
}