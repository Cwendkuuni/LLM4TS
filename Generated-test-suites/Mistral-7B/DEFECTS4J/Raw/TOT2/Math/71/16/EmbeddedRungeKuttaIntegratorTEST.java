package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.ContinuousIntegratorEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.IntegratorEvent;
import org.apache.commons.math3.ode.events.LogEvent;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.test.DummyStepHandler;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math3.ode.nonstiff.RK23FE;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends AbstractODETest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        final double absTol = 1e-11;
        final double relTol = 1e-9;
        final double minStep = 1e-10;
        final double maxStep = 1e2;

        // Test RK23FE integrator
        final EmbeddedRungeKuttaIntegratorFactory factory =
            new EmbeddedRungeKuttaIntegratorFactory("RK23FE");
        final EmbeddedRungeKuttaIntegrator integrator =
            factory.createIntegrator(new RK23FE(new RungeKuttaStepInterpolator()),
                                    minStep, maxStep, absTol, relTol);

        // Test basic functionality
        final double[] y0 = { 1.0, 1.0 };
        final double[] y1 = { 1.5, 1.5 };
        final double tStart = 0.0;
        final double tEnd = 2.0;
        final double[] expected = { 1.98849654952134, 1.98849654952134 };
        final FirstOrderDifferentialEquations equations =
            new DummyDifferentialEquations();
        final double result = integrator.integrate(equations, tStart, y0, tEnd, y1);
        assertEquals(tEnd, result, absTol);
        assertArrayEquals(expected, y1, absTol);

        // Test exception handling
        final double[] y2 = { 1.0, 0.0 };
        try {
            integrator.integrate(equations, tStart, y2, tEnd, y1);
            fail("Expected IntegratorException");
        } catch (final IntegratorException e) {
            // expected
        }

        // Test log event handling
        final StepHandler eventHandler = new DummyStepHandler();
        final IntegratorEvent event = new LogEvent(1.0, 1.0, 1.0);
        final ContinuousIntegratorEvent logEvent =
            new ContinuousIntegratorEvent(event, 1.0);
        eventHandler.addEvent(logEvent);
        integrator.addStepHandler(eventHandler);
        final double[] y3 = { 1.0, 1.0 };
        final double[] y4 = { 1.1, 1.1 };
        final double tLogStart = 1.0;
        final double tLogEnd = 2.0;
        integrator.integrate(equations, tLogStart, y3, tLogEnd, y4);
        assertTrue(eventHandler.isHandled());
        assertEquals(1, eventHandler.getNumberOfEventsHandled());
        assertEquals(event, eventHandler.getEvent(0));

        // Test events handling
        final double[] y5 = { 1.0, 1.0 };
        final double[] y6 = { 2.0, 2.0 };
        final double[] y7 = { 3.0, 3.0 };
        final Event[] events = {
            new LogEvent(1.5, 1.5, 1.5),
            new LogEvent(2.0, 2.0, 2.0) };
        final double[] expectedY8 = { 1.99412096692144, 1.99412096692144 };
        final double[] expectedY9 = { 2.0, 2.0 };
        final double tEventStart = 1.0;
        final double tEventEnd = 3.0;
        eventHandler = new DummyStepHandler();
        integrator.setEventsHandler(eventHandler);
        integrator.integrate(equations, tStart, y5, tEnd, y6);
        assertTrue(eventHandler.isHandled());
        assertEquals(2, eventHandler.getNumberOfEventsHandled());
        assertEquals(events[0], eventHandler.getEvent(0));
        assertEquals(events[1], eventHandler.getEvent(1));
        assertArrayEquals(expectedY8, y6, absTol);
        assertArrayEquals(expectedY9, y7, absTol);

        // Test step size control
        final double[] y10 = { 1.0, 1.0 };
        final double[] y11 = { 1.1, 1.1 };
        final double[] y12 = { 1.2, 1.2 };
        final double t1Start = 0.0;
        final double t1End = 4.0;
        integrator = new EmbeddedRungeKuttaIntegrator(
          "RK23FE", false, new double[] { 0.23, 0.68, 0.68, 1.0 },
          new double[][] { { 0.23, 0.49, 0.87, 1.0 },
                           { 0.68, 0.87, 1.0, 1.0 } },
          new double[] { 0.23, 0.68 },
          new RungeKuttaStepInterpolator(), minStep, maxStep, absTol, relTol) {
            @Override
            protected double estimateError(double[][] yDotK,
                                           double[] y0, double[] y1,
                                           double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(y1[i] - y0[i]);
                }
                return error;
            }
        };
        integrator.setSafety(0.95);
        integrator.setMinReduction(0.4);
        integrator.setMaxGrowth(1.5);
        integrator.integrate(equations, t1Start, y10, t1End, y11);
        assertEquals(t1End, result, absTol);
        assertArrayEquals(expectedY8, y11, absTol);
        integrator.reset();
        integrator.integrate(equations, t1Start, y11, t1End, y12);
        assertFalse(Double.isNaN(result));
        assertTrue(Double.isFinite(result));
        assertArrayEquals(expectedY9, y12, absTol);

    }

    /**
     * Test double precision arithmetic.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorDouble() {
        final double absTol = 1e-11;
        final double relTol = 1e-9;
        final double minStep = 1e-10;
        final double maxStep = 1e2;

        // Test RK23FE integrator
        final EmbeddedRungeKuttaIntegratorFactory factory =
            new EmbeddedRungeKuttaIntegratorFactory("RK23FE");
        EmbeddedRungeKuttaIntegrator integrator = null;
        try {
            integrator = factory.createIntegrator(new RK23FE(new RungeKuttaStepInterpolator()),
                                                 minStep, maxStep, absTol, relTol);
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception");
        }

        // Test basic functionality
        final double[] y0 = { 1.0d, 1.0d };
        final double[] y1 = { 1.5d, 1.5d };
        final double tStart = 0.0d;
        final double tEnd = 2.0d;
        final double[] expected = { 1.98849654952134d, 1.98849654952134d };
        final FirstOrderDifferentialEquations equations =
            new DummyDifferentialEquations();
        final double result = integrator.integrate(equations, tStart, y0, tEnd, y1);
        assertEquals(tEnd, result, absTol);
        assertArrayEquals(expected, y1, absTol);

        // Test exception handling
        final double[] y2 = { 1.0d, 0.0d };
        try {
            integrator.integrate(equations, tStart, y2, tEnd, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        // Test log event handling
        final StepHandler eventHandler = new DummyStepHandler();
        final IntegratorEvent event = new LogEvent(1.0d, 1.0d, 1.0d);
        final ContinuousIntegratorEvent logEvent =
            new ContinuousIntegratorEvent(event, 1.0d);
        eventHandler.addEvent(logEvent);
        integrator.addStepHandler(eventHandler);
        final double[] y3 = { 1.0d, 1.0d };
        final double[] y4 = { 1.1d, 1.1d };
        final double tLogStart = 1.0d;
        final double tLogEnd = 2.0d;
        integrator.integrate(equations, tLogStart, y3, tLogEnd, y4);
        assertTrue(eventHandler.isHandled());
        assertEquals(1, eventHandler.getNumberOfEventsHandled());
        assertEquals(event, eventHandler.getEvent(0));

        // Test events handling
        final double[] y5 = { 1.0d, 1.0d };
        final double[] y6 = { 2.0d, 2.0d };
        final double[] y7 = { 3.0d, 3.0d };
        final Event[] events = {
            new LogEvent(1.5d, 1.5d, 1.5d),
            new LogEvent(2.0d, 2.0d, 2.0d) };
        final double[] expectedY8 = { 1.99412096692144d, 1.99412096692144d };
        final double[] expectedY9 = { 2.0d, 2.0d };
        final double tEventStart = 1.0d;
        final double tEventEnd = 3.0d;
        eventHandler = new DummyStepHandler();
        integrator.setEventsHandler(eventHandler);
        integrator.integrate(equations, tStart, y5, tEnd, y6);
        assertTrue(eventHandler.isHandled());
        assertEquals(2, eventHandler.getNumberOfEventsHandled());
        assertEquals(events[0], eventHandler.getEvent(0));
        assertEquals(events[1], eventHandler.getEvent(1));
        assertArrayEquals(expectedY8, y6, absTol);
        assertArrayEquals(expectedY9, y7, absTol);

        // Test step size control
        final double[] y10 = { 1.0d, 1.0d };
        final double[] y11 = { 1.1d, 1.1d };
        final double[] y12 = { 1.2d, 1.2d };
        final double t1Start = 0.0d;
        final double t1End = 4.0d;
        integrator = new EmbeddedRungeKuttaIntegrator(
          "RK23FE", false, new double[] { 0.23d, 0.68d, 0.68d, 1.0d },
          new double[][] { { 0.23d, 0.49d, 0.87d, 1.0d },
                           { 0.68d, 0.87d, 1.0d, 1.0d } },
          new double[] { 0.23d, 0.68d },
          new RungeKuttaStepInterpolator(), minStep, maxStep, absTol, relTol) {
            @Override
            protected double estimateError(double[][] yDotK,
                                           double[] y0, double[] y1,
                                           double h) {
                double error = 0.0d;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(y1[i] - y0[i]);
                }
                return error;
            }
        };
        integrator.setSafety(0.95d);
        integrator.setMinReduction(0.4d);
        integrator.setMaxGrowth(1.5d);
        integrator.integrate(equations, t1Start, y10, t1End, y11);
        assertEquals(t1End, result, absTol);
        assertArrayEquals(expectedY8, y11, absTol);
        integrator.reset();
        integrator.integrate(equations, t1Start, y11, t1End, y12);
        assertFalse(Double.isNaN(result));
        assertTrue(Double.isFinite(result));
        assertArrayEquals(expectedY9, y12, absTol);
    }

}