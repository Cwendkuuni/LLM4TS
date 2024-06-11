package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert. assertArrayEquals;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** The integrator to test. */
    private EmbeddedRungeKuttaIntegrator integrator;

    /** The ODE to integrate. */
    private ExpandableStatefulODE ode;

    /** The step handler to test. */
    private StepHandler stepHandler;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        final double[] c = { 0.0, 0.3, 0.6, 1.0 };
        final double[][] a = { { 0.0, 0.0, 0.0, 0.0 },
                               { 0.3, 0.48, 0.0, 0.0 },
                               { 0.6, 0.3, 0.6, 0.0 },
                               { 1.0, 0.0, 0.6, 1.0 } };
        final double[] b = { 1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0, 1. };
        integrator = new EmbeddedRungeKuttaIntegrator("Test RK4", false, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += FastMath.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };
        ode = new ExpandableStatefulODE("TestODE") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = BesselFunctions.besselJ(0, 2 * Math.PI * t) * Math.sin(t);
            }
        };
        ode.setTime(0.0);
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(double t, double[] y) {
                // empty
            }
        };
    }

    /**
     * Tests the integrator with a simple ODE.
     *
     * @throws MathIllegalArgumentException if the ODE is not integrable
     * @throws IllegalArgumentException if the integrator parameters are invalid
     */
    @Test
    public void testIntegration() throws MathIllegalArgumentException {
        final double tolerance = 1e-8;
        integrator.setMinStep(0.0001);
        integrator.setMaxStep(0.01);
        final double[] y0 = new double[] { 0.0 };
        final double t = 1.0;
        integrator.integrate(ode, t);
        final double[] y = ode.getCompleteState();
        assertArrayEquals(y, new double[] { Math.sin(t) }, tolerance);
        assertEquals(t, ode.getTime(), tolerance);
    }

    /**
     * Tests the integrator with a simple ODE and a step handler.
     *
     * @throws MathIllegalArgumentException if the ODE is not integrable
     * @throws IllegalArgumentException if the integrator parameters are invalid
     */
    @Test
    public void testIntegrationWithStepHandler() throws MathIllegalArgumentException {
        integrator.setMinStep(0.0001);
        integrator.setMaxStep(0.01);
        final double[] y0 = new double[] { 0.0 };
        final double t = 1.0;
        integrator.addStepHandler(stepHandler);
        integrator.integrate(ode, t);
        final double[] y = ode.getCompleteState();
        assertArrayEquals(y, new double[] { Math.sin(t) }, ODETestUtils.TOLERANCE);
        assertEquals(t, ode.getTime(), ODETestUtils.TOLERANCE);
        stepHandler.fireStep();
    }

    /**
     * Tests an illegal argument exception is thrown when the minimal step is set
     * to a negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMinStep() {
        integrator.setMinStep(-0.001);
    }

    /**
     * Tests an illegal argument exception is thrown when the maximal step is set
     * to a negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxStep() {
        integrator.setMaxStep(-0.01);
    }

}