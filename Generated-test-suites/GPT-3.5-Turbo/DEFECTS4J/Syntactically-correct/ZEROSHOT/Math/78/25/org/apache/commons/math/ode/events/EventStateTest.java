import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    private EventState eventState;

    @Before
    public void setUp() {
        EventHandler handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return 0;
            }

            @Override
            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                return 0;
            }

            @Override
            public void resetState(double t, double[] y) throws EventException {

            }
        };
        double maxCheckInterval = 0.1;
        double convergence = 1e-6;
        int maxIterationCount = 100;
        eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
    }

    @Test
    public void testEventState() {
        assertNotNull(eventState.getEventHandler());
        assertEquals(0.1, eventState.getMaxCheckInterval(), 0);
        assertEquals(1e-6, eventState.getConvergence(), 0);
        assertEquals(100, eventState.getMaxIterationCount(), 0);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        eventState.reinitializeBegin(1.0, new double[] {1.0});
        assertEquals(1.0, eventState.t0, 0);
        assertEquals(0.0, eventState.g0, 0);
        assertTrue(eventState.g0Positive);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public void setInterpolatedTime(double time) throws IllegalStateException {

            }

            @Override
            public double getInterpolatedTime() throws IllegalStateException {
                return 0;
            }

            @Override
            public double[] getInterpolatedState() throws IllegalStateException {
                return new double[] {0};
            }

            @Override
            public void setInterpolatedState(double[] state) throws IllegalStateException, IllegalArgumentException {

            }

            @Override
            public double getCurrentTime() {
                return 0;
            }

            @Override
            public double[] getCurrentState() {
                return new double[] {0};
            }

            @Override
            public boolean isForward() {
                return true;
            }
        };
        assertFalse(eventState.evaluateStep(interpolator));
    }

    @Test
    public void testGetEventTime() {
        assertEquals(Double.NaN, eventState.getEventTime(), 0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        eventState.stepAccepted(1.0, new double[]{1.0});
        assertEquals(1.0, eventState.t0, 0);
        assertEquals(0.0, eventState.g0, 0);
        assertTrue(eventState.g0Positive);
    }

    @Test
    public void testStop() {
        assertFalse(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        assertFalse(eventState.reset(1.0, new double[]{1.0}));
    }
}