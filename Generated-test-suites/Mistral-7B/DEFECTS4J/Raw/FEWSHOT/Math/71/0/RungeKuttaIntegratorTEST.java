package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.EvictionPolicy;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws Exception {
        double[][] a = {
            {0.0},
            {0.5, 0.5},
            {0.5, 0, 0.5},
            {0.5, 0.25, 0.25, 0.5}
        };
        double[] b = {0.25, 0.125, 0.125, 0.5};
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double step = 0.1;

        RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", c, a, b, new RungeKuttaStepInterpolator(), step);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] y0 = {1.0};
        double t0 = 0.0;
        double t = 0.5;
        double[] y = new double[y0.length];

        integrator.integrate(equations, t0, y0, t, y);

        assertEquals(1.31622776601683793, y[0], 1e-6);

        // Test with events
        EventManager eventsManager = new EventManager(EvictionPolicy.LIFO);
        eventsManager.addEvent(new DefaultValueEvent(0.5, 1.5));

        integrator = new RungeKuttaIntegrator("RK4", c, a, b, new RungeKuttaStepInterpolator(), step) {
            @Override
            protected AbstractStepInterpolator createStepInterpolator() {
                return new RungeKuttaStepInterpolator(this, y, yDotK, forward) {
                    @Override
                    public void handleEvent(Event event) {
                        super.handleEvent(event);
                        System.out.println("Event triggered at " + event.getTime());
                    }
                };
            }
        };

        integrator.setEventsHandlersManager(eventsManager);

        integrator.integrate(equations, t0, y0, t, y);

        assertEquals(1.31622776601683793, y[0], 1e-6);
        assertEquals(1.5, equations.getTime(), 1e-6);
        assertArrayEquals(new double[] {0.0}, equations.getDerivatives(), 1e-6);
        assertEquals(1.5, integrator.getStepStart(), 1e-6);
        assertEquals(-0.1, integrator.getStepSize(), 1e-6);
    }

    @Test(expected = DeriveException.class)
    public void testDerivativeException() throws Exception {
        double[][] a = {
            {0.0},
            {0.5, 0.5},
            {0.5, 0, 0.5},
            {0.5, 0.25, 0.25, 0.5}
        };
        double[] b = {0.25, 0.125, 0.125, 0.5};
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double step = 0.1;

        RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", c, a, b, new RungeKuttaStepInterpolator(), step);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {
            @Override
            public double[] getDerivatives(double t, double[] y) {
                throw new DerivativeException("Dummy exception");
            }
        };
        double[] y0 = {1.0};
        double t0 = 0.0;
        double t = 0.5;
        double[] y = new double[y0.length];

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        double[][] a = {
            {0.0},
            {0.5, 0.5},
            {0.5, 0, 0.5},
            {0.5, 0.25, 0.25, 0.5}
        };
        double[] b = {0.25, 0.125, 0.125, 0.5};
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double step = 0.1;

        RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", c, a, b, new RungeKuttaStepInterpolator(), step);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] y0 = {1.0};
        double t0 = 0.0;
        double t = 0.5;
        double[] y = new double[y0.length];

        integrator.setStepHandlers(new StepHandler[] {
                new StepHandler() {
                    @Override
                    public void handleStep(StepHandler.StepEvent event) {
                        throw new IntegratorException("Dummy exception");
                    }
                }
        });

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test
    public void testNullInput() {
        assertNull(new RungeKuttaIntegrator(null, null, null, null, null, 0));
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public double getTime() {
        return 0;
    }

    @Override
    public double[] getDerivatives(double t, double[] y) {
        return new double[] {1.0};
    }
}