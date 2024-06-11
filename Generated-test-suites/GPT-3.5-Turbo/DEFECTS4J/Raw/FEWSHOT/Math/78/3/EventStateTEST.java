package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.junit.Test;

public class EventStateTest {
    @Test
    public void testEventState() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) throws DerivativeException, EventException {
                return 0;
            }

            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                return CONTINUE;
            }

            public void resetState(double t, double[] y) throws EventException {
            }
        };
        EventState eventState = new EventState(handler, 1.0, 0.01, 100);
        
        // Test getEventHandler()
        assertEquals(handler, eventState.getEventHandler());
        
        // Test getMaxCheckInterval()
        assertEquals(1.0, eventState.getMaxCheckInterval(), 0.0);
        
        // Test getConvergence()
        assertEquals(0.01, eventState.getConvergence(), 0.0);
        
        // Test getMaxIterationCount()
        assertEquals(100, eventState.getMaxIterationCount());
        
        // Test reinitializeBegin()
        eventState.reinitializeBegin(0.0, new double[]{});
        
        // Test evaluateStep()
        StepInterpolator interpolator = new StepInterpolator() {
            public void storeTime(double t) throws DerivativeException {
            }

            public void setInterpolatedTime(double time) throws DerivativeException {
            }

            public double getInterpolatedTime() throws DerivativeException {
                return 0.0;
            }

            public void setInterpolatedState(double[] state) throws DerivativeException {
            }

            public double[] getInterpolatedState() throws DerivativeException {
                return new double[]{};
            }

            public double[] getInterpolatedDerivatives() throws DerivativeException {
                return new double[]{};
            }

            public double getCurrentTime() throws DerivativeException {
                return 0.0;
            }

            public void rescale(double scaleFactor) throws DerivativeException {
            }

            public void shift() throws DerivativeException {
            }
        };
        boolean triggered = eventState.evaluateStep(interpolator);
        assertFalse(triggered);
        
        // Test getEventTime()
        assertEquals(Double.NaN, eventState.getEventTime(), 0.0);
        
        // Test stepAccepted()
        eventState.stepAccepted(0.0, new double[]{});
        
        // Test stop()
        assertFalse(eventState.stop());
        
        // Test reset()
        boolean resetDerivatives = eventState.reset(0.0, new double[]{});
        assertFalse(resetDerivatives);
    }
}