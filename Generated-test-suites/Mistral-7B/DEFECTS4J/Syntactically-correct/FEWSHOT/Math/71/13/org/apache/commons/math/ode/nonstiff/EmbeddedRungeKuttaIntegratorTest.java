package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.noark47.Noark47DifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.events.Event;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Test the default constructor
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] { 0.25, 0.25, 0.5, 0.25 },
                new double[][] { { 0.25, 0 }, { 0.4444444444444444, 0.25 },
                                  { 0.6923076923076923, 0.4444444444444444 },
                                  { 0.8467916984679169, 0.6923076923076923 } },
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                (RungeKuttaStepInterpolator) new RungeKuttaStepInterpolator(1.0, 1.0));
        assertEquals(1.0, integrator.getOrder());
        assertArrayEquals(new double[] { 0.25, 0.25, 0.5, 0.25 }, integrator.c, 0.0);
        assertArrayEquals(new double[][] { { 0.25, 0 }, { 0.4444444444444444, 0.25 },
                                             { 0.6923076923076923, 0.4444444444444444 },
                                             { 0.8467916984679169, 0.6923076923076923 } }, integrator.a, 0.0);
        assertArrayEquals(new double[] { 0.25, 0.25, 0.25, 0.25 }, integrator.b, 0.0);
        assertNull(integrator.prototype);
        assertEquals(0.9, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);

        // Test the constructor with custom minStep, maxStep, scalAbsoluteTolerance, and scalRelativeTolerance
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] { 0.25, 0.25, 0.5, 0.25 },
                new double[][] { { 0.25, 0 }, { 0.4444444444444444, 0.25 },
                                  { 0.6923076923076923, 0.4444444444444444 },
                                  { 0.8467916984679169, 0.6923076923076923 } },
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[] { 1.0e-10, 1.0e-10 },
                new double[] { 1.0e-12, 1.0e-12 },
                (RungeKuttaStepInterpolator) new RungeKuttaStepInterpolator(1.0, 1.0),
                0.001, 100.0,
                new double[] { 1.0e-13, 1.0e-13 });
        assertEquals(1.0, integrator.getOrder());
        assertArrayEquals(new double[] { 0.25, 0.25, 0.5, 0.25 }, integrator.c, 0.0);
        assertArrayEquals(new double[][] { { 0.25, 0 }, { 0.4444444444444444, 0.25 },
                                             { 0.6923076923076923, 0.4444444444444444 },
                                             { 0.8467916984679169, 0.6923076923076923 } }, integrator.a, 0.0);
        assertArrayEquals(new double[] { 0.25, 0.25, 0.25, 0.25 }, integrator.b, 0.0);
        assertNotNull(integrator.prototype);
        assertEquals(0.9, integrator.getSafety(), 0.0);
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
        assertEquals(1.0e-10, integrator.getScalAbsoluteTolerance(), 0.0);
        assertEquals(1.0e-12, integrator.getScalRelativeTolerance(), 0.0);
        assertArrayEquals(new double[] { 1.0e-13, 1.0e-13 }, integrator.vecRelativeTolerance, 0.0);

        // Test the getSafety(), setSafety(), getMinReduction(), and setMinReduction() methods
        integrator.setSafety(0.7);
        assertEquals(0.7, integrator.getSafety(), 0.0);
        integrator.setMinReduction(0.5);
        assertEquals(0.5, integrator.getMinReduction(), 0.0);

        // Test the estimateError() method with a dummy differential equations implementation
        FirstOrderDifferentialEquations equations = new Noark47DifferentialEquations() {
            @Override
            public void derive(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        };
        double[] y0 = new double[] { 1.0, 0.0 };
        double[] y1 = new double[] { 0.0, 1.0 };
        double h = 0.1;
        double error = integrator.estimateError(equations, y0, y1, h);
        assertTrue(error > 1.0);

        // Test the integrate() method with a simple differential equations implementation
        equations = new Noark47DifferentialEquations() {
            @Override
            public void derive(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        };
        double t0 = 0.0;
        double[] y0_orig = new double[] { 1.0, 0.0 };
        double[] y = new double[] { 1.0, 0.0 };
        double t1 = 1.0;
        double[] y1 = new double[] { 0.0, 1.0 };
        Event[] events = new Event[] { new org.apache.commons.math3.ode.events.StepEvent(1.0) };
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                System.out.println("Step " + stepNumber);
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.setEventsHandlersManager(new CombinedEventsManager(events));
        try {
            double stopTime = integrator.integrate(equations, t0, y0_orig, t1, y);
            assertEquals(t1, stopTime, 1.0e-12);
            assertEquals(y1[0], y[0], 1.0e-12);
            assertEquals(y1[1], y[1], 1.0e-12);
            assertEquals(1, stepHandler.getCallCount());
            assertEquals(1, stepHandler.getLastStepNumber());
        } catch (DerivativeException e) {
            e.printStackTrace();
            fail("Integration should not have thrown an exception");
        }

        // Test the integrate() method with a differential equations implementation that throws an exception
        equations = new Noark47DifferentialEquations() {
            @Override
            public void derive(double t, double[] y, double[] dy) {
                throw new RuntimeException("Exception");
            }
        };
        try {
            integrator.integrate(equations, t0, y0_orig, t1, y);
            fail("Integration should have thrown an exception");
        } catch (IntegratorException e) {
            assertEquals("Exception in derivative evaluation at t = " + t0, e.getMessage());
        }

        // Test the integrate() method with a differential equations implementation that has an event at t1
        equations = new Noark47DifferentialEquations() {
            @Override
            public void derive(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }

            @Override
            public boolean equals(double t, double[] y, double[] dy) {
                return (y[0] == 0.0 && y[1] == 1.0) || (y[0] == 1.0 && y[1] == 0.0);
            }

            @Override
            public Event[] getEvents(double t, double[] y, double[] dy) {
                if (y[0] == 0.0 && y[1] == 1.0) {
                    return new Event[] { new org.apache.commons.math3.ode.events.StepEvent(t) };
                }
                return new Event[0];
            }
        };
        try {
            integrator.integrate(equations, t0, y0_orig, t1, y);
            fail("Integration should have stopped at an event");
        } catch (IntegratorException e) {
            // expected
        }
    }
}