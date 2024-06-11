package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /** Integrator under test. */
    private RungeKuttaIntegrator integrator;

    /** Differential equations to be integrated. */
    private FirstOrderDifferentialEquations equations;

    /** Test data. */
    private double[] y0;
    private double t0, t, step;

    /** Step handler to be notified after each step. */
    private StepHandler stepHandler;

    /**
     * Initialize the equations and the integrator.
     */
    @Before
    public void setUp() {
        equations = new MyDifferentialEquations();
        integrator = new ClassicalRungeKuttaIntegrator(
                0.3, new ClassicalRungeKuttaMethod(), new DummyStepInterpolator(), 0.01);
        step = 0.01;
        y0 = new double[] { 0.0, 0.0 };
        t0 = 0.0;
        t = 1.0;
        stepHandler = new StepCounter();
        integrator.addStepHandler(stepHandler);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() {
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);
        Assert.assertArrayEquals(new double[] { 0.749895522855, 0.749895522855 }, y0, 0.0001);
    }

    /**
     * Test method for handling an exception when the integration is impossible
     * due to too many iterations.
     */
    @Test(expected = TooManyIterationsException.class)
    public void testTooManyIterationsException() {
        integrator = new ClassicalRungeKuttaIntegrator(
                0.3, new ClassicalRungeKuttaMethod(), new DummyStepInterpolator(), 0.01, null, 100);
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);
    }

    /**
     * Test method for handling an exception when the integration is impossible
     * due to a too large step size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        integrator = new ClassicalRungeKuttaIntegrator(
                0.3, new ClassicalRungeKuttaMethod(), new DummyStepInterpolator(), 1.0);
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);
    }

    /**
     * Test method for handling an exception when the integration is impossible
     * due to exceeding the maximum count of iterations.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        integrator = new ClassicalRungeKuttaIntegrator(
                0.3, new ClassicalRungeKuttaMethod(), new DummyStepInterpolator(), 0.01, null, 0);
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);
    }

    /**
     * Test method for handling events during integration.
     */
    @Test
    public void testHandleEvents() {
        integrator = new ClassicalRungeKuttaIntegrator(
                0.3, new ClassicalRungeKuttaMethod(), new DummyStepInterpolator(), 0.01);
        integrator.addEventsHandler(new MyEvent());
        integrator.integrate(equations, t0, y0, t, new double[y0.length]);
        Assert.assertTrue(stepHandler.getStepCount() >= 2);
    }

    /**
     * Test step counter.
     */
    private static class StepCounter implements StepHandler {
        private int stepCount = 0;

        @Override
        public void handleStep(AbstractIntegrator integrator, double[] y, double t, int step) {
            stepCount++;
        }

        public int getStepCount() {
            return stepCount;
        }

        @Override
        public void reset() {
            stepCount = 0;
        }
    }

    /**
     * Test differential equations.
     */
    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0];
        }

    }

    /**
     * Test event handler.
     */
    private static class MyEvent implements Event {

        @Override
        public double value(double t) {
            return 0.0;
        }

        @Override
        public boolean occurs(double t, double[] y) {
            return (y[0] > 0.5);
        }

        @Override
        public double time() {
            return 0.5;
        }

        @Override
        public void register(When when, double h) {
            // not used
        }

        @Override
        public void unregister() {
            // not used
        }

    }

}