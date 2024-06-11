import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.events.EventException;
import org.apache.commons.math.ode.sampling.StepInterpolator;

public class EventStateTest {
    
    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) { return 0.0; }
            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException { return 0; }
            public void resetState(double t, double[] y) throws EventException {}

        };
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        eventState.reinitializeBegin(0.0, new double[]{});
        assertEquals(0.0, eventState.getEventHandler().g(0.0, new double[]{}), 0.0);
    }
    
    @Test
    public void testEvaluateStep() throws DerivativeException, EventException {
        StepInterpolator interpolator = new StepInterpolator() {
            public void setInterpolatedTime(double time) throws DerivativeException {
                
            }
            
            public boolean isForward() {
                return true;
            }
            
            public void finalizeStep() throws DerivativeException {
                
            }
            
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                
            }
            
            public double[] getInterpolatedState() throws DerivativeException {
                return new double[]{};
            }
            
            public double[] getInterpolatedDerivatives() throws DerivativeException {
                return new double[]{};
            }
            
            public double getCurrentTime() {
                return 0.0;
            }
        };
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) { return 0.0; }
            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException { return 0; }
            public void resetState(double t, double[] y) throws EventException {}

        };
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        assertFalse(eventState.evaluateStep(interpolator));
    }
    
    @Test
    public void testGetEventTime() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) { return 0.0; }
            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException { return 0; }
            public void resetState(double t, double[] y) throws EventException {}

        };
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        assertEquals(Double.NaN, eventState.getEventTime(), 0.0);
    }
    
    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) { return 0.0; }
            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException { return 0; }
            public void resetState(double t, double[] y) throws EventException {}

        };
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        eventState.stepAccepted(0.0, new double[]{});
        assertEquals(0.0, eventState.getEventHandler().g(0.0, new double[]{}), 0.0);
    }
    
    @Test
    public void testStop() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) { return 0.0; }
            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException { return EventHandler.CONTINUE; }
            public void resetState(double t, double[] y) throws EventException {}

        };
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        assertFalse(eventState.stop());
    }
    
    @Test
    public void testReset() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) { return 0.0; }
            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException { return EventHandler.CONTINUE; }
            public void resetState(double t, double[] y) throws EventException {}

        };
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        assertFalse(eventState.reset(0.0, new double[]{}));
    }
}