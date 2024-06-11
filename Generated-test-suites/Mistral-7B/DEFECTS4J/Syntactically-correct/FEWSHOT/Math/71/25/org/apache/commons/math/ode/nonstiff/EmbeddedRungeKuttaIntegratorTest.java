package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator(
                "MyMethod", false,
                new double[] {0.3, 0.5, 0.8, 1.0},
                new double[][] {
                        {0.0, 0.25, 0.25, 0.5},
                        {0.0, 0, 0.5, 0.5},
                        {0.0, 0, 0.8333333333333333, 0.1666666666666667},
                        {0.0, 0, 1.0, 0.0}
                },
                new double[] {0.25, 0.5, 0.8333333333333333, 1.0},
                new RungeKuttaStepInterpolator(),
                0.001, 0.01,
                1.0e-8, 5.0e-6);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] y0 = {1.0, 1.0};

        double t = 0.0;
        double[] y = integrator.integrate(equations, 0.0, y0, t, new double[y0.length]);

        assertEquals(2.0, y[0], 1.0e-8);
        assertEquals(2.0, y[1], 1.0e-8);

        t = 0.1;
        y = integrator.integrate(equations, 0.0, y0, t, new double[y0.length]);

        assertEquals(2.125, y[0], 1.0e-5);
        assertEquals(2.125, y[1], 1.0e-5);

        t = 0.2;
        y = integrator.integrate(equations, 0.0, y0, t, new double[y0.length]);

        assertEquals(2.40625, y[0], 1.0e-5);
        assertEquals(2.40625, y[1], 1.0e-5);

        // Test event handling
        FirstOrderDifferentialEquations equationsWithEvent = new DummyDifferentialEquationsWithEvent();
        double[] yEvent = {1.0, 1.0};

        t = 0.25;
        Event event = new When(0.25);
        CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(event);

        y = integrator.integrate(equationsWithEvent, 0.0, yEvent, t, new double[yEvent.length]);

        // The integration should stop at the event
        assertEquals(2.40625, y[0], 1.0e-5);
        assertEquals(2.40625, y[1], 1.0e-5);
        assertTrue(event.isOccurred());

        // Test that the event is reset when the integration continues
        y = integrator.integrate(equationsWithEvent, t, y, 0.25, new double[yEvent.length]);

        assertEquals(2.615625, y[0], 1.0e-5);
        assertEquals(2.615625, y[1], 1.0e-5);
        assertFalse(event.isOccurred());
    }
}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
    MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b,
                                  RungeKuttaStepInterpolator prototype, double minStep, double maxStep,
                                  double[] scalAbsoluteTolerance, double[] scalRelativeTolerance) {
        super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double error = 0.0;
        for (int i = 0; i < y0.length; i++) {
            error += Math.pow(Math.abs(y1[i] - y0[i]), 2.0);
        }
        return Math.sqrt(error);
    }
}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0];
    }
}

class DummyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {
    private Event event;

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = y[0];
        if (event != null && t >= event.getTime()) {
            event.reset();
        }
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}