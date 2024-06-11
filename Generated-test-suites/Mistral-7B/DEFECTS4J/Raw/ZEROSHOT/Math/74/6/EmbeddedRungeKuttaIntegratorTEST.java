package org.apache.commons.math.ode.nonstiff. EmbeddedRungeKuttaIntegratorTest;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaMethod;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRK4Integrator() {
        final double[][] a = { { 0.0, 0.0, 0.0, 0.25 }, { 0.25, 0.0, 0.5, 0.0 },
            { 0.25, 0.5, 0.0, 0.25 }, { 0.5, 0.0, 0.25, 0.0 } };
        final double[] b = { 0.25, 0.25, 0.25, 0.25 };
        final RungeKuttaMethod rk4 = new EmbeddedRungeKuttaIntegrator(
                "RK4", true, a, b, new RungeKuttaStepInterpolator(),
                0.0, Double.POSITIVE_INFINITY, null, null) {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0,
                                           double[] y1, double h) {
                double error = 0.0;
                for (int j = 0; j < y0.length; ++j) {
                    error += Math.abs(yDotK[3][j] - yDotK[0][j]);
                }
                return error;
            }
        };

        final double[][] coefficients = rk4.getCoefficients();
        assertNotNull(coefficients);
        assertEquals(4, coefficients.length);
        for (int i = 0; i < coefficients.length; ++i) {
            assertNotNull(coefficients[i]);
            assertEquals(coefficients[i].length, 4);
        }

        final double absoluteTolerance = 1e-9;
        final double relativeTolerance = 1e-8;

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        final double[] initialCondition = new double[] { 1.0 };

        // test integration
        double t = 0.0;
        for (int i = 0; i < 10; ++i) {
            double y[] = new double[] { initialCondition[0] };
            double yNew[] = new double[y.length];
            t += rk4.integrate(equations, t, y, 10.0, yNew)[0] - t;
            assertEquals(Math.sin(y[0] + t), yNew[0], absoluteTolerance
                    | relativeTolerance * Math.abs(y[0] + t));
            System.arraycopy(yNew, 0, y, 0, y.length);
        }

        // test event handling
        final double eventTime = 2.0;
        final Event event = new When(eventTime) {
            @Override
            public void handle(double t, double[] y) {
                System.out.println("Event time = " + t);
            }
        };
        rk4.addStepHandler(event);

        // test integration with event
        t = 0.0;
        for (int i = 0; i < 10; ++i) {
            double y[] = new double[] { initialCondition[0] };
            t += rk4.integrate(equations, t, y, eventTime, yNew)[0] - t;
            assertEquals(Math.sin(y[0] + t), yNew[0], absoluteTolerance
                    | relativeTolerance * Math.abs(y[0] + t));
            System.arraycopy(yNew, 0, y, 0, y.length);
            if (t >= eventTime) {
                break;
            }
        }
        assertFalse(event.isHandled());

        // test integration with multiple events
        final Event event1 = new When(1.5) {
            @Override
            public void handle(double t, double[] y) {
                System.out.println("Event 1 time = " + t);
            }
        };
        final Event event2 = new When(2.5) {
            @Override
            public void handle(double t, double[] y) {
                System.out.println("Event 2 time = " + t);
            }
        };
        rk4.addStepHandler(event1);
        rk4.addStepHandler(event2);

        // test integration with multiple events and one step handler
        t = 0.0;
        for (int i = 0; i < 10; ++i) {
            double y[] = new double[] { initialCondition[0] };
            t += rk4.integrate(equations, t, y, eventTime, yNew)[0] - t;
            if (t >= 1.5) {
                assertNotNull(event1.isHandled());
                event1.reset();
            }
            if (t >= 2.5) {
                assertNotNull(event2.isHandled());
                event2.reset();
            }
            assertEquals(Math.sin(y[0] + t), yNew[0], absoluteTolerance
                    | relativeTolerance * Math.abs(y[0] + t));
            System.arraycopy(yNew, 0, y, 0, y.length);
            if (t >= eventTime) {
                break;
            }
        }
        assertFalse(event1.isHandled());
        assertFalse(event2.isHandled());

        // test integration with multiple events and multiple step handlers
        final StepHandler handler1 = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                System.out.println("Step 1 handler: step = " + step);
            }
        };
        final StepHandler handler2 = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                System.out.println("Step 2 handler: step = " + step);
            }
        };
        rk4.addStepHandler(event1);
        rk4.addStepHandler(event2);
        rk4.addStepHandler(handler1);
        rk4.addStepHandler(handler2);

        // test integration with multiple events and multiple step handlers
        t = 0.0;
        for (int i = 0; i < 10; ++i) {
            double y[] = new double[] { initialCondition[0] };
            t += rk4.integrate(equations, t, y, eventTime, yNew)[0] - t;
            if (t >= 1.5) {
                assertNotNull(event1.isHandled());
                event1.reset();
                assertNotNull(handler1.isHandled());
                handler1.reset();
            }
            if (t >= 2.5) {
                assertNotNull(event2.isHandled());
                event2.reset();
                assertNotNull(handler2.isHandled());
                handler2.reset();
            }
            assertEquals(Math.sin(y[0] + t), yNew[0], absoluteTolerance
                    | relativeTolerance * Math.abs(y[0] + t));
            System.arraycopy(yNew, 0, y, 0, y.length);
            if (t >= eventTime) {
                break;
            }
        }
        assertFalse(event1.isHandled());
        assertFalse(event2.isHandled());
        assertFalse(handler1.isHandled());
        assertFalse(handler2.isHandled());

    }

}