import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() {
        EventState eventState = new EventState(new EventHandler() {
            public double g(double t, double[] y) {
                return 0;
            }

            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.CONTINUE;
            }

            public void resetState(double t, double[] y) {
            }
        }, 0.1, 0.01, 10);

        eventState.reinitializeBegin(0, new double[]{});

        // Assert Statements
        assertEquals(0, eventState.getEventHandler().g(0, new double[]{}), 0);
    }

    @Test
    public void testEvaluateStep() {
        EventState eventState = new EventState(new EventHandler() {
            public double g(double t, double[] y) {
                if (t == 0) {
                    return -1;
                } else if (t == 1) {
                    return 1;
                }
                return 0;
            }

            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.CONTINUE;
            }

            public void resetState(double t, double[] y) {
            }
        }, 0.1, 0.01, 10);

        try {
            eventState.reinitializeBegin(0, new double[]{});
            boolean trigger = eventState.evaluateStep(new StepInterpolator() {
                public void setInterpolatedTime(double time) throws IllegalArgumentException {
                }

                public double getCurrentTime() {
                    return 1;
                }

                public boolean isForward() {
                    return true;
                }

                public void computeInterpolatedState(double time, double[] state) throws DerivativeException, IllegalArgumentException {
                }

                public double[] getInterpolatedState() throws IllegalStateException {
                    return new double[]{};
                }

                public double getInterpolatedTime() throws IllegalStateException {
                    return 0;
                }

                public double getPreviousTime() throws IllegalStateException {
                    return Double.NaN;
                }

                public boolean isPreviousTimeInclusive() throws IllegalStateException {
                    return false;
                }
            });
            assertTrue(trigger);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testStepAccepted() {
        EventState eventState = new EventState(new EventHandler() {
            public double g(double t, double[] y) {
                return 0;
            }

            public int eventOccurred(double t, double[] y, boolean increasing) {
                return (t == 0) ? EventHandler.STOP : EventHandler.CONTINUE;
            }

            public void resetState(double t, double[] y) {
            }
        }, 0.1, 0.01, 10);

        try {
            eventState.stepAccepted(1, new double[]{});
            assertFalse(eventState.stop());
            eventState.stepAccepted(2, new double[]{});
            assertTrue(eventState.stop());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testReset() {
        EventState eventState = new EventState(new EventHandler() {
            public double g(double t, double[] y) {
                return 0;
            }

            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.RESET_STATE;
            }

            public void resetState(double t, double[] y) {
            }
        }, 0.1, 0.01, 10);

        try {
            eventState.reinitializeBegin(0, new double[]{});
            assertTrue(eventState.reset(1, new double[]{}));
            assertFalse(eventState.reset(2, new double[]{}));
        } catch (Exception e) {
            fail();
        }
    }

}