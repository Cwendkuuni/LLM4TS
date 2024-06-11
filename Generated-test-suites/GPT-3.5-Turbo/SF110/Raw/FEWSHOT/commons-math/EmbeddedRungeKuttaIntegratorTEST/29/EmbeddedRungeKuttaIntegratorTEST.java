package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testGetOrder() {
        double[] c = {1};
        double[][] a = {{1}};
        double[] b = {1};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl("", true, c, a, b, prototype, 1, 2, 0.1, 0.01);

        assertEquals(1, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        double[] c = {1};
        double[][] a = {{1}};
        double[] b = {1};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator impl = new EmbeddedRungeKuttaIntegratorImpl("", true, c, a, b, prototype, 1, 2, 0.1, 0.01);

        assertEquals(impl.getSafety(), 0.9, 0.01);
    }

    @Test
    public void testSetSafety() {
        double[] c = {1};
        double[][] a = {{1}};
        double[] b = {1};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator impl = new EmbeddedRungeKuttaIntegratorImpl("", true, c, a, b, prototype, 1, 2, 0.1, 0.01);
        impl.setSafety(0.8);

        assertEquals(impl.getSafety(), 0.8, 0.01);
    }

    @Test
    public void testSetSafetyWithNegativeValue() {
        double[] c = {1};
        double[][] a = {{1}};
        double[] b = {1};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator impl = new EmbeddedRungeKuttaIntegratorImpl("", true, c, a, b, prototype, 1, 2, 0.1, 0.01);
        impl.setSafety(-0.8);

        assertEquals(impl.getSafety(), -0.8, 0.01);
    }

    @Test
    public void testGetMinReduction() {
        double[] c = {1};
        double[][] a = {{1}};
        double[] b = {1};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator impl = new EmbeddedRungeKuttaIntegratorImpl("", true, c, a, b, prototype, 1, 2, 0.1, 0.01);

        assertEquals(impl.getMinReduction(), 0.2, 0.01);
    }

    @Test
    public void testSetMinReduction() {
        double[] c = {1};
        double[][] a = {{1}};
        double[] b = {1};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator impl = new EmbeddedRungeKuttaIntegratorImpl("", true, c, a, b, prototype, 1, 2, 0.1, 0.01);
        impl.setMinReduction(0.3);

        assertEquals(impl.getMinReduction(), 0.3, 0.01);
    }

    @Test
    public void testGetMaxGrowth() {
        double[] c = {1};
        double[][] a = {{1}};
        double[] b = {1};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator impl = new EmbeddedRungeKuttaIntegratorImpl("", true, c, a, b, prototype, 1, 2, 0.1, 0.01);

        assertEquals(impl.getMaxGrowth(), 10.0, 0.01);
    }

    @Test
    public void testSetMaxGrowth() {
        double[] c = {1};
        double[][] a = {{1}};
        double[] b = {1};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator impl = new EmbeddedRungeKuttaIntegratorImpl("", true, c, a, b, prototype, 1, 2, 0.1, 0.01);
        impl.setMaxGrowth(20.0);

        assertEquals(impl.getMaxGrowth(), 20.0, 0.01);
    }

    private class EmbeddedRungeKuttaIntegratorImpl extends EmbeddedRungeKuttaIntegrator {
        public EmbeddedRungeKuttaIntegratorImpl(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        }

        public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0.0;
        }

        public int getOrder() {
            return 1;
        }

        public void computeDerivatives(double t, double[] y, double[] yDot) {
        }

        public StepInterpolator prototypeCopy() {
            return null;
        }

        public double filterStep(double h, boolean forward, boolean acceptSmall) {
            return 0.0;
        }

        protected double initializeStep(boolean forward, int order, double[] scale, double t0, double[] y0, double[] yDot0, double[] y1) throws DimensionMismatchException {
            return 0.0;
        }

        protected double acceptStep(StepInterpolator interpolator, double[] y, double[] yDot, double t) {
            return 0.0;
        }
    }
}