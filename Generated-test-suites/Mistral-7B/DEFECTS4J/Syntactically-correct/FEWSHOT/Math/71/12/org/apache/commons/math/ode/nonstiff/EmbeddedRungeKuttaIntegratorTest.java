package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.IsNullEvent;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[][] a = {{0.0, 0.3, 0.6}, {0.0, 0.27, 0.45, 0.84}, {0.0, 0}, {0.0, 0.6, 0.87, 0.27, 1.35}};
        double[] b = {0.3, 0.45, 0.84, 1.0, 1.35};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta54Integrator("EmbeddedRungeKutta54Integrator", true, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] y0 = new double[]{0.0, 0.0};

        // Test integration with no events
        double tEnd = 10.0;
        double[] y = integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        assertEquals(tEnd, y[0], 1e-10);
        assertEquals(tEnd, y[1], 1e-10);

        // Test integration with an event at t=2.0
        equations = new DummyDifferentialEquationsWithEvent();
        y0 = new double[]{0.0, 0.0};
        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(new IsNullEvent(0.0));
        manager.addEvent(new IsNullEvent(2.0));
        integrator = new EmbeddedRungeKutta54Integrator("EmbeddedRungeKutta54Integrator", true, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.max(1.1 * Math.abs(y1[0] - y0[0]), 1.1 * Math.abs(y1[1] - y0[1]));
            }
        };
        double tStart = 0.0;
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                System.out.println("Step " + stepNumber + ": t = " + context.getStepTime());
            }
        });
        double[] y2 = integrator.integrate(equations, tStart, y0, tEnd, manager.getEvents());
        assertEquals(3, manager.getEventCount());
        assertEquals(2.0, manager.getEventTime(0), 1e-10);
        assertEquals(3.0, manager.getEventTime(1), 1e-10);
        assertEquals(4.0, manager.getEventTime(2), 1e-10);
    }
}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        for (int i = 0; i < y.length; i++) {
            yPrime[i] = Math.exp(y[i]);
        }
    }
}

class DummyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {
    private boolean eventFired = false;

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        if (t >= 2.0) {
            eventFired = true;
            for (int i = 0; i < y.length; i++) {
                yPrime[i] = 0.0;
            }
        } else {
            for (int i = 0; i < y.length; i++) {
                yPrime[i] = Math.exp(y[i]);
            }
        }
    }

    public boolean getEventFired() {
        boolean result = eventFired;
        eventFired = false;
        return result;
    }
}