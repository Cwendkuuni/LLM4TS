package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator() {
        final double[] c = {0.0, 0.5, 0.5, 1.0};
        final double[][] a = {{0.0, 0.0}, {0.5, 0.5}, {0.5, 0.5}, {1.0, 0.0}};
        final double[] b = {0.25, 0.25, 0.5};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5);
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, prototype, 0.01, 0.01, null, null);
        assertEquals("RK4", integrator.getName());
        assertTrue(integrator.isFSAL());
        assertEquals(4, integrator.getOrder());
        assertEquals(0.01, integrator.getMinStep(), 1e-12);
        assertEquals(0.01, integrator.getMaxStep(), 1e-12);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#getOrder()}.
     */
    @org.junit.Test
    public void testGetOrder() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, null, null, null, null, 0.0, 0.0, null, null);
        assertEquals(5, integrator.getOrder());
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#getSafety()}.
     */
    @org.junit.Test
    public void testGetSafety() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, null, null, null, null, 0.0, 0.0, null, null);
        assertEquals(0.9, integrator.getSafety(), 1e-12);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#setSafety(double)}.
     */
    @org.junit.Test
    public void testSetSafety() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, null, null, null, null, 0.0, 0.0, null, null);
        integrator.setSafety(0.5);
        assertEquals(0.5, integrator.getSafety(), 1e-12);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#getMinReduction()}.
     */
    @org.junit.Test
    public void testGetMinReduction() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, null, null, null, null, 0.0, 0.0, null, null);
        assertEquals(0.2, integrator.getMinReduction(), 1e-12);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#setMinReduction(double)}.
     */
    @org.junit.Test
    public void testSetMinReduction() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, null, null, null, null, 0.0, 0.0, null, null);
        integrator.setMinReduction(0.5);
        assertEquals(0.5, integrator.getMinReduction(), 1e-12);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#getMaxGrowth()}.
     */
    @org.junit.Test
    public void testGetMaxGrowth() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, null, null, null, null, 0.0, 0.0, null, null);
        assertEquals(10.0, integrator.getMaxGrowth(), 1e-12);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#setMaxGrowth(double)}.
     */
    @org.junit.Test
    public void testSetMaxGrowth() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, null, null, null, null, 0.0, 0.0, null, null);
        integrator.setMaxGrowth(15.0);
        assertEquals(15.0, integrator.getMaxGrowth(), 1e-12);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @org.junit.Test
    public void testIntegrate() throws DeriveException, IntegratorException {
        final double[] c = {0.0, 0.5, 0.5, 1.0};
        final double[][] a = {{0.0, 0.0}, {0.5, 0.5}, {0.5, 0.5}, {1.0, 0.0}};
        final double[] b = {0.25, 0.25, 0.5};
        final double[] x = {0.0, 1.0, 2.0};
        final double[] y = {1.0, 1.0, 1.0};
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1.0 - y[0]);
            }
        };
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, new RungeKuttaStepInterpolator(5), 0.01, 0.01, null, null);

        for (int i = 0; i < x.length; ++i) {
            final double t = x[i];
            final double[] y0 = y.clone();
            final double[] y1 = y.clone();
            integrator.integrate(equations, 0.0, y0, t, y1);
            assertEquals(y1[0], Math.exp(t - 0.01), 1e-5);
        }
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[], StepHandler...)}.
     */
    @org.junit.Test
    public void testIntegrateWithStepHandler() throws DeriveException, IntegratorException {
        final double[] c = {0.0, 0.5, 0.5, 1.0};
        final double[][] a = {{0.0, 0.0}, {0.5, 0.5}, {0.5, 0.5}, {1.0, 0.0}};
        final double[] b = {0.25, 0.25, 0.5};

        final class MyStepHandler implements StepHandler {
            int count = 0;

            @Override
            public void handleStep(StepHandler handler, boolean success) {
                count++;
            }
        }

        final MyStepHandler stepHandler = new MyStepHandler();

        final double[] x = {0.0, 1.0, 2.0};
        final double[] y = {1.0, 1.0, 1.0};
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1.0 - y[0]);
            }
        };
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, new RungeKuttaStepInterpolator(5), 0.01, 0.01, null, null);

        for (int i = 0; i < x.length; ++i) {
            final double t = x[i];
            final double[] y0 = y.clone();
            integrator.integrate(equations, 0.0, y0, t, y, stepHandler);
            assertEquals(Math.exp(t - 0.01) / Math.exp(0.01), y[0], 1e-5);
            assertTrue(stepHandler.count >= 3); // 3 steps per call to integrate
        }
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[], StepHandler..., Event[])}.
     */
    @org.junit.Test
    public void testIntegrateWithEventHandler() throws DeriveException, IntegratorException {
        final double[] c = {0.0, 0.5, 0.5, 1.0};
        final double[][] a = {{0.0, 0.0}, {0.5, 0.5}, {0.5, 0.5}, {1.0, 0.0}};
        final double[] b = {0.25, 0.25, 0.5};

        final class MyEvent extends Event {
            final double time;

            MyEvent(double time) {
                super(time);
                this.time = time;
            }
        }

        final double[] x = {0.0, 1.0, 2.0};
        final double[] y = {1.0, 1.0, 1.0};
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1.0 - y[0]);
            }
        };
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, new RungeKuttaStepInterpolator(5), 0.01, 0.01, null, null);

        final Event[] events = new Event[] { new MyEvent(1.0) };

        for (int i = 0; i < x.length; ++i) {
            final double t = x[i];
            final double[] y0 = y.clone();
            integrator.integrate(equations, 0.0, y0, t, y, null, events);
            assertEquals(Math.exp(t - 0.01) / Math.exp(0.01), y[0], 1e-5);
            assertTrue(events[0].getEventTime() == t);
        }
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @org.junit.Test
    public void testIntegrateWithWhenEvent() throws DeriveException, IntegratorException {
        final double[] c = {0.0, 0.5, 0.5, 1.0};
        final double[][] a = {{0.0, 0.0}, {0.5, 0.5}, {0.5, 0.5}, {1.0, 0.0}};
        final double[] b = {0.25, 0.25, 0.5};

        final class MyEvent extends Event {
            final double yValue;

            MyEvent(double yValue) {
                super(Double.POSITIVE_INFINITY);
                this.yValue = yValue;
            }

            @Override
            public boolean isImmediate() {
                return false;
            }

            @Override
            public boolean isSatisfied(double t, double[] y) {
                return y[0] >= yValue;
            }
        }

        final double[] x = {0.0, 1.0, 2.0};
        final double[] y = {1.0, 1.0, 1.0};
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * (1.0 - y[0]);
            }
        };
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, c, a, b, new RungeKuttaStepInterpolator(5), 0.01, 0.01, null, null);

        integrator.addEvent(new When(1.5, new MyEvent(2.0)));

        for (int i = 0; i < x.length; ++i) {
            final double t = x[i];
            final double[] y0 = y.clone();
            integrator.integrate(equations, 0.0, y0, t, y);
            assertEquals(Math.exp(t - 0.01) / Math.exp(0.01), y[0], 1e-5);
            if (i == 1) {
                assertTrue(integrator.hasEvent());
                assertEquals(1.5, integrator.getLastEventTime(), 1e-8);
                assertEquals(2.0, integrator.getEventValue(), 1e-5);
            } else {
                assertFalse(integrator.hasEvent());
            }
        }
    }
}