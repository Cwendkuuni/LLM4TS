package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueStore;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.SimpleEvent;
import org.apache.commons.math3.ode.integration.StepHandler;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        // define a test differential equation
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        // create an integrator with a simple Runge-Kutta method
        integrator = new RungeKutta4Method("RK4", false,
                new double[] { 0.25, 0.45, 0.5, 0.5 },
                new double[][] { { 0.25, 0, 0.25, 0, 0.25, 0 },
                                  { 0, 0.25, 0.25, 0.25, 0.25, 0.25 },
                                  { 0, 0, 0, 0.5, 0.5, 0.5 },
                                  { 0, 0, 0, 0.5, 0.5, 0.5 } },
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new RungeKuttaStepInterpolator(),
                0.01, 0.05, new double[] { 1e-5, 1e-5 });
    }

    @Test
    public void testIntegration() {
        double[] y0 = new double[] { 0.1 };
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[] { 0.841473 };

        double result = integrator.integrate(equations, t0, y0, t1, null);
        assertTrue(Math.abs(result - t1) <= 1e-10);
        assertArrayEquals(y1, integrator.getSolution(), 1e-10);
    }

    @Test
    public void testIntegrationWithEvent() {
        double[] y0 = new double[] { 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }

            @Override
            public Event[] getEvents(double t, double[] y, double h) {
                if (y[0] >= 1.5) {
                    return new Event[] { new SimpleEvent(t, "TestEvent") };
                }
                return new Event[0];
            }
        };

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(RungeKuttaStepInterpolator interpolator, int index) {
                DefaultValueStore store = interpolator.getValueStore();
                double t = store.getValue(0);
                double[] y = store.getValuesBelow(0);
                assertTrue(y[0] <= 1.5);
            }
        });

        integrator.integrate(equations, t0, y0, t1, null);
    }
}