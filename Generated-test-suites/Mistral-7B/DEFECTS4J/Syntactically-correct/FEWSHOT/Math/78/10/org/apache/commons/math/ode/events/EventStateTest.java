package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.special.Easymath;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testEventState() {
        final double convergence = 1e-6;
        final double maxIterationCount = 20;
        final EventHandler handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return Math.sin(t) * Math.sin(t) - 1.0;
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };

        EventState eventState = new EventState(handler, 0.001, convergence, maxIterationCount);

        assertTrue(Double.isNaN(eventState.getEventHandler().g(0.0, new double[0])));
        assertTrue(eventState.stop());

        EventState stateWithInitialValues = new EventState(handler, 0.001, convergence, maxIterationCount);
        stateWithInitialValues.reinitializeBegin(0.0, new double[]{Math.PI / 4.0, Math.PI / 4.0});
        assertFalse(stateWithInitialValues.stop());
        assertEquals(Math.sin(0.0) * Math.sin(0.0), stateWithInitialValues.g0, 1e-6);

        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedState(new double[]{Math.PI / 4.0, Math.PI / 4.0});
        interpolator.setInterpolatedTime(0.001);

        try {
            stateWithInitialValues.evaluateStep(interpolator);
            fail("Expected DerivativeException or EventException");
        } catch (DerivativeException e) {
            // expected, as the method uses the StepInterpolator to interpolate the function
        } catch (EventException e) {
            assertFalse(stateWithInitialValues.stop());
            assertEquals(Math.sin(0.001) * Math.sin(0.001), stateWithInitialValues.g0, 1e-6);
        }

        interpolator.setInterpolatedTime(0.01);
        try {
            stateWithInitialValues.evaluateStep(interpolator);
            fail("Expected EventException");
        } catch (EventException e) {
            assertTrue(stateWithInitialValues.pendingEvent);
            assertEquals(Math.sin(0.01) * Math.sin(0.01), stateWithInitialValues.g0, 1e-6);
            assertEquals(Math.sin(0.01), stateWithInitialValues.g0Positive ? 1.0 : -1.0, 1e-6);
            assertEquals(0.01, stateWithInitialValues.pendingEventTime, 1e-6);
            assertEquals(0.0, stateWithInitialValues.previousEventTime);
        }

        stateWithInitialValues.stepAccepted(0.01, new double[]{Math.PI / 4.0 + Easymath.PI / 4.0, Math.PI / 4.0 + Easymath.PI / 4.0});
        assertFalse(stateWithInitialValues.pendingEvent);
        assertTrue(stateWithInitialValues.stop());

        stateWithInitialValues.reset(0.02, new double[]{0.0, 0.0});
        assertFalse(stateWithInitialValues.pendingEvent);
        assertFalse(stateWithInitialValues.stop());
    }
}