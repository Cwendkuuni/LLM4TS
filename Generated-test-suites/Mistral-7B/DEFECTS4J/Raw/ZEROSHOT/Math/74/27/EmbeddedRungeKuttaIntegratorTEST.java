package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKutta5_4Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.DummyDifferentialEquations;
import org.apache.commons.math3.ode.test.DummyStepHandler;
import org.apache.commons.math3.ode.test.MockEventsManager;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator} class.
 *
 * @author Iulian Bogdanov
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** Integrator to test. */
    private EmbeddedRungeKuttaIntegrator integrator;

    /** Differential equations to integrate. */
    private FirstOrderDifferentialEquations equations;

    /** Step handler for testing. */
    private StepHandler stepHandler;

    /** Events manager for testing. */
    private MockEventsManager eventsManager;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        equations = new DummyDifferentialEquations();
        integrator = new RungeKutta5_4Integrator(1.0, false, null, null, null, null,
                0.001, 0.01, null, null);
        stepHandler = new DummyStepHandler();
        eventsManager = new MockEventsManager();
    }

    /**
     * Test integration with no events.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIntegrationNoEvents() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = { 1.0, 2.0 };
        final double tEnd = 1.0;
        final double[] yEnd = { 3.0, 4.0 };

        integrator.reset();
        integrator.setStepHandler(stepHandler);
        integrator.setEventsManager(eventsManager);

        final double result = integrator.integrate(equations, t0, y0, tEnd, new double[yEnd.length]);
        assertEquals(tEnd, result, 1e-14);
        assertTrue(ODETestUtils.isClose(yEnd, integrator.getY(), 1e-14));

        assertEquals(0, integrator.getEventIndex());
        assertFalse(eventsManager.isEventTriggered());

        final double[] yDot = integrator.getYDot();
        assertTrue(ODETestUtils.isClose(yEnd, yDot, 1e-14));
    }

    /**
     * Test integration with events.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIntegrationWithEvents() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = { 1.0, 2.0 };
        final double tEnd = 1.0;
        final double[] yEnd = { 3.0, 4.0 };

        equations = new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        integrator.reset();
        integrator.setStepHandler(stepHandler);
        integrator.setEventsManager(eventsManager);

        final When event = new When(0.5, new Event() {
            @Override
            public boolean isSatisfied(double t, double[] y) {
                return false;
            }

            @Override
            public int getId() {
                return 1;
            }
        });
        eventsManager.addEvent(event);

        final double result = integrator.integrate(equations, t0, y0, tEnd, new double[yEnd.length]);
        assertEquals(tEnd, result, 1e-14);
        assertTrue(ODETestUtils.isClose(yEnd, integrator.getY(), 1e-14));

        assertEquals(1, integrator.getEventIndex());
        assertTrue(eventsManager.isEventTriggered());

        final double[] yDot = integrator.getYDot();
        assertTrue(ODETestUtils.isClose(yEnd, yDot, 1e-14));

        integrator.reinit();

        result = integrator.integrate(equations, t0, y0, tEnd, new double[yEnd.length]);
        assertEquals(tEnd, result, 1e-14);
        assertTrue(ODETestUtils.isClose(yEnd, integrator.getY(), 1e-14));

        assertEquals(0, integrator.getEventIndex());
        assertFalse(eventsManager.isEventTriggered());

        yDot = integrator.getYDot();
        assertTrue(ODETestUtils.isClose(yEnd, yDot, 1e-14));
    }

    /**
     * Test integration with negative step size.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() throws Exception {
        final double t0 = 0.0;
        final double[] y0 = { 1.0, 2.0 };
        final double tEnd = 1.0;
        final double[] yEnd = { 3.0, 4.0 };

        integrator.reset();
        integrator.setStepHandler(stepHandler);
        integrator.setEventsManager(eventsManager);

        integrator.integrate(equations, t0, y0, tEnd, new double[yEnd.length], -1.0);
    }

}

class DummyStepHandler implements StepHandler {
    private boolean called = false;

    @Override
    public void handleStep(StepInterpolator interpolator, int stepNumber) {
        called = true;
    }

    public boolean isCalled() {
        return called;
    }

    public void reset() {
        called = false;
    }
}

class MockEventsManager extends CombinedEventsManager {
    private boolean eventTriggered = false;

    public void stepAccepted(double t, double[] y) {
        eventTriggered = true;
    }

    public boolean isEventTriggered() {
        final boolean result = eventTriggered;
        eventTriggered = false;
        return result;
    }

    public void reset(double t, double[] y) {
        resetEventIndex();
        eventTriggered = false;
    }
}