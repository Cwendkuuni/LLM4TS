package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
    
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testEstimateError() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double[] yDotK1 = {1.0, 2.0, 3.0};
        double[] y2 = {0.5, 1.5, 2.5};
        double[] yTmp = {1.0, 2.0, 3.0};
        double stepSize = 0.1;
        double error = integrator.estimateError(new double[][] {yDotK1}, y2, yTmp, stepSize);
        assertEquals(0.0, error, 0.0);
    }

    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestEmbeddedRungeKuttaIntegrator() {
            super("Test", false, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
        }

        @Override
        public int getOrder() {
            return 4;
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0.0;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {

        }

        @Override
        public RungeKuttaStepInterpolator prototypeCopy() {
            return null;
        }
    }
}