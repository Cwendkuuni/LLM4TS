import org.apache.commons.math3.exception.DerivativeException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.ode.EventException;
import org.apache.commons.math3.ode.StepInterpolator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testEventState() {
        EventHandler handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) throws EventException {
                return Math.sin(BesselFunctions.I0(t));
            }

            @Override
            public void resetState(double t, double[] y) throws EventException {
                // do nothing
            }

            @Override
            public boolean eventOccurred(double t, double[] y, boolean crossing) throws EventException {
                throw new EventException("Test event");
            }
        };

        double maxCheckInterval = 0.001;
        double convergence = 1e-6;
        int maxIterationCount = 10;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        StepInterpolator interpolator = new StepInterpolator(new RK4StepInterpolator(), 0.0);

        double[] y0 = new double[]{0.0, 1.0};

        try {
            eventState.reinitializeBegin(0.0, y0);

            // step 1: event is not triggered
            interpolator.setInitialValues(y0);
            interpolator.setStepSize(0.1);
            eventState.evaluateStep(interpolator);
            double t1 = interpolator.getCurrentTime();
            double[] y1 = interpolator.getInterpolatedState();
            boolean stepAccepted = eventState.stepAccepted(t1, y1);
            assertEquals(0.0, eventState.getEventTime(), 1e-6);
            assertFalse(stepAccepted);

            // step 2: event is triggered
            interpolator.setInitialValues(y1);
            interpolator.setStepSize(0.2);
            eventState.evaluateStep(interpolator);
            double t2 = interpolator.getCurrentTime();
            double[] y2 = interpolator.getInterpolatedState();
            stepAccepted = eventState.stepAccepted(t2, y2);
            assertEquals(t2, eventState.getEventTime(), 1e-6);
            assertTrue(stepAccepted);

            // step 3: event is not triggered anymore
            interpolator.setInitialValues(y2);
            interpolator.setStepSize(0.1);
            eventState.evaluateStep(interpolator);
            double t3 = interpolator.getCurrentTime();
            double[] y3 = interpolator.getInterpolatedState();
            stepAccepted = eventState.stepAccepted(t3, y3);
            assertEquals(0.0, eventState.getEventTime(), 1e-6);
            assertFalse(stepAccepted);

            // test event handling
            try {
                eventState.stepAccepted(t3 + 1e-6, y3);
                fail();
            } catch (EventException e) {
                // expected
            }

            try {
                eventState.reset(t3 + 1e-6, y3);
                fail();
            } catch (EventException e) {
                // expected
            }

            eventState.reset(t3, y3);
            assertEquals(0.0, eventState.getEventTime(), 1e-6);
            assertFalse(eventState.stop());

        } catch (DerivativeException | ConvergenceException e) {
            fail(e);
        }

    }

}