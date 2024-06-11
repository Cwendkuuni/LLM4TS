package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class EventStateTest {

    private EventState eventState;
    private EventHandler handler;

    @Parameterized.Parameters(name = "{0} Integration direction, {1} Event position")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {true, 1.0},
                {true, 0.5},
                {true, -0.5},
                {false, -1.0},
                {false, -0.5},
                {false, 0.5},
                {false, 1.0}
        });
    }

    @Parameterized.Constructor
    public EventStateTest(boolean forward, double pendingEventPosition) {
        handler = new SimpleEventHandler();
        eventState = new EventState(handler, 0.1, 0.01, 10);
        eventState.reinitializeBegin(0.0, new double[]{0.0});
    }

    @Before
    public void setUp() {
        handler.setPendingEventPosition(pendingEventPosition);
    }

    /**
     * Test that an event is detected when it occurs within the step.
     * @throws Exception if test fails
     */
    @Test
    public void testEventDetected() throws Exception {
        StepInterpolator stepInterpolator = new StepInterpolator(new StepHandlerAdapter() {
            @Override
            public void evaluateDerivative(double t, double[] yPrime, double[] y) throws DerivativeException {
                // dummy derivative
            }

            @Override
            public double interpolatedValue(int index) {
                return y[index];
            }
        }, 0.01, eventState);

        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertTrue(eventState.pendingEvent);
        assertEquals(pendingEventPosition, eventState.getEventTime(), 0.0);
    }

    /**
     * Test that an event is not detected when it is outside the step.
     * @throws Exception if test fails
     */
    @Test
    public void testEventNotDetected() throws Exception {
        StepInterpolator stepInterpolator = new StepInterpolator(new StepHandlerAdapter() {
            @Override
            public void evaluateDerivative(double t, double[] yPrime, double[] y) throws DerivativeException {
                // dummy derivative
            }

            @Override
            public double interpolatedValue(int index) {
                return y[index];
            }
        }, 0.01, eventState);

        stepInterpolator.setInterpolatedTime(pendingEventPosition + 0.01);
        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertFalse(eventState.pendingEvent);
    }

    /**
     * Test that an event is localized correctly.
     * @throws Exception if test fails
     */
    @Test
    public void testEventLocalized() throws Exception {
        StepInterpolator stepInterpolator = new StepInterpolator(new StepHandlerAdapter() {
            @Override
            public void evaluateDerivative(double t, double[] yPrime, double[] y) throws DerivativeException {
                // dummy derivative
            }

            @Override
            public double interpolatedValue(int index) {
                return y[index];
            }
        }, 0.01, eventState);

        eventState.evaluateStep(stepInterpolator);
        assertTrue(eventState.pendingEvent);
        double root = eventState.getEventTime();
        double tPrev = eventState.previousEventTime;
        assertTrue(root > tPrev);

        eventState.reset(root, new double[]{0.0});
        stepInterpolator.setInterpolatedTime(root - 0.01);
        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertFalse(eventState.pendingEvent);
        assertEquals(root, eventState.previousEventTime, 0.0);
    }

    /**
     * Test that the integration direction is properly taken into account
     * when localizing events.
     * @throws Exception if test fails
     */
    @Test
    public void testEventLocalizedWithOppositeDirection() throws Exception {
        eventState.forward = false;
        StepInterpolator stepInterpolator = new StepInterpolator(new StepHandlerAdapter() {
            @Override
            public void evaluateDerivative(double t, double[] yPrime, double[] y) throws DerivativeException {
                // dummy derivative
            }

            @Override
            public double interpolatedValue(int index) {
                return y[index];
            }
        }, 0.01, eventState);

        eventState.evaluateStep(stepInterpolator);
        assertTrue(eventState.pendingEvent);
        double root = eventState.getEventTime();
        double tPrev = eventState.previousEventTime;
        assertTrue(root < tPrev);

        eventState.reset(root, new double[]{0.0});
        stepInterpolator.setInterpolatedTime(root + 0.01);
        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertFalse(eventState.pendingEvent);
        assertEquals(root, eventState.previousEventTime, 0.0);
    }

    /**
     * Test that the state is properly reseted when an event occurs.
     * @throws Exception if test fails
     */
    @Test
    public void testStateReseted() throws Exception {
        StepInterpolator stepInterpolator = new StepInterpolator(new StepHandlerAdapter() {
            @Override
            public void evaluateDerivative(double t, double[] yPrime, double[] y) throws DerivativeException {
                // dummy derivative
            }

            @Override
            public double interpolatedValue(int index) {
                return y[index];
            }
        }, 0.01, eventState);

        eventState.evaluateStep(stepInterpolator);
        assertTrue(eventState.pendingEvent);
        assertEquals(pendingEventPosition, eventState.getEventTime(), 0.0);

        eventState.stepAccepted(pendingEventPosition + 0.1, new double[]{0.0});
        assertFalse(eventState.pendingEvent);
        assertFalse(Double.isNaN(eventState.pendingEventTime));
        assertEquals(pendingEventPosition + 0.1, eventState.getEventTime(), 0.0);

        eventState.reset(pendingEventPosition + 0.1, new double[]{0.0});
        assertFalse(eventState.pendingEvent);
        assertFalse(Double.isNaN(eventState.pendingEventTime));
        assertEquals(pendingEventPosition + 0.1, eventState.previousEventTime, 0.0);

        stepInterpolator.setInterpolatedTime(pendingEventPosition + 0.05);
        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertFalse(eventState.pendingEvent);
        assertEquals(pendingEventPosition + 0.1, eventState.previousEventTime, 0.0);
    }

    /**
     * Test that the integrator stops when an event occurs.
     * @throws Exception if test fails
     */
    @Test
    public void testIntegratorStops() throws Exception {
        handler.setStopIntegratorOnEvent(true);

        StepInterpolator stepInterpolator = new StepInterpolator(new StepHandlerAdapter() {
            @Override
            public void evaluateDerivative(double t, double[] yPrime, double[] y) throws DerivativeException {
                // dummy derivative
            }

            @Override
            public double interpolatedValue(int index) {
                return y[index];
            }
        }, 0.01, eventState);

        eventState.evaluateStep(stepInterpolator);
        assertTrue(eventState.pendingEvent);
        assertEquals(pendingEventPosition, eventState.getEventTime(), 0.0);

        eventState.stepAccepted(pendingEventPosition + 0.1, new double[]{0.0});
        assertFalse(eventState.pendingEvent);
        assertFalse(eventState.stop());

        eventState.reset(pendingEventPosition + 0.1, new double[]{0.0});
        stepInterpolator.setInterpolatedTime(pendingEventPosition + 0.05);
        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertTrue(eventState.stop());
    }

    /**
     * Test that the integrator does not stop when no event occurs.
     * @throws Exception if test fails
     */
    @Test
    public void testIntegratorDoesNotStop() throws Exception {
        handler.setStopIntegratorOnEvent(false);

        StepInterpolator stepInterpolator = new StepInterpolator(new StepHandlerAdapter() {
            @Override
            public void evaluateDerivative(double t, double[] yPrime, double[] y) throws DerivativeException {
                // dummy derivative
            }

            @Override
            public double interpolatedValue(int index) {
                return y[index];
            }
        }, 0.01, eventState);

        eventState.evaluateStep(stepInterpolator);
        assertFalse(eventState.pendingEvent);
        assertFalse(eventState.stop());

        eventState.reset(0.0, new double[]{0.0});
        stepInterpolator.setInterpolatedTime(0.01);
        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertFalse(eventState.stop());
    }

    /**
     * Test that the integrator resets the derivatives when an event occurs.
     * @throws Exception if test fails
     */
    @Test
    public void testDerivativesReseted() throws Exception {
        handler.setResetDerivativesOnEvent(true);

        StepInterpolator stepInterpolator = new StepInterpolator(new StepHandlerAdapter() {
            @Override
            public void evaluateDerivative(double t, double[] yPrime, double[] y) throws DerivativeException {
                // dummy derivative
            }

            @Override
            public double interpolatedValue(int index) {
                return y[index];
            }
        }, 0.01, eventState);

        eventState.evaluateStep(stepInterpolator);
        assertTrue(eventState.pendingEvent);
        assertEquals(pendingEventPosition, eventState.getEventTime(), 0.0);

        eventState.stepAccepted(pendingEventPosition + 0.1, new double[]{0.0, 0.0});
        assertFalse(eventState.pendingEvent);
        assertFalse(Double.isNaN(eventState.pendingEventTime));
        assertEquals(pendingEventPosition + 0.1, eventState.getEventTime(), 0.0);
        assertNotNull(eventState.getEventHandler().getDerivatives());

        eventState.reset(pendingEventPosition + 0.1, new double[]{0.0, 0.0});
        stepInterpolator.setInterpolatedTime(pendingEventPosition + 0.05);
        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertFalse(eventState.pendingEvent);
        assertFalse(eventState.stop());
        assertNotNull(eventState.getEventHandler().getDerivatives());

        eventState.getEventHandler().setDerivatives(new double[]{1.0, 1.0});
        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertFalse(eventState.pendingEvent);
        assertFalse(eventState.stop());
        assertNull(eventState.getEventHandler().getDerivatives());
    }

    /**
     * Test that the integrator does not reset the derivatives when no event occurs.
     * @throws Exception if test fails
     */
    @Test
    public void testDerivativesNotReseted() throws Exception {
        handler.setResetDerivativesOnEvent(false);

        StepInterpolator stepInterpolator = new StepInterpolator(new StepHandlerAdapter() {
            @Override
            public void evaluateDerivative(double t, double[] yPrime, double[] y) throws DerivativeException {
                // dummy derivative
            }

            @Override
            public double interpolatedValue(int index) {
                return y[index];
            }
        }, 0.01, eventState);

        eventState.evaluateStep(stepInterpolator);
        assertFalse(eventState.pendingEvent);
        assertFalse(eventState.stop());

        eventState.reset(0.0, new double[]{0.0, 0.0});
        stepInterpolator.setInterpolatedTime(0.01);
        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertFalse(eventState.pendingEvent);
        assertFalse(eventState.stop());
        assertNotNull(eventState.getEventHandler().getDerivatives());

        eventState.getEventHandler().setDerivatives(new double[]{1.0, 1.0});
        assertFalse(eventState.evaluateStep(stepInterpolator));
        assertFalse(eventState.pendingEvent);
        assertFalse(eventState.stop());
        assertSame(eventState.getEventHandler().getDerivatives(),
                   eventState.getEventHandler().getDerivatives());
    }

}