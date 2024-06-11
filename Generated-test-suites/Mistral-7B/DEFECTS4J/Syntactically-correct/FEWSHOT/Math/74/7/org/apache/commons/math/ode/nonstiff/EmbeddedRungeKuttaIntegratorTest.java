package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Create a dummy differential equation
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };

        // Create an embedded Runge-Kutta integrator with a simple Butcher array
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK2", false,
                new double[] { 0.0, 0.5 }, new double[][] { { 0.0, 0.5 } },
                new double[] { 0.5, 0.5 }, new RungeKuttaStepInterpolator(),
                0.01, 0.02, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (h + 1e-20);
            }
        };

        // Test integration
        double[] y0 = new double[] { 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;
        double result = integrator.integrate(equations, t0, y0, t1, null);
        assertFalse(Double.isNaN(result));
        assertEquals(t1, result, 1e-12);
        assertArrayEquals(new double[] { 2.0 }, y0, 1e-12);

        // Test integration with events
        EventManager events = new EventManager();
        events.addEvent(t0 + 0.5, new DefaultValueEvent(1.5));
        integrator.addEventManager(events);
        result = integrator.integrate(equations, t0, y0, t1, null);
        assertEquals(t1, result, 1e-12);
        assertArrayEquals(new double[] { 1.5 }, y0, 1e-12);

        // Test step size control
        integrator = new EmbeddedRungeKuttaIntegrator("RK2", false,
                new double[] { 0.0, 0.5 }, new double[][] { { 0.0, 0.5 } },
                new double[] { 0.5, 0.5 }, new RungeKuttaStepInterpolator(),
                0.01, 0.02, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (h + 1e-20);
            }

            @Override
            public double getSafety() {
                return 0.5;
            }
        };
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);
        double[] y = new double[] { 1.0 };
        double[] yPrime = new double[y0.length];
        double t = 0.0;
        double hPrev = 0.01;
        double hNew = 0.0;
        double error = 0.0;
        while (t < t1) {
            integrator.computeDerivatives(t, y, yPrime);
            hNew = integrator.initializeStep(equations, true, getOrder(), null, t, y, yPrime, y, yPrime);
            error = integrator.estimateError(yDotK, y, y, hNew);
            if (error <= 1.0) {
                hPrev = hNew;
                integrator.doStep(equations, t, y, hNew);
                t += hNew;
            } else {
                hNew = Math.min(hNew * getSafety(), hPrev * getMinReduction());
            }
        }
        assertArrayEquals(new double[] { 2.0 }, y, 1e-12);
        assertEquals(t1, t, 1e-12);

    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorFSAL() {
        // Create a dummy differential equation
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };

        // Create an embedded Runge-Kutta integrator with a simple Butcher array (fsal)
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", true,
                new double[] { 0.0, 1.0 / 3.0, 2.0 / 3.0 }, new double[][] { { 0.0, 1.0 / 3.0, 2.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 } },
                new double[] { 1.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, new RungeKuttaStepInterpolator(),
                0.01, 0.02, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (h + 1e-20);
            }
        };

        // Test integration
        double[] y0 = new double[] { 1.0 };
        double t0 = 0.0;
        double t1 = 1.0;
        double result = integrator.integrate(equations, t0, y0, t1, null);
        assertFalse(Double.isNaN(result));
        assertEquals(t1, result, 1e-12);
        assertArrayEquals(new double[] { 3.0 }, y0, 1e-12);

        // Test integration with events
        EventManager events = new EventManager();
        events.addEvent(t0 + 0.5, new DefaultValueEvent(1.5));
        integrator.addEventManager(events);
        result = integrator.integrate(equations, t0, y0, t1, null);
        assertEquals(t1, result, 1e-12);
        assertArrayEquals(new double[] { 1.5 }, y0, 1e-12);

        // Test step size control
        integrator = new EmbeddedRungeKuttaIntegrator("RK3", true,
                new double[] { 0.0, 1.0 / 3.0, 2.0 / 3.0 }, new double[][] { { 0.0, 1.0 / 3.0, 2.0 / 3.0 }, { 1.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 } },
                new double[] { 1.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 }, new RungeKuttaStepInterpolator(),
                0.01, 0.02, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (h + 1e-20);
            }

            @Override
            public double getSafety() {
                return 0.5;
            }
        };
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);
        double[] y = new double[] { 1.0 };
        double[] yPrime = new double[y0.length];
        double t = 0.0;
        double hPrev = 0.01;
        double hNew = 0.0;
        double error = 0.0;
        while (t < t1) {
            integrator.computeDerivatives(t, y, yPrime);
            hNew = integrator.initializeStep(equations, true, getOrder(), null, t, y, yPrime, y, yPrime);
            error = integrator.estimateError(yDotK, y, y, hNew);
            if (error <= 1.0) {
                hPrev = hNew;
                integrator.doStep(equations, t, y, hNew);
                t += hNew;
            } else {
                hNew = Math.min(hNew * getSafety(), hPrev * getMinReduction());
            }
        }
        assertArrayEquals(new double[] { 3.0 }, y, 1e-12);
        assertEquals(t1, t, 1e-12);

    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorWithVectorTolerances() {
        // Create a dummy differential equation
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
                yPrime[1] = y[1] + 1;
            }
        };

        // Create an embedded Runge-Kutta integrator with a simple Butcher array
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK2", false,
                new double[] { 0.0, 0.5 }, new double[][] { { 0.0, 0.5 } },
                new double[] { 0.5, 0.5 }, new RungeKuttaStepInterpolator(),
                0.01, 0.02, new double[] { 1e-4, 1e-4 }, new double[] { 1e-4, 1e-4 }) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double errorSum = 0;
                for (int i = 0; i < y0.length; i++) {
                    errorSum += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(errorSum) / (h + 1e-20);
            }
        };

        // Test integration
        double[] y0 = new double[] { 1.0, 0.0 };
        double t0 = 0.0;
        double t1 = 1.0;
        double result = integrator.integrate(equations, t0, y0, t1, null);
        assertFalse(Double.isNaN(result));
        assertEquals(t1, result, 1e-12);
        assertArrayEquals(new double[] { 2.0, 1.0 }, y0, 1e-12);

        // Test integration with events
        EventManager events = new EventManager();
        events.addEvent(t0 + 0.5, new DefaultValueEvent(new double[] { 1.5, 1.0 }));
        integrator.addEventManager(events);
        result = integrator.integrate(equations, t0, y0, t1, null);
        assertEquals(t1, result, 1e-12);
        assertArrayEquals(new double[] { 1.5, 1.1 }, y0, 1e-12);

        // Test step size control
        integrator = new EmbeddedRungeKuttaIntegrator("RK2", false,
                new double[] { 0.0, 0.5 }, new double[][] { { 0.0, 0.5 } },
                new double[] { 0.5, 0.5 }, new RungeKuttaStepInterpolator(),
                0.01, 0.02, new double[] { 1e-4, 1e-4 }, new double[] { 1e-4, 1e-4 }) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double errorSum = 0;
                for (int i = 0; i < y0.length; i++) {
                    errorSum += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(errorSum) / (h + 1e-20);
            }

            @Override
            public double getSafety() {
                return 0.5;
            }
        };
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);
        double[] y = new double[] { 1.0, 0.0 };
        double[] yPrime = new double[y0.length];
        double t = 0.0;
        double hPrev = 0.01;
        double hNew = 0.0;
        double error = 0.0;
        while (t < t1) {
            integrator.computeDerivatives(t, y, yPrime);
            hNew = integrator.initializeStep(equations, true, getOrder(), null, t, y, yPrime, y, yPrime);
            error = integrator.estimateError(yDotK, y, y, hNew);
            if (error <= 1.0) {
                hPrev = hNew;
                integrator.doStep(equations, t, y, hNew);
                t += hNew;
            } else {
                hNew = Math.min(hNew * getSafety(), hPrev * getMinReduction());
            }
        }
        assertArrayEquals(new double[] { 2.0, 1.0 }, y, 1e-12);
        assertEquals(t1, t, 1e-12);

    }
}