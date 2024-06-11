import org.apache.commons.math.ode.events.EventException;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 100;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        double tStart = 0.0;
        double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);

        verify(handler).g(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws EventException {
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventHandler handler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 100;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.0);
        when(interpolator.getInterpolatedState()).thenReturn(new double[1]);
        when(handler.g(anyDouble(), any(double[].class))).thenReturn(0.0, 1.0);

        boolean result = eventState.evaluateStep(interpolator);

        verify(interpolator).isForward();
        verify(interpolator, times(2)).getInterpolatedState();
        verify(handler, times(2)).g(anyDouble(), any(double[].class));
        verify(interpolator).setInterpolatedTime(0.9909999);
        verify(interpolator).setInterpolatedTime(1.0);
        verify(handler).g(0.9909999, new double[1]);
        verify(handler).g(1.0, new double[1]);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 100;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        double t = 1.0;
        double[] y = new double[1];
        eventState.stepAccepted(t, y);

        verify(handler).g(t, y);
        verify(handler).eventOccurred(t, y, false);
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handler = mock(EventHandler.class);
        double maxCheckInterval = 0.01;
        double convergence = 0.001;
        int maxIterationCount = 100;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        double t = 1.0;
        double[] y = new double[1];
        eventState.reset(t, y);

        verify(handler).resetState(t, y);
    }

}