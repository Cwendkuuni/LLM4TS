package org.apache.commons.math3.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.MockStepInterpolator;
import org.apache.commons.math3.ode.test.MockStepHandler;
import org.apache.commons.math3.ode.test.MockEventHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    /** The integrator to test. */
    private AbstractIntegrator integrator;

    /** The equations to integrate. */
    private FirstOrderDifferentialEquations equations;

    /** The step interpolator for testing. */
    private AbstractStepInterpolator interpolator;

    /** The step handlers for testing. */
    private List<StepHandler> stepHandlers;

    /** The event handlers for testing. */
    private List<EventHandler> eventHandlers;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }
        };

        equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2;
            }

            @Override
            public double getTime() {
                return 0;
            }

            @Override
            public double[] getPrimaryState() {
                return new double[] { 1.0 };
            }

            @Override
            public void setTime(double time) {
                // no-op
            }

            @Override
            public void setPrimaryState(double[] primaryState) {
                // no-op
            }
        };

        interpolator = new MockStepInterpolator();
        stepHandlers = new ArrayList<>();
        eventHandlers = new ArrayList<>();
        integrator.addStepHandler(new MockStepHandler());
        integrator.addEventHandler(new MockEventHandler(), 0.1, 1.0e-5, 10, null);
    }

    /**
     * Tests that the integrator initializes correctly.
     */
    @Test
    public void testInitIntegration() {
        integrator.initIntegration(0, new double[] { 1.0 }, 1.0);
        assertNotNull(integrator.getStepHandlers());
        assertNotNull(integrator.getEventHandlers());
    }

    /**
     * Tests that the integrator can integrate simple equations.
     */
    @Test
    public void testIntegration()
            throws DimensionMismatchException, NumberIsTooSmallException,
                   MaxCountExceededException, NoBracketingException {
        integrator.setEquations(equations);
        double[] y = new double[] { 1.0 };
        double result = integrator.integrate(equations, 0, y, 2.0, y);
        assertEquals(2.0, result, 1e-5);
        assertEquals(2.0 * 1.0, y[0], 1e-5);
    }

    /**
     * Tests that the integrator can handle step handlers.
     */
    @Test
    public void testStepHandlers() {
        integrator.addStepHandler(new MockStepHandler());
        interpolator.setGlobalCurrentTime(0.5);
        integrator.acceptStep(interpolator, new double[] { 1.0 }, new double[] { 2.0 }, 1.0);
        assertEquals(1.0, integrator.isLastStep());
    }

    /**
     * Tests that the integrator can handle event handlers.
     */
    @Test
    public void testEventHandlers() {
        integrator.addEventHandler(new MockEventHandler(), 0.1, 1.0e-5, 10, null);
        interpolator.setGlobalPreviousTime(0.0);
        interpolator.setGlobalCurrentTime(0.1);
        integrator.acceptStep(interpolator, new double[] { 1.0 }, new double[] { 2.0 }, 0.2);
        assertEquals(true, integrator.resetOccurred);
        integrator.resetOccurred = false;
        interpolator.setGlobalCurrentTime(0.2);
        integrator.acceptStep(interpolator, new double[] { 2.0 }, new double[] { 3.0 }, 0.4);
        assertEquals(true, integrator.isLastStep());
    }

}