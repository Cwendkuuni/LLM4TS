package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.exception.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegratorImpl();
    }

    @Test
    public void testGetOrder() {
        int expected = 4;
        int actual = integrator.getOrder();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSafety() {
        double expected = 0.9;
        double actual = integrator.getSafety();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testSetSafety() {
        double expected = 0.5;
        integrator.setSafety(expected);
        double actual = integrator.getSafety();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetMinReduction() {
        double expected = 0.2;
        double actual = integrator.getMinReduction();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testSetMinReduction() {
        double expected = 0.1;
        integrator.setMinReduction(expected);
        double actual = integrator.getMinReduction();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        double expected = 10.0;
        double actual = integrator.getMaxGrowth();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        double expected = 5.0;
        integrator.setMaxGrowth(expected);
        double actual = integrator.getMaxGrowth();
        assertEquals(expected, actual, 0.0);
    }

    private class EmbeddedRungeKuttaIntegratorImpl extends EmbeddedRungeKuttaIntegrator {

        public EmbeddedRungeKuttaIntegratorImpl() {
            super("test", true, new double[]{}, new double[][]{}, new double[]{}, new RungeKuttaStepInterpolator(), 0.0, 0.0, 0.0, 0.0);
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
        protected void computeDerivatives(double stepStart, double[] y2, double[] yDotK) {
        }

        @Override
        protected double filterStep(double scaledH, boolean forward, boolean nextIsLast) {
            return 0.0;
        }

        @Override
        protected double initializeStep(boolean forward, int order, double[] scale, double stepStart, double[] y2, double[] yDotK, double[] yTmp, double[] yDotK1) {
            return 0.0;
        }

        @Override
        protected double acceptStep(RungeKuttaStepInterpolator interpolator, double[] y2, double[] yDotTmp, double t) {
            return 0.0;
        }

        @Override
        protected void resetInternalState() {
        }
    }
}