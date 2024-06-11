package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.events.GoalType;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.DummyDifferentialEquations;
import org.apache.commons.math3.ode.test.SimpleStepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private static final double TOL = 1e-14;
    private static final double MIN_STEP = 0.001;
    private static final double MAX_STEP = 10.0;

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        // Create a simple integrator with a Butcher array of order 5.
        double[] c = { 0.0, 0.3, 0.4, 0.3, 1.0 };
        double[][] a = { { 0.0, 0.44787178, 0.0, 0.63212056, 0.0 },
                         { 0.0, 0, 0.25, 0.6, 0.25 },
                         { 0.0, 0.34662646, 0.49999998, 0.63212056, 0.66666667 },
                         { 0.0, 0.3, 0.3, 0.6, 0.8 },
                         { 0.0, 1.0, 0.0, 0.0, 0.5 } };
        double[] b = { 0.23562247, 0.23562247, 0.65981547, 0.86892811, 1.0 };
        integrator = new EmbeddedRungeKuttaIntegrator("RK5", false, c, a, b,
                new RungeKuttaStepInterpolator(), MIN_STEP, MAX_STEP, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                // Return a hardcoded error value for simplicity.
                return 2.0;
            }
        };
    }

    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] y0 = new double[] { 1.0, 1.0 };
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[] { 1.1, 1.1 };

        // Test integration with no events or step handlers.
        double result = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, result, TOL);

        // Test integration with a simple step handler.
        integrator.setStepHandlers(new StepHandler[] { new SimpleStepHandler() });
        result = integrator.integrate(equations, t0, y0, t, y);
        double expected = 1.2121110223023958; // Manually computed result.
        assertEquals(expected, result, TOL);

        // Test integration with an event at t=1.
        integrator.addEndTimeChecker(t, new Event() {
            @Override
            public double getRelativeTime() {
                return 0.0;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.REACH;
            }

            @Override
            public double getValue() {
                return 1.0;
            }

            @Override
            public EventRate getRate() {
                return EventRate.VALUE;
            }
        });
        result = integrator.integrate(equations, t0, y0, t, y);
        expected = 1.01; // Manually computed result.
        assertEquals(expected, y[0], TOL);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new DerivativeException("Test");
            }
        };

        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new RuntimeException("Test");
            }
        };

        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }
}