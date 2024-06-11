package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(5, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.9, integrator.getSafety(), 0.001);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setSafety(0.5);
        assertEquals(0.5, integrator.getSafety(), 0.001);
    }

    @Test
    public void testGetIntegrate() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        TestExpandableStatefulODE equations = new TestExpandableStatefulODE();
        try {
            integrator.integrate(equations, 1.0);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.001);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        integrator.setMaxGrowth(20.0);
        assertEquals(20.0, integrator.getMaxGrowth(), 0.001);
    }

    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        private TestEmbeddedRungeKuttaIntegrator() {
            super("testIntegrator", true, new double[]{}, new double[][]{}, new double[]{}, null, 0.0, 0.0, 0.0, 0.0);
        }

        @Override
        public int getOrder() {
            return 5;
        }

        @Override
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 0.0;
        }
    }

    private class TestExpandableStatefulODE extends ExpandableStatefulODE {
        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public double[] getCompleteState() {
            return new double[]{};
        }

        @Override
        public void setTime(double time) {

        }

        @Override
        public void setCompleteState(double[] completeState) {

        }

        @Override
        public int getPrimaryMapperDimension() {
            return 0;
        }

        @Override
        public int getSecondaryMapperDimension(int index) {
            return 0;
        }

        @Override
        public void computeDerivatives(double time, double[] y, double[] yDot) {

        }
    }
}