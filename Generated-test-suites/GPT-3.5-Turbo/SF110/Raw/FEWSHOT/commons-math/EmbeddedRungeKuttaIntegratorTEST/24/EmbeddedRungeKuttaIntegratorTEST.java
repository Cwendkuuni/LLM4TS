package org.apache.commons.math3.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    // Create a mock class inheriting from EmbeddedRungeKuttaIntegrator
    private class MockEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

        public MockEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b,
                RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance,
                double scalRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        }

        @Override
        public int getOrder() {
            return 0;
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0;
        }

        @Override
        protected StepInterpolator prototypeCopy() {
            return null;
        }

        @Override
        protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH, double thetaH,
                double[] y0, double[] y1, double[] yDot0, double[] yDot1, double[] yMidDots) {
        }
    }

    private MockEmbeddedRungeKuttaIntegrator integrator;

    private double c[];
    private double a[][];
    private double b[];
    private RungeKuttaStepInterpolator prototype;
    private double minStep;
    private double maxStep;
    private double scalAbsoluteTolerance;
    private double scalRelativeTolerance;

    @Before
    public void setUp() {
        c = new double[] { 0.0, 1.0 / 2.0, 1.0 };
        a = new double[][] { { 0.0, 0.0, 0.0 }, { 1.0 / 2.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 } };
        b = new double[] { 1.0 / 6.0, 2.0 / 3.0, 1.0 / 6.0 };
        prototype = null;
        minStep = 0.0;
        maxStep = 0.0;
        scalAbsoluteTolerance = 0.0;
        scalRelativeTolerance = 0.0;

        integrator = new MockEmbeddedRungeKuttaIntegrator("", true, c, a, b, prototype, minStep, maxStep,
                scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Test
    public void testGetOrder() {
        assertEquals(0, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.5);
        assertEquals(0.5, integrator.getSafety(), 0.0);
    }

    @Test
    public void testIntegrate() {
        ExpandableStatefulODE equations = null;
        double t = 0.0;

        try {
            integrator.integrate(equations, t);
            fail("Integrate method should throw an exception");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.0);
    }
}