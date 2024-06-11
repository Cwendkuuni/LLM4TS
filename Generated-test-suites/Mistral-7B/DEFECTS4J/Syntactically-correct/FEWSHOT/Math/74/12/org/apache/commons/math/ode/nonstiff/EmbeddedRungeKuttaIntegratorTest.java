package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Test class for EmbeddedRungeKuttaIntegrator.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Create a sample differential equation
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]) - 1;
            }
        };

        // Create an instance of the Runge-Kutta integrator
        double[][] a = {{-0.3, 0.3}, {0.3, 0.4741659685355369}};
        double[] b = {0.6, 0.6231465968535537};
        double[] c = {0.2369268850566713, 0.8630434067224957};
        double minStep = 0.001;
        double maxStep = 10.0;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, minStep, maxStep);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, minStep, maxStep, null, null);

        // Test the getOrder method
        assertEquals(2, integrator.getOrder());

        // Test the integration of a simple differential equation
        double t0 = 0.0;
        double[] y0 = {0.0};
        double t1 = 1.0;
        double[] y1 = {1.0};
        double error = integrator.integrate(equations, t0, y0, t1, y1);
        assertEquals(0.0, error, 1e-9);

        // Test the integration of a differential equation with events
        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(1.5, new MyEvent(), null);
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, minStep, maxStep, null, null) {
            @Override
            public void handleEvent(double time, double[] value) {
                // Do nothing
            }
        };
        integrator.setEventsHandlersManager(manager);
        double[] y2 = new double[y0.length];
        integrator.integrate(equations, t0, y0, t1, y2);
        assertEquals(1.0, y2[0], 1e-9);
        assertEquals(1.5, manager.getLastEventTime(), 1e-9);

        // Test the integration of a differential equation with different tolerance values
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, minStep, maxStep, new double[]{1e-10, 1e-9}, new double[]{1e-12, 1e-11});
        double t2 = 1.0;
        double[] y3 = {1.0};
        double error2 = integrator.integrate(equations, t0, y0, t2, y3);
        assertEquals(0.0, error2, 1e-11);

        // Test the integration of a differential equation with a non-fsal method
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, minStep, maxStep, null, null) {
            @Override
            public boolean isFSAL() {
                return false;
            }
        };
        double t3 = 1.0;
        double[] y4 = {1.0};
        double error3 = integrator.integrate(equations, t0, y0, t3, y4);
        assertTrue(error3 > 0);

        // Test the integration of a differential equation with a fsal method
        integrator = new EmbeddedRungeKuttaIntegrator("test", true, c, a, b, prototype, minStep, maxStep, null, null);
        double t4 = 1.0;
        double[] y5 = {1.0};
        double error4 = integrator.integrate(equations, t0, y0, t4, y5);
        assertEquals(0.0, error4, 1e-9);

        // Test the integration of a differential equation with a large step size
        integrator.setSafety(0.5);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(100.0);
        double t5 = 10.0;
        double[] y6 = {1.0};
        double error5 = integrator.integrate(equations, t0, y0, t5, y6);
        assertTrue(error5 > 0);

        // Test the integration of a differential equation with a small step size
        integrator.setSafety(0.99);
        integrator.setMinReduction(0.01);
        integrator.setMaxGrowth(1.1);
        double t6 = 0.01;
        double[] y7 = {1.0};
        double error6 = integrator.integrate(equations, t0, y0, t6, y7);
        assertEquals(0.0, error6, 1e-9);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator2() {
        // Create a sample differential equation
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                double x = y[0];
                double yPrime = y[1];
                yPrime[0] = x * (1 - x);
                yPrime[1] = -y[0] * y[1] + E.E(x);
            }
        };

        // Create an instance of the Runge-Kutta integrator
        double[][] a = {{0.3, 0, 0.3, 0}, {0, 0.3, 0.3, 0.3}, {0.3, 0.3, 0.4741659685355369, 0.6}, {0, 0.3, 0.6231465968535537, 0.6}};
        double[] b = {0.6, 0, 0.6, 0.6};
        double[] c = {0.2369268850566713, 0.3285611030029562, 0.6231465968535537, 0.8630434067224957};
        double minStep = 0.001;
        double maxStep = 10.0;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4, minStep, maxStep);
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, minStep, maxStep, null, null);

        // Test the getOrder method
        assertEquals(4, integrator.getOrder());

        // Test the integration of a simple differential equation
        double[] y0 = {0.5, 1.0};
        double t1 = 1.0;
        double[] y1 = {0.5, 1.1359862754822707};
        double error = integrator.integrate(equations, 0.0, y0, t1, y1);
        assertEquals(0.0, error, 1e-9);

        // Test the integration of a differential equation with events
        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(0.5, new MyEvent2(), new double[]{0.5, 1.5});
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, minStep, maxStep, null, null) {
            @Override
            public void handleEvent(double time, double[] value) {
                // Do nothing
            }
        };
        integrator.setEventsHandlersManager(manager);
        double[] y2 = new double[y0.length];
        integrator.integrate(equations, 0.0, y0, t1, y2);
        assertArrayEquals(new double[]{0.5, 1.1359862754822707}, y2, 1e-9);
        assertEquals(0.5, manager.getLastEventTime(), 1e-9);
        assertArrayEquals(new double[]{0.5, 1.5}, manager.getLastEventValues(), 1e-9);

        // Test the integration of a differential equation with different tolerance values
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, minStep, maxStep, new double[]{1e-10, 1e-9}, new double[]{1e-12, 1e-11});
        double t2 = 1.0;
        double[] y3 = {0.5, 1.0};
        double error2 = integrator.integrate(equations, 0.0, y0, t2, y3);
        assertEquals(0.0, error2, 1e-11);

        // Test the integration of a differential equation with a non-fsal method
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b, prototype, minStep, maxStep, null, null) {
            @Override
            public boolean isFSAL() {
                return false;
            }
        };
        double t3 = 1.0;
        double[] y4 = {0.5, 1.0};
        double error3 = integrator.integrate(equations, 0.0, y0, t3, y4);
        assertTrue(error3 > 0);

        // Test the integration of a differential equation with a fsal method
        integrator = new EmbeddedRungeKuttaIntegrator("test", true, c, a, b, prototype, minStep, maxStep, null, null);
        double t4 = 1.0;
        double[] y5 = {0.5, 1.0};
        double error4 = integrator.integrate(equations, 0.0, y0, t4, y5);
        assertEquals(0.0, error4, 1e-9);

        // Test the integration of a differential equation with a large step size
        integrator.setSafety(0.5);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(100.0);
        double t5 = 10.0;
        double[] y6 = {0.5, 1.0};
        double error5 = integrator.integrate(equations, 0.0, y0, t5, y6);
        assertTrue(error5 > 0);

        // Test the integration of a differential equation with a small step size
        integrator.setSafety(0.99);
        integrator.setMinReduction(0.01);
        integrator.setMaxGrowth(1.1);
        double t6 = 0.01;
        double[] y7 = {0.5, 1.0};
        double error6 = integrator.integrate(equations, 0.0, y0, t6, y7);
        assertEquals(0.0, error6, 1e-9);
    }

    /**
     * Custom event handler for testing.
     */
    private static class MyEvent implements StepHandler {
        @Override
        public void handleStep(StepHandlerContext context, int stepNumber) {
            // Do nothing
        }
    }

    /**
     * Custom event handler for testing.
     */
    private static class MyEvent2 implements StepHandler {
        @Override
        public void handleStep(StepHandlerContext context, int stepNumber) {
            // Do nothing
        }

        @Override
        public void handleEvent(double time, double[] value) {
            // Do nothing
        }
    }

}