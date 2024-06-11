package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueComparator;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.IStepHandler;
import org.apache.commons.math3.ode.events.MockStepHandler;
import org.apache.commons.math3.ode.events.StepHandlerAdapter;
import org.apache.commons.math3.ode.events.ValueComparator;
import org.apache.commons.math3.ode.initialvalues.InitialValue;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** Dummy function for testing. */
    private static class DummyFunction implements UnivariateFunction {
        public double value(double x) {
            return Math.sin(x);
        }

        public double derivative(double x) {
            return Math.cos(x);
        }
    }

    /** Test for {@link EmbeddedRungeKuttaIntegrator#initializeStep}. */
    @org.junit.Test
    public void testInitializeStep() {
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbedgedRungeKutta23FehlbergIntegrator(
                3.0, new RungeKuttaStepInterpolator(23));
        final double[] y = new double[] { 1.0, 1.0 };
        final double[] yDot = new double[] { 1.0, 1.0 };
        final double stepSize = integrator.initializeStep(
            new DummyFunction(), 1.0, y, yDot, 2.0, 1.0e-10, 1.0e-10);
        double expected = 1.0;
        assertEquals(expected, stepSize, 1e-11);
        // Test if the integrator does not modify the input arrays.
        assertArrayEquals(y, new double[] { 1.0, 1.0 });
        assertArrayEquals(yDot, new double[] { 1.0, 1.0 });
    }

    /** Test for {@link EmbeddedRungeKuttaIntegrator#integrate}. */
    @org.junit.Test
    public void testIntegrate() throws Exception {
        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbedgedRungeKutta23FehlbergIntegrator(
                3.0, new RungeKuttaStepInterpolator(23));

        // Test with no events.
        final double[] y0 = new double[] { 1.0, 1.0 };
        final double[] y = new double[] { 0.0, 0.0 };
        final double tStart = 0.0;
        final double tEnd = 2.0 * Math.PI;
        final double[] yExpect = new double[] { 2.0 * Math.sin(Math.PI),
                                             2.0 * Math.cos(Math.PI) };
        final double relTolerance = 1.0e-9;
        final double absTolerance = 1.0e-10;
        final FirstOrderDifferentialEquations equations =
            new DummyFunction();
        final double[] result = integrator.integrate(equations, tStart, y0, tEnd, y);
        assertEquals(tEnd, result, 1e-12);
        assertArrayEquals(yExpect, y, relTolerance, absTolerance);

        // Test with events.
        final double[] y1 = new double[] { 2.0 * Math.sin(Math.PI),
                                          2.0 * Math.cos(Math.PI) };
        final double[] yExpectEvent = new double[] { 2.0 * Math.sin(Math.PI),
                                                   2.0 * Math.cos(Math.PI) };
        final double tEvent = Math.PI;
        final StepHandlerAdapter stepHandler = new MockStepHandler();
        final ValueComparator comparator = new DefaultValueComparator();
        final Event event = new Event(tEvent, new InitialValue(tEvent, y1),
                                      comparator);
        integrator.addStepHandler(event);
        integrator.setStepHandler(stepHandler);
        final double[] resultEvent = integrator.integrate(equations, tStart, y0, tEnd, y);
        assertEquals(tEnd, resultEvent, 1e-12);
        assertArrayEquals(yExpectEvent, y, relTolerance, absTolerance);
        assertTrue(stepHandler.isHandled());

        // Test with an error.
        final double[] y2 = new double[] { 1.0, 1.0 };
        final double tError = 1.0;
        final double[] yExpectError = new double[] { 1.0, 1.0 };
        final StepHandlerAdapter stepHandlerError = new MockStepHandler();
        integrator.addStepHandler(stepHandlerError);
        assertThrows(DerivativeException.class, () -> integrator.integrate(
            equations, tStart, y0, tError, yExpectError));
        assertFalse(stepHandlerError.isHandled());

        // Test with an IntegratorException.
        final double[] y3 = new double[] { 1.0, 1.0 };
        final double tException = Double.NaN;
        final StepHandlerAdapter stepHandlerException = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandler handler, int step) {
                throw new IntegratorException("Test exception");
            }
        };
        integrator.addStepHandler(stepHandlerException);
        assertThrows(IntegratorException.class, () -> integrator.integrate(
            equations, tStart, y0, tException, y3));
        assertFalse(stepHandlerException.isHandled());
    }

    /** Test for {@link EmbeddedRungeKuttaIntegrator#reset}. */
    @org.junit.Test
    public void testReset() {
        final EmbeddedRungeKutta23FehlbergIntegrator integrator =
            new EmbeddedRungeKutta23FehlbergIntegrator(
                3.0, new RungeKuttaStepInterpolator(23));
        final double[] y = new double[] { 1.0, 1.0 };
        integrator.reset();
        final double[] yReset = new double[y.length];
        integrator.integrate(new DummyFunction(), 0.0, yReset, 1.0, y);
        assertArrayEquals(y, new double[] { 1.0, 1.0 });
    }

    /** Test for {@link EmbeddedRungeKuttaIntegrator#setSafety}. */
    @org.junit.Test
    public void testSetSafety() {
        final EmbeddedRungeKutta23FehlbergIntegrator integrator =
            new EmbeddedRungeKutta23FehlbergIntegrator(
                3.0, new RungeKuttaStepInterpolator(23));
        integrator.setSafety(0.5);
    }

    /** Test for {@link EmbeddedRungeKuttaIntegrator#setMinReduction}. */
    @org.junit.Test
    public void testSetMinReduction() {
        final EmbeddedRungeKutta23FehlbergIntegrator integrator =
            new EmbeddedRungeKutta23FehlbergIntegrator(
                3.0, new RungeKuttaStepInterpolator(23));
        integrator.setMinReduction(0.2);
    }

    /** Test for {@link EmbeddedRungeKuttaIntegrator#setMaxGrowth}. */
    @org.junit.Test
    public void testSetMaxGrowth() {
        final EmbeddedRungeKutta23FehlbergIntegrator integrator =
            new EmbeddedRungeKutta23FehlbergIntegrator(
                3.0, new RungeKuttaStepInterpolator(23));
        integrator.setMaxGrowth(2.0);
    }

    /** Test for {@link EmbeddedRungeKuttaIntegrator#requiresDenseOutput}. */
    @org.junit.Test
    public void testRequiresDenseOutput() {
        final EmbeddedRungeKutta23FehlbergIntegrator integrator =
            new EmbeddedRungeKutta23FehlbergIntegrator(
                3.0, new RungeKuttaStepInterpolator(23));
        assertFalse(integrator.requiresDenseOutput());

        final EmbeddedRungeKuttaRk4Integrator integratorDense =
            new EmbeddedRungeKuttaRk4Integrator(
                4.0, new RungeKuttaStepInterpolator(4));
        assertTrue(integratorDense.requiresDenseOutput());
    }

}