package org.apache.commons.math.ode.events;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class EventStateTest {

    private EventHandler handler;
    private StepInterpolator interpolator;

    @Before
    public void setUp() {
        handler = new EventHandler() {
            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }

            @Override
            public double g(double t, double[] y) {
                return Math.sin(t);
            }

            @Override
            public int eventOccurred(double t, double[] y, boolean crosses) {
                if (Double.isNaN(y[0])) {
                    throw new NumberIsTooSmallException(t, "Negative initial state");
                }
                if (y[0] > Math.PI) {
                    throw new NumberIsTooLargeException(t, "State is too large");
                }
                if (crosses) {
                    return EventHandler.RESET_STATE;
                }
                return EventHandler.CONTINUE;
            }
        };

        interpolator = new StepInterpolator(new StepHandler() {

            @Override
            public void handleStep(double t, double[] y, double[] dy, double h) {
                // no-op
            }
        });
    }

    @Test
    public void testEventStateIntegration() throws Exception {
        EventState state = new EventState(handler, 0.01, 1e-5, 1000);

        double[] y = new double[] { 1.0 };
        state.reinitializeBegin(0.0, y);

        interpolator.setInitialStepSize(0.1);
        interpolator.setMaximumStepSize(0.5);

        double t = 0.0;
        double[] yStart;
        while (true) {
            try {
                boolean acceptStep = state.evaluateStep(interpolator);
                if (! acceptStep) {
                    interpolator.next();
                    continue;
                }
                yStart = interpolator.getInterpolatedState();
                state.stepAccepted(interpolator.getCurrentTime(), yStart);
                t = interpolator.getCurrentTime();
                Assert.assertArrayEquals(new double[] { Math.sin(t) }, yStart, 1e-5);
                interpolator.next();
            } catch (ConvergenceException e) {
                // this exception should not be thrown by the test code
                Assert.fail("Unexpected exception: " + e.getMessage());
            } catch (DerivativeException e) {
                // this exception should not be thrown by the test code
                Assert.fail("Unexpected exception: " + e.getMessage());
            } catch (EventException e) {
                if (state.stop()) {
                    Assert.assertEquals(t, 2 * Math.PI, 1e-5);
                    Assert.assertArrayEquals(new double[] { 0.0 }, y, 1e-5);
                    break;
                }
                Assert.fail("Unexpected event: " + e.getMessage());
            }
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEventStateNegativeInitialState() throws Exception {
        EventState state = new EventState(handler, 0.01, 1e-5, 1000);
        double[] y = new double[] { -1.0 };
        state.reinitializeBegin(0.0, y);
        state.evaluateStep(interpolator);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEventStateStateTooLarge() throws Exception {
        EventState state = new EventState(handler, 0.01, 1e-5, 1000);
        double[] y = new double[] { 3.0 };
        state.reinitializeBegin(0.0, y);
        state.evaluateStep(interpolator);
    }

    @Test(expected = MaxEvalException.class)
    public void testEventStateInfiniteIterations() throws Exception {
        EventState state = new EventState(handler, 0.01, 1e-5, 1);
        double[] y = new double[] { 1.0 };
        state.reinitializeBegin(0.0, y);

        interpolator.setInitialStepSize(0.1);
        interpolator.setMaximumStepSize(0.5);

        double t = 0.0;
        double[] yStart;
        while (true) {
            try {
                boolean acceptStep = state.evaluateStep(interpolator);
                if (! acceptStep) {
                    interpolator.next();
                    continue;
                }
                yStart = interpolator.getInterpolatedState();
                state.stepAccepted(interpolator.getCurrentTime(), yStart);
                t = interpolator.getCurrentTime();
                Assert.assertArrayEquals(new double[] { Math.sin(t) }, yStart, 1e-5);
                interpolator.next();
            } catch (ConvergenceException e) {
                // this exception should not be thrown by the test code
                Assert.fail("Unexpected exception: " + e.getMessage());
            } catch (DerivativeException e) {
                // this exception should not be thrown by the test code
                Assert.fail("Unexpected exception: " + e.getMessage());
            } catch (EventException e) {
                if (state.stop()) {
                    break;
                }
                Assert.fail("Unexpected event: " + e.getMessage());
            }
            if (Math.abs(Math.sin(t)) > 1) {
                throw new MaxEvalException("Maximum number of evaluations exceeded");
            }
        }
    }

}