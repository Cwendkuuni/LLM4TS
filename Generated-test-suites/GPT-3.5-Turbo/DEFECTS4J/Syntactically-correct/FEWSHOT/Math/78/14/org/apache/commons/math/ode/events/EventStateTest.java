package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {
    private EventHandler handler;
    private double maxCheckInterval;
    private double convergence;
    private int maxIterationCount;
    private EventState eventState;

    @Before
    public void setUp() {
        handler = new EventHandler() {
            public double g(double t, double[] y) throws EventException {
                return y[0] - 1.0;
            }

            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                return 0;
            }

            public void resetState(double t, double[] y) throws EventException {

            }
        };
        maxCheckInterval = 0.001;
        convergence = 0.001;
        maxIterationCount = 100;
        eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
    }

    @Test
    public void testGetEventHandler() {
        assertEquals(handler, eventState.getEventHandler());
    }

    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 0.0);
    }

    @Test
    public void testGetConvergence() {
        assertEquals(convergence, eventState.getConvergence(), 0.0);
    }

    @Test
    public void testGetMaxIterationCount() {
        assertEquals(maxIterationCount, eventState.getMaxIterationCount());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0.0;
        double[] yStart = {1.0};
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(yStart[0] - 1.0, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());

        double newTStart = 1.0;
        double[] newYStart = {1.0};
        eventState.reinitializeBegin(newTStart, newYStart);
        assertEquals(newTStart, eventState.getT0(), 0.0);
        assertEquals(newYStart[0] - 1.0, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = new StepInterpolator() {
            public double getCurrentTime() {
                return 0.0;
            }

            public void setInterpolatedTime(double time) {
            }

            public boolean isForward() {
                return true;
            }

            public double getPreviousTime() {
                return 0;
            }

            public double getInterpolatedTime() {
                return 0;
            }

            public void interpolate(double time, double[] state) throws DerivativeException {
            }

            public StepInterpolator copy() throws DerivativeException {
                return null;
            }

            public void writeExternal(ObjectOutput out) throws IOException {
            }

            public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            }
        };

        boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);
    }
    
    @Test
    public void testGetEventTime() {
        double expected = 0.0;
        assertEquals(expected, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 0.0;
        double[] y = {1.0};
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(y[0] - 1.0, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    @Test
    public void testStop() {
        assertFalse(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 0.0;
        double[] y = {1.0};
        assertFalse(eventState.reset(t, y));
    }
}