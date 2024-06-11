import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class EventStateTest {

    private EventState eventState;
    private EventHandler mockEventHandler;

    @Before
    public void setUp() {
        mockEventHandler = new MockEventHandler();
        eventState = new EventState(mockEventHandler, 0.01, 0.001, 10);
    }

    @Test
    public void testEventStateInitialization() {
        Assert.assertNotNull(eventState);
        Assert.assertNull(eventState.getEventHandler());
        Assert.assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        Assert.assertEquals(0.001, eventState.getConvergence(), 0.0);
        Assert.assertEquals(10, eventState.getMaxIterationCount());
        Assert.assertNull(eventState.getT0());
        Assert.assertNull(eventState.getG0());
        Assert.assertTrue(eventState.isG0Positive());
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertNull(eventState.getPendingEventTime());
        Assert.assertNull(eventState.getPreviousEventTime());
        Assert.assertTrue(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEventStateNegativeMaxCheckInterval() {
        new EventState(mockEventHandler, -0.01, 0.001, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEventStateNegativeConvergence() {
        new EventState(mockEventHandler, 0.01, -0.001, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEventStateNegativeMaxIterationCount() {
        new EventState(mockEventHandler, 0.01, 0.001, -10);
    }

    @Test
    public void testEventStateReinitializeBegin() throws EventException {
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0, 3.0};
        eventState.reinitializeBegin(tStart, yStart);
        Assert.assertEquals(tStart, eventState.getT0(), 0.0);
        Assert.assertEquals(mockEventHandler.g(tStart, yStart), eventState.getG0(), 0.0);
    }

    @Test(expected = EventException.class)
    public void testEventStateReinitializeBeginWithException() throws EventException {
        mockEventHandler.setThrowExceptionOnGEvaluation = true;
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0, 3.0};
        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        double t1 = 2.0;
        StepInterpolator stepInterpolator = new StepInterpolator();
        eventState.evaluateStep(stepInterpolator);
        Assert.assertFalse(eventState.isPendingEvent());

        double t1WithEvent = 2.001;
        mockEventHandler.setGValueForTime = (t, y) -> Math.sin(t);
        stepInterpolator.setInterpolatedState(yStart);
        eventState.evaluateStep(stepInterpolator);
        Assert.assertTrue(eventState.isPendingEvent());
        Assert.assertEquals(t1WithEvent, eventState.getPendingEventTime(), 0.0);

        double t1WithEventAndCrossed = 2.0005;
        mockEventHandler.setGValueForTime = (t, y) -> Math.sin(t) * 2;
        stepInterpolator.setInterpolatedState(yStart);
        eventState.evaluateStep(stepInterpolator);
        Assert.assertTrue(eventState.isPendingEvent());
        Assert.assertEquals(t1WithEventAndCrossed, eventState.getPendingEventTime(), 0.0);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        double t1 = 2.0;
        StepInterpolator stepInterpolator = new StepInterpolator();
        stepInterpolator.setForward(false);
        eventState.evaluateStep(stepInterpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        double t1 = 2.0;
        StepInterpolator stepInterpolator = new StepInterpolator();
        stepInterpolator.setForward(true);
        mockEventHandler.setThrowEventExceptionOnGEvaluation = true;
        eventState.evaluateStep(stepInterpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        double t1 = 2.0;
        StepInterpolator stepInterpolator = new StepInterpolator();
        stepInterpolator.setForward(true);
        mockEventHandler.setThrowConvergenceExceptionOnGEvaluation = true;
        eventState.evaluateStep(stepInterpolator);
    }

    @Test
    public void testGetEventTime() {
        double t1WithEvent = 2.001;
        mockEventHandler.setGValueForTime = (t, y) -> Math.sin(t);
        eventState.evaluateStep(stepInterpolator);
        Assert.assertEquals(t1WithEvent, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 2.001;
        double[] y = new double[]{1.0, 2.0, 3.0};
        eventState.stepAccepted(t, y);
        Assert.assertEquals(t, eventState.getT0(), 0.0);
        Assert.assertEquals(mockEventHandler.g(t, y), eventState.getG0(), 0.0);
        Assert.assertFalse(eventState.isPendingEvent());
    }

    @Test(expected = EventException.class)
    public void testStepAcceptedWithException() throws EventException {
        mockEventHandler.setThrowExceptionOnGEvaluation = true;
        double t = 2.001;
        double[] y = new double[]{1.0, 2.0, 3.0};
        eventState.stepAccepted(t, y);
    }

    @Test
    public void testStop() {
        mockEventHandler.setStopOnGEvaluation = true;
        Assert.assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 2.001;
        double[] y = new double[]{1.0, 2.0, 3.0};
        eventState.reset(t, y);
        Assert.assertFalse(eventState.isPendingEvent());
        Assert.assertNull(eventState.getPendingEventTime());
    }

    @Test(expected = EventException.class)
    public void testResetWithException() throws EventException {
        mockEventHandler.setThrowExceptionOnReset = true;
        double t = 2.001;
        double[] y = new double[]{1.0, 2.0, 3.0};
        eventState.reset(t, y);
    }

    @Test
    public void testNextActionIsContinue() {
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testNextActionIsStop() {
        mockEventHandler.setStopOnGEvaluation = true;
        eventState.evaluateStep(stepInterpolator);
        Assert.assertEquals(EventHandler.STOP, eventState.getNextAction());
    }

    @Test
    public void testNextActionIsResetState() {
        mockEventHandler.setResetStateOnGEvaluation = true;
        eventState.evaluateStep(stepInterpolator);
        Assert.assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
    }

    @Test
    public void testNextActionIsResetDerivatives() {
        mockEventHandler.setResetDerivativesOnGEvaluation = true;
        eventState.evaluateStep(stepInterpolator);
        Assert.assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
    }

    private static class MockEventHandler implements EventHandler {

        private boolean throwExceptionOnGEvaluation = false;
        private boolean throwEventExceptionOnGEvaluation = false;
        private boolean throwConvergenceExceptionOnGEvaluation = false;
        private boolean throwExceptionOnReset = false;
        private boolean stopOnGEvaluation = false;
        private boolean resetStateOnGEvaluation = false;
        private boolean resetDerivativesOnGEvaluation = false;

        @Override
        public double g(double t, double[] y) throws DerivativeException, EventException {
            if (throwExceptionOnGEvaluation) {
                throw new DerivativeException("Test exception");
            }
            if (throwEventExceptionOnGEvaluation) {
                throw new EventException("Test exception");
            }
            if (throwConvergenceExceptionOnGEvaluation) {
                throw new ConvergenceException("Test exception");
            }
            return Math.sin(t);
        }

        @Override
        public void eventOccurred(double t, double[] y, boolean crossed) {
            // empty implementation
        }

        @Override
        public void resetState(double t, double[] y) {
            if (throwExceptionOnReset) {
                throw new EventException("Test exception");
            }
            System.out.println("Resetting state at t=" + t + ", y=" + Arrays.toString(y));
        }

        public void setGValueForTime(UnivariateRealFunction gFunction) {
            this.gFunction = gFunction;
        }

        public void setThrowExceptionOnGEvaluation(boolean throwException) {
            this.throwExceptionOnGEvaluation = throwException;
        }

        public void setThrowEventExceptionOnGEvaluation(boolean throwEventException) {
            this.throwEventExceptionOnGEvaluation = throwEventException;
        }

        public void setThrowConvergenceExceptionOnGEvaluation(boolean throwConvergenceException) {
            this.throwConvergenceExceptionOnGEvaluation = throwConvergenceException;
        }

        public void setThrowExceptionOnReset(boolean throwException) {
            this.throwExceptionOnReset = throwException;
        }

        public void setStopOnGEvaluation(boolean stop) {
            this.stopOnGEvaluation = stop;
        }

        public void setResetStateOnGEvaluation(boolean resetState) {
            this.resetStateOnGEvaluation = resetState;
        }

        public void setResetDerivativesOnGEvaluation(boolean resetDerivatives) {
            this.resetDerivativesOnGEvaluation = resetDerivatives;
        }

    }

}