import org.junit.Assert;
import org.junit.Test;

public class EventStateTest {

    private class MockHandler implements EventHandler {

        private double[] state;
        private double time;
        private double value;
        private boolean forward;

        public MockHandler(double[] state, double time, double value, boolean forward) {
            this.state = state;
            this.time = time;
            this.value = value;
            this.forward = forward;
        }

        public double g(double t, double[] y) throws EventException {
            if (t == time) {
                return value;
            }
            return 0;
        }

        public int eventOccurred(double t, double[] y, boolean increasing) {
            return 0;
        }

        public void resetState(double t, double[] y) throws EventException {
        
        }
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        // Arrange
        double t0 = 0;
        double[] yStart = {1, 2, 3};
        double t = 0;
        double value = 0;
        boolean forward = true;
        EventState eventState = new EventState(new MockHandler(yStart, t, value, forward), 0, 0, 0);

        // Act
        eventState.reinitializeBegin(t0, yStart);

        // Assert
        Assert.assertEquals(t0, eventState.t0, 0);
        Assert.assertEquals(value, eventState.g0, 0);
        Assert.assertEquals(true, eventState.g0Positive);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        // Arrange
        double[] yStart = {1, 2, 3};
        double t = 0;
        double value = 0;
        boolean forward = true;
        EventState eventState = new EventState(new MockHandler(yStart, t, value, forward), 0, 0, 0);
        StepInterpolator interpolator = new StepInterpolator() {

            private double time = 0;
            private double[] state = {1, 2, 3};

            @Override
            public void reinitialize(double[] y, boolean forward) {

            }

            @Override
            public void setInterpolatedTime(double time) {
                this.time = time;
            }

            @Override
            public boolean isForward() {
                return forward;
            }

            @Override
            public double getCurrentTime() {
                return time;
            }

            @Override
            public double[] getInterpolatedState() {
                return state;
            }

            @Override
            public double[] getInterpolatedDerivatives() {
                return null;
            }

            @Override
            public double[] getInterpolatedSecondaryState(int index) {
                return null;
            }

            @Override
            public double[] getInterpolatedSecondaryDerivatives(int index) {
                return null;
            }

            @Override
            public void writeExternal(ObjectOutput out) throws IOException {

            }

            @Override
            public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

            }
        };

        // Act
        boolean result = eventState.evaluateStep(interpolator);

        // Assert
        Assert.assertEquals(false, result);
    }

    @Test
    public void testStepAccepted() throws EventException {
        // Arrange
        double[] yStart = {1, 2, 3};
        double t = 0;
        double value = 0;
        boolean forward = true;
        EventState eventState = new EventState(new MockHandler(yStart, t, value, forward), 0, 0, 0);
        double newT = 1;
        double[] newY = {4, 5, 6};

        // Act
        eventState.stepAccepted(newT, newY);

        // Assert
        Assert.assertEquals(newT, eventState.t0, 0);
        Assert.assertEquals(value, eventState.g0, 0);
        Assert.assertEquals(true, eventState.g0Positive);
        Assert.assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    }

    @Test
    public void testStop() throws EventException {
        // Arrange
        double[] yStart = {1, 2, 3};
        double t = 0;
        double value = 0;
        boolean forward = true;
        EventState eventState = new EventState(new MockHandler(yStart, t, value, forward), 0, 0, 0);
        double newT = 1;
        double[] newY = {4, 5, 6};
        eventState.stepAccepted(newT, newY);

        // Act
        boolean result = eventState.stop();

        // Assert
        Assert.assertEquals(false, result);
    }

    @Test
    public void testReset() throws EventException {
        // Arrange
        double[] yStart = {1, 2, 3};
        double t = 0;
        double value = 0;
        boolean forward = true;
        EventState eventState = new EventState(new MockHandler(yStart, t, value, forward), 0, 0, 0);
        double newT = 1;
        double[] newY = {4, 5, 6};
        eventState.stepAccepted(newT, newY);

        // Act
        boolean result = eventState.reset(newT, newY);

        // Assert
        Assert.assertEquals(false, result);
    }
}