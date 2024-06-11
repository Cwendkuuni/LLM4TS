package org.apache.commons.math3.ode.nonstiff;

import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        int expectedOrder = 4;
        int actualOrder = integrator.getOrder();
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expectedSafety = 0.9;
        double actualSafety = integrator.getSafety();
        assertEquals(expectedSafety, actualSafety, 0.0);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expectedSafety = 0.8;
        integrator.setSafety(expectedSafety);
        double actualSafety = integrator.getSafety();
        assertEquals(expectedSafety, actualSafety, 0.0);
    }

    @Test
    public void testIntegrate() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        ExpandableStatefulODE equations = new TestExpandableStatefulODE();
        double t = 10.0;

        try {
            integrator.integrate(equations, t);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expectedMinReduction = 0.2;
        double actualMinReduction = integrator.getMinReduction();
        assertEquals(expectedMinReduction, actualMinReduction, 0.0);
    }

    @Test
    public void testSetMinReduction() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expectedMinReduction = 0.3;
        integrator.setMinReduction(expectedMinReduction);
        double actualMinReduction = integrator.getMinReduction();
        assertEquals(expectedMinReduction, actualMinReduction, 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expectedMaxGrowth = 10.0;
        double actualMaxGrowth = integrator.getMaxGrowth();
        assertEquals(expectedMaxGrowth, actualMaxGrowth, 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double expectedMaxGrowth = 15.0;
        integrator.setMaxGrowth(expectedMaxGrowth);
        double actualMaxGrowth = integrator.getMaxGrowth();
        assertEquals(expectedMaxGrowth, actualMaxGrowth, 0.0);
    }

    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public TestEmbeddedRungeKuttaIntegrator() {
            super("test", true, new double[]{}, new double[][]{}, new double[]{}, null, 0.0, 0.0, 0.0, 0.0);
        }

        @Override
        public int getOrder() {
            return 4;
        }

        @Override
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            return 0.0;
        }

        @Override
        protected void computeDerivatives(double v, double[] doubles, double[] doubles1) {

        }

        @Override
        public StepInterpolator prototypeCopy() {
            return null;
        }

        @Override
        protected double filterStep(double v, boolean b, boolean b1) {
            return 0.0;
        }
    }

    private class TestExpandableStatefulODE extends ExpandableStatefulODE {
        public TestExpandableStatefulODE() {
            super(null);
        }

        @Override
        public double[] getCompleteState() {
            return new double[]{};
        }

        @Override
        public void setCompleteState(double[] doubles) {

        }

        @Override
        public double getTime() {
            return 0.0;
        }

        @Override
        public void setTime(double v) {

        }

        @Override
        public int getDimension() {
            return 0;
        }
    }

}