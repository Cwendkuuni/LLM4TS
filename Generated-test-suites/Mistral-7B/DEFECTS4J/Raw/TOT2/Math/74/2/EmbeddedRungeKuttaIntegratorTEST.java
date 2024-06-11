package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueHolder;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.events.EventType;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** Function for testing the integrator. */
    private static class TestFunction implements FirstOrderDifferentialEquations {

        /** The constant value for y[0]. */
        private static final double CONSTANT_Y0 = 1.0;

        /** The constant value for y[1]. */
        private static final double CONSTANT_Y1 = 0.0;

        /**
         * Computes the derivatives.
         *
         * @param t the current time
         * @param y the current state
         * @param yDot the derivatives
         */
        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = 2.0 * y[1];
            yDot[1] = -y[0];
        }

        /**
         * Returns the number of state variables.
         *
         * @return the number of state variables
         */
        @Override
        public int getDimension() {
            return 2;
        }

    }

    /** The integrator. */
    private EmbeddedRungeKuttaIntegrator integrator;

    /** The step interpolator. */
    private StepInterpolator interpolator;

    /** The input function. */
    private TestFunction inputFunction;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        inputFunction = new TestFunction();
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                                                     new double[] { 0.25, 0.25, 0.25, 0.25 },
                                                     new double[][] { { 0.25, 0 },
                                                                       { 0, 0.25 },
                                                                       { 0.25, 0.25 },
                                                                       { 0.25, 0.25 } },
                                                     new double[] { 0.25, 0.25, 0.25, 0.25 },
                                                     new RungeKuttaStepInterpolator(),
                                                     0.001, 0.01,
                                                     new double[] { 1.0e-10, 1.0e-10 },
                                                     new double[] { 1.0e-10, 1.0e-10 }) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    error += Math.pow(y1[i] - y0[i], 2);
                }
                return Math.sqrt(error);
            }
        };
        interpolator = integrator.getInterpolator();
    }

    /**
     * Tests the basic functionality of the integrator.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testBasicFunctionality() throws Exception {
        final double start = 0.0;
        final double end = 1.0;
        double[] y0 = new double[] { CONSTANT_Y0, CONSTANT_Y1 };
        double[] y = new double[y0.length];
        double step = integrator.integrate(inputFunction, start, y0, end, y);
        assertEquals(end, step, 1.0e-9);
        assertEquals(y0[0], y[0], 1.0e-9);
        assertEquals(y0[1], y[1], 1.0e-9);
    }

    /**
     * Tests an edge case where the derivatives are not defined for a
     * certain time.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testEdgeCase() throws Exception {
        final double start = 0.0;
        final double end = 1.0;
        final double eventTime = 1.0;

        // Set up an event that will be triggered at the end time
        DefaultValueHolder holder = new DefaultValueHolder();
        final Event event = new Event(EventType.REACHED_TIME, eventTime, null, null, null, null, holder);
        integrator.addEvent(event);

        double[] y0 = new double[] { CONSTANT_Y0, CONSTANT_Y1 };
        double[] y = new double[y0.length];

        // Set up a step handler that does nothing
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                // Do nothing
            }
        };
        integrator.addStepHandler(stepHandler);

        try {
            integrator.integrate(inputFunction, start, y0, end, y);
            fail("Expected an IntegratorException");
        } catch (IntegratorException e) {
            // Expected
        }
        assertEquals(start, interpolator.getTime(), 1.0e-9);

        // The step handler was not called
        assertEquals(0, stepHandler.getCallCount());

        // The event was triggered
        assertEquals(eventTime, holder.getValue(), 1.0e-9);
    }

    /**
     * Tests an error scenario where the derivatives do not bracket the
     * initial and final states.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testErrorScenario() throws Exception {
        final double start = 0.0;
        final double end = 1.0;
        double[] y0 = new double[] { CONSTANT_Y0, CONSTANT_Y1 };
        double[] y = new double[y0.length];

        final double[] yDot = new double[y0.length];
        yDot[0] = 0.1;
        yDot[1] = 0.1;

        final double h = 0.1;
        integrator.resetEvaluations();
        integrator.computeDerivatives(start, y0, yDot);

        final double error = integrator.estimateError(yDot, y0, y, h);
        assertEquals(Math.sqrt(2.0), error, 1.0e-6);

        try {
            integrator.integrate(inputFunction, start, y0, end, y);
            fail("Expected a MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        try {
            integrator.integrate(inputFunction, start, y0, end, y);
            fail("Expected a NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        // The error estimation failed, so the integrator should have
        // tried to reduce the stepsize
        assertEquals(h / 2.0, integrator.getStepSize(), 1.0e-9);
    }

}