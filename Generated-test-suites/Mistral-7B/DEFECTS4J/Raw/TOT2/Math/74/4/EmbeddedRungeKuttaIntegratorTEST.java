package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.IntervalEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        // Set up a simple integrator for testing
        integrator = new EmbeddedRungeKutta25();
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegration() {

        // Define a simple system of differential equations
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 2 * y[0];
            }
        };

        // Define a step handler for testing
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(double t, double[] y, double[] yDot) {
                assertEquals(2 * y[0], yDot[0], 0.01, "Derivative does not match");
            }
        };

        // Define a test event manager
        final EventManager eventManager = new EventManager();
        eventManager.addEqualityEvent(t -> new IntervalEvent(t, 10.0, y -> y[0] == 1.0));

        // Test integration with no events
        final double[] y0 = {0.0};
        final double t0 = 0.0;
        final double t1 = 5.0;

        integrator.addStepHandler(stepHandler);

        final double result = integrator.integrate(equations, t0, y0, t1, new double[1]);

        assertEquals(t1, result, 0.01, "Integration did not reach expected time");
        assertThrows(IllegalArgumentException.class, () -> integrator.integrate(equations, t0, y0, -1.0, new double[1]));
        assertThrows(NumberIsTooSmallException.class, () -> integrator.integrate(equations, t0, y0, 0.0, new double[1]));
        assertThrows(MaxCountExceededException.class, () -> integrator.integrate(equations, t0, y0, Double.POSITIVE_INFINITY, new double[1]));

        // Test integration with an event
        eventManager.addStepHandler(t -> integrator.removeStepHandler(stepHandler));
        integrator.addStepHandler(stepHandler);
        integrator.setEventsManager(eventManager);

        final double[] y1 = new double[1];
        final double[] state = new double[1];
        integrator.integrate(equations, t0, y0, 10.1, state);

        assertEquals(1.0, state[0], 0.01, "Step did not reach expected value at event");

    }

}