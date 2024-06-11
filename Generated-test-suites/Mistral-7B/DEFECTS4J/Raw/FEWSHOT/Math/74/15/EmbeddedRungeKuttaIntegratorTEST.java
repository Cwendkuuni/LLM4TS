package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.step.RungeKuttaStepHandler;
import org.apache.commons.math3.ode.step.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** The differential equations to integrate. */
    private static final FirstOrderDifferentialEquations equations =
        new SimpleDifferentialEquations();

    /** The step interpolator. */
    private RungeKuttaStepInterpolator interpolator;

    /** The step handler. */
    private StepHandler stepHandler;

    /** The embedded Runge-Kutta integrator to test. */
    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        interpolator = new RungeKuttaStepInterpolator(4);
        stepHandler = new RungeKuttaStepHandler();
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[] { 0.25, 0.5, 0.5, 0.25 },
                                                      new double[][] { { 0.25, 0 }, { 0, 0.25 }, { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.25, 0 } },
                                                      new double[] { 0.25, 0.25, 0.25, 0.25 }, new DummyStepInterpolator()) {
            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 2.0;
            }
        };
        integrator.setMinStep(0.01);
        integrator.setMaxStep(10.0);
        integrator.setSafety(0.9);
        integrator.setMinReduction(0.2);
        integrator.setMaxGrowth(10.0);
    }

    /**
     * Test the integration of simple differential equations.
     */
    @Test
    public void testIntegration() throws Exception {
        double[] initialState = new double[] { 0.0, 1.0 };
        double startTime = 0.0;
        double endTime = 3.0;

        double[] solution = new double[] { 1.0, 2.0, 2.0, 1.5 };
        double[] absoluteTolerance = new double[] { 1.0E-9, 1.0E-9 };
        double[] relativeTolerance = new double[] { 1.0E-6, 1.0E-6 };

        integrator.integrate(equations, startTime, initialState, endTime, new double[2]);

        assertArrayEquals(solution, initialState, 1.0E-6);
    }

    /**
     * Test the handling of an event.
     */
    @Test
    public void testEvent() throws Exception {
        double[] initialState = new double[] { 0.0, 0.5 };
        double startTime = 0.0;
        double endTime = 2.0;

        double[] solution = new double[] { 0.0, 1.0 };
        double[] absoluteTolerance = new double[] { 1.0E-9, 1.0E-9 };
        double[] relativeTolerance = new double[] { 1.0E-6, 1.0E-6 };

        integrator.addStepHandler(stepHandler);
        integrator.addEvent(new When(2.0), new MyEvent());

        double[] y = new double[2];
        integrator.integrate(equations, startTime, initialState, endTime, y);

        assertFalse(stepHandler.isHandled());

        integrator.resetInternalState();

        initialState[0] = 0.1;
        integrator.integrate(equations, startTime, initialState, endTime, y);

        assertTrue(stepHandler.isHandled());

        assertArrayEquals(solution, y, 1.0E-6);
    }

    /**
     * An event handler.
     */
    private static class MyEvent extends Event {

        @Override
        public boolean isRegularArrivalTime() {
            return true;
        }

        @Override
        public double getTime() {
            return 2.0;
        }

        @Override
        public void evaluateEventEffect(double[] y, double t, double[] dy) {
            dy[1] = 1.0;
        }

    }

    /**
     * Dummy step handler.
     */
    private static class DummyStepHandler implements StepHandler {

        private boolean handled;

        @Override
        public void handleStep(StepHandler event, int type) {
            handled = true;
        }

        public boolean isHandled() {
            return handled;
        }

        public void reset() {
            handled = false;
        }

    }

    /**
     * Dummy step interpolator.
     */
    private static class DummyStepInterpolator extends AbstractStepInterpolator {

        @Override
        public double interpolate(double t, double[] y0, double[] y) {
            return y[0];
        }

        @Override
        public double interpolateDerivative(double t, double[] y0, double[] y) {
            return 0.0;
        }

        @Override
        public double[] interpolateVectorValue(double t, double[] y0, double[] y) {
            return new double[] { y[0] };
        }

        @Override
        public double[] interpolateVectorDerivative(double t, double[] y0, double[] y) {
            return new double[] { 0.0 };
        }

    }

    /**
     * Simple differential equations.
     */
    private static class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] dy) {
            dy[0] = y[1];
            dy[1] = -y[0] + 1.0;
        }

    }

}