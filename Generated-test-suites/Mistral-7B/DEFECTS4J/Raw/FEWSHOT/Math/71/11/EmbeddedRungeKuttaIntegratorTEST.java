package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.NoInitialValueDerivativeProvider;
import org.apache.commons.math3.ode.events.AbstractValueChecker;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.ValueChecker;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

/**
 * Test cases for the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Create a dummy differential equation provider
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        // Create a test integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaRk4Integrator(
                "RK4", false,
                new double[] { 0.25, 0, 0.25, 0.25, 0.5 },
                new double[][] { { 0.25, 0, 0.25, 0.25, 0.5 },
                               { 0.0, 0.25, 0.25, 0.25, 0.5 },
                               { 0.0, 0.0, 0.5, 0.5, 0.5 },
                               { 0.0, 0.0, 0.0, 0.75, 0.75 },
                               { 0.0, 0.0, 0.0, 0.0, 1.0 } },
                new double[] { 0.25, 0.25, 0.25, 0.25, 0.25 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(RungeKuttaIntegrator integrator, double[] y,
                            double[][] yDot, boolean forward) {
                        // Do nothing
                    }
                },
                0.001, 0.01,
                null, null);

        // Test integration
        double[] y = new double[] { 1.0, 1.0 };
        double t = 0.0;
        double stepSize = 0.01;
        double result = integrator.integrate(equations, t, y, t + stepSize, y);
        assertEquals(t + stepSize, result, 1e-11);

        // Test integration with events
        ValueChecker valueChecker = new AbstractValueChecker() {
            @Override
            public Event check(double t, double[] y) {
                if (Math.abs(y[0] - 2.0) < 1e-11) {
                    return new Event(t, new EventType() { });
                }
                return null;
            }
        };
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepContext context, int stepNumber) {
                System.out.println("Step " + stepNumber + " taken at t = " + context.getTime()
                        + ", y = " + java.util.Arrays.toString(context.getY()));
            }
        };
        CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addValueChecker(valueChecker);
        eventsManager.addStepHandler(stepHandler);
        integrator.setEventsHandlersManager(eventsManager);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 1.0 }, 2.0, new double[2]);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorWithInitialValueDerivativeProvider() {
        // Create a dummy differential equation provider with initial value derivatives
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsWithInitialValueDerivativeProvider();

        // Create a test integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaRk4Integrator(
                "RK4", false,
                new double[] { 0.25, 0, 0.25, 0.25, 0.5 },
                new double[][] { { 0.25, 0, 0.25, 0.25, 0.5 },
                               { 0.0, 0.25, 0.25, 0.25, 0.5 },
                               { 0.0, 0.0, 0.5, 0.5, 0.5 },
                               { 0.0, 0.0, 0.0, 0.75, 0.75 },
                               { 0.0, 0.0, 0.0, 0.0, 1.0 } },
                new double[] { 0.25, 0.25, 0.25, 0.25, 0.25 },
                new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(RungeKuttaIntegrator integrator, double[] y,
                            double[][] yDot, boolean forward) {
                        // Do nothing
                    }
                },
                0.001, 0.01,
                null, null);

        // Test integration
        double[] y = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 };
        double t = 0.0;
        double stepSize = 0.01;
        double result = integrator.integrate(equations, t, y, t + stepSize, y);
        assertEquals(t + stepSize, result, 1e-11);
    }

}