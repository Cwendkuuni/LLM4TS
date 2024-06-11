package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;

/**
 * Test class for RungeKuttaIntegrator class.
 */
public class RungeKuttaIntegratorTest {

    @Test
    public void testRungeKuttaIntegrator() {
        double[] c = {0.5, 0.25, 0};
        double[][] a = {{0.5, 0}, {0.75, 0.25}, {1.0, 0.5}};
        double[] b = {0.25, 0.5, 0.875};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c, a, b);
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestRungeKuttaIntegrator", c, a, b, prototype, 0.1);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialValues = {0.0};
        double startTime = 0.0;
        double endTime = 1.0;

        double result = integrator.integrate(equations, startTime, initialValues, endTime, new double[1]);
        assertEquals(endTime, result, 1e-11);
    }

    @Test
    public void testEventHandling() {
        double[] c = {0.5, 0.25, 0};
        double[][] a = {{0.5, 0}, {0.75, 0.25}, {1.0, 0.5}};
        double[] b = {0.25, 0.5, 0.875};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c, a, b);
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestRungeKuttaIntegrator", c, a, b, prototype, 0.1);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialValues = {0.0};
        double startTime = 0.0;
        double endTime = 1.0;
        CombinedEventsManager eventsManager = new CombinedEventsManager();
        eventsManager.addEvent(new DefaultValueEvent(0.5, 1.0, 1.0));

        integrator.setEventsManager(eventsManager);

        double result = integrator.integrate(equations, startTime, initialValues, endTime, new double[1]);
        assertEquals(0.5, result, 1e-11);
        assertArrayEquals(new double[] {1.0}, integrator.getLastValues(), 1e-11);
    }

    @Test(expected = DeriveException.class)
    public void testDerivativeException() {
        double[] c = {0.5, 0.25, 0};
        double[][] a = {{0.5, 0}, {0.75, 0.25}, {1.0, 0.5}};
        double[] b = {0.25, 0.5, 0.875};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c, a, b);
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestRungeKuttaIntegrator", c, a, b, prototype, 0.1);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new DerivativeException("Test Derivative Exception");
            }
        };

        double[] initialValues = {0.0};
        double startTime = 0.0;
        double endTime = 1.0;

        integrator.integrate(equations, startTime, initialValues, endTime, new double[1]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        double[] c = {0.5, 0.25, 0};
        double[][] a = {{0.5, 0}, {0.75, 0.25}, {1.0, 0.5}};
        double[] b = {0.25, 0.5, 0.875};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c, a, b);
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestRungeKuttaIntegrator", c, a, b, prototype, 0.1);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialValues = {0.0};
        double startTime = 0.0;
        double endTime = 1.0;

        integrator.setEventsManager(new CombinedEventsManager() {
            @Override
            public void addEvent(Event event) {
                throw new IntegratorException("Test Integrator Exception");
            }
        });

        integrator.integrate(equations, startTime, initialValues, endTime, new double[1]);
    }

}