import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventState eventState;

    @Before
    public void setUp() {
        EventHandler handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) throws DerivativeException, EventException {
                return y[0] - 5;
            }

            @Override
            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                return EventHandler.STOP;
            }

            @Override
            public void resetState(double t, double[] y) throws EventException {
                for (int i = 0; i < y.length; i++) {
                    y[i] = 0;
                }
            }
        };

        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 100;

        eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
    }

    @After
    public void tearDown() {
        eventState = null;
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        double[] y = new double[1];
        y[0] = 10;

        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public void setInterpolatedTime(double time) throws IllegalArgumentException {
            }

            @Override
            public double getPreviousTime() {
                return 0;
            }

            @Override
            public double getCurrentTime() {
                return 1;
            }

            @Override
            public double[] getInterpolatedState() throws IllegalStateException {
                return new double[]{10};
            }

            @Override
            public double[] getInterpolatedDerivatives() throws IllegalStateException {
                return new double[]{0};
            }

            @Override
            public double[] getInterpolatedSecondaryState(int index) throws IllegalStateException {
                return new double[0];
            }

            @Override
            public double[] getInterpolatedSecondaryDerivatives(int index) throws IllegalStateException {
                return new double[0];
            }

            @Override
            public double getCurrentStateValue() throws IllegalStateException {
                return 10;
            }

            @Override
            public double getGlobalPreviousTime() {
                return 0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 1;
            }

            @Override
            public double[] getGlobalCurrentState() throws IllegalStateException {
                return new double[]{10};
            }

            @Override
            public double[] getGlobalCurrentDerivatives() throws IllegalStateException {
                return new double[]{0};
            }

            @Override
            public double[] getGlobalCurrentSecondaryState(int index) throws IllegalStateException {
                return new double[0];
            }

            @Override
            public double[] getGlobalCurrentSecondaryDerivatives(int index) throws IllegalStateException {
                return new double[0];
            }
        };

        boolean result = eventState.evaluateStep(interpolator);

        Assert.assertTrue(result);
        Assert.assertEquals(0, eventState.getEventTime(), 0.0001);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double[] y = new double[1];
        y[0] = 10;

        eventState.stepAccepted(1, y);

        Assert.assertEquals(1, eventState.getEventHandler().g(1, y), 0.0001);
    }

    @Test
    public void testStop() {
        Assert.assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double[] y = new double[1];
        y[0] = 10;

        Assert.assertFalse(eventState.reset(1, y));
    }
}