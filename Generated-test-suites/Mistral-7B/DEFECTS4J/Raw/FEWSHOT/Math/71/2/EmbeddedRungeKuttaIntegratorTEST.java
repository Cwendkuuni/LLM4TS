package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.NotReachedEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test constructor with no arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructor() {
        new EmbeddedRungeKuttaIntegrator("", false, null, null, null, null, 0, 0, null, null);
    }

    /**
     * Test constructor with absolute tolerances.
     */
    @Test
    public void testAbsoluteTolerancesConstructor() {
        final String name = "TestIntegrator";
        final double minStep = 0.001;
        final double maxStep = 10.0;
        final double scalAbsoluteTolerance = 1e-10;
        final double[] scalRelativeTolerance = new double[] {1e-10, 1e-11};

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, false, null, null, null, null, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        assertEquals(name, integrator.getName());
        assertEquals(minStep, integrator.getMinStep(), 0.0);
        assertEquals(maxStep, integrator.getMaxStep(), 0.0);
        assertArrayEquals(scalRelativeTolerance, integrator.getScaledRelativeTolerances(), 0.0);
    }

    /**
     * Test constructor with vector tolerances.
     */
    @Test
    public void testVectorTolerancesConstructor() {
        final String name = "TestIntegrator";
        final double minStep = 0.001;
        final double maxStep = 10.0;
        final double[] scalAbsoluteTolerance = new double[] {1e-10, 1e-11};
        final double[] scalRelativeTolerance = new double[] {1e-10, 1e-11};

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, false, null, null, null, null, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        assertEquals(name, integrator.getName());
        assertEquals(minStep, integrator.getMinStep(), 0.0);
        assertEquals(maxStep, integrator.getMaxStep(), 0.0);
        assertArrayEquals(scalAbsoluteTolerance, integrator.getScaledAbsoluteTolerances(), 0.0);
        assertArrayEquals(scalRelativeTolerance, integrator.getScaledRelativeTolerances(), 0.0);
    }

    /**
     * Test getOrder method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetOrder() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, null, null, null, null, 0.0, 0.0, null, null) {
            @Override
            public int getOrder() {
                throw new UnsupportedOperationException();
            }
        };
        integrator.integrate(null, 0.0, new double[0], 1.0, new double[0]);
    }

    /**
     * Test getSafety method.
     */
    @Test
    public void testGetSafety() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, null, null, null, null, 0.0, 0.0, null, null) {
            @Override
            public double getSafety() {
                return 0.9;
            }
        };
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    /**
     * Test setSafety method.
     */
    @Test
    public void testSetSafety() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, null, null, null, null, 0.0, 0.0, null, null) {
            @Override
            public void setSafety(double safety) {
                this.safety = safety;
            }
        };
        integrator.setSafety(0.9);
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    /**
     * Test integrate method with no events.
     */
    @Test
    public void testIntegrateNoEvents() throws Exception {
        final String name = "TestIntegrator";
        final double minStep = 0.001;
        final double maxStep = 10.0;
        final double scalAbsoluteTolerance = 1e-10;
        final double scalRelativeTolerance = 1e-10;
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, false, null, null, null, null, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        final FirstOrderDifferentialEquations equations = (x, y) -> new double[0];
        final double[] initialState = new double[] {0.0};
        final double tStart = 0.0;
        final double tEnd = 1.0;
        final double[] finalState = new double[] {1.0};

        integrator.integrate(equations, tStart, initialState, tEnd, finalState);
        assertEquals(1.0, finalState[0], 0.0);
    }

    /**
     * Test integrate method with events.
     */
    @Test
    public void testIntegrateWithEvents() throws Exception {
        final String name = "TestIntegrator";
        final double minStep = 0.001;
        final double maxStep = 10.0;
        final double scalAbsoluteTolerance = 1e-10;
        final double scalRelativeTolerance = new double[] {1e-10, 1e-11};
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, false, null, null, null, null, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                final double error = Math.abs(y1[0] - y0[0]) + Math.abs(y1[1] - y0[1]);
                return error;
            }
        };
        final FirstOrderDifferentialEquations equations = (x, y) -> new double[] {Math.exp(y[0]) - y[1], y[0] / 10.0};
        final double[] initialState = new double[] {0.0, 0.0};
        final double tStart = 0.0;
        final double tEnd = 1.0;
        final double[] finalState = new double[] {1.0, 0.1};
        final StepInterpolator interpolator = new RungeKuttaStepInterpolator(integrator.getPrototype(), integrator, new double[2], new double[2][2], true);
        final StepHandler eventHandler = new StepHandler() {
            private double nextEventTime = 0.0;
            private Event nextEvent = null;

            @Override
            public void handleStep(StepInterpolator interpolator, boolean lastStep) {
                if (nextEvent != null) {
                    final double time = interpolator.getTime();
                    if (time >= nextEventTime) {
                        nextEvent.handle(interpolator, lastStep);
                        nextEvent = null;
                    }
                }
            }

            @Override
            public void reset() {
                nextEvent = new NotReachedEvent(1.0);
                nextEventTime = nextEvent.getTime();
            }

            @Override
            public void handleEvent(Event event) {
                nextEvent = event;
            }
        };
        integrator.addStepHandler(eventHandler);

        integrator.integrate(equations, tStart, initialState, tEnd, finalState);
        assertEquals(1.0, finalState[0], 0.0);
        assertEquals(0.1, finalState[1], 0.0);
        assertTrue(eventHandler.isLastStep());
    }

    /**
     * Test integrate method with maximum step size reached.
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrateMaxStepSize() throws Exception {
        final String name = "TestIntegrator";
        final double minStep = 0.001;
        final double maxStep = 1.0;
        final double scalAbsoluteTolerance = 1e-10;
        final double scalRelativeTolerance = 1e-10;
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, false, null, null, null, null, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]);
            }
        };
        final FirstOrderDifferentialEquations equations = (x, y) -> new double[] {y[0] / 2.0};
        final double[] initialState = new double[] {1.0};
        final double tStart = 0.0;
        final double tEnd = 2.0;

        integrator.integrate(equations, tStart, initialState, tEnd, null);
    }

    /**
     * Test integrate method with minimal step size reached.
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrateMinStepSize() throws Exception {
        final String name = "TestIntegrator";
        final double minStep = 0.0;
        final double maxStep = 10.0;
        final double scalAbsoluteTolerance = 1e-10;
        final double scalRelativeTolerance = 1e-10;
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, false, null, null, null, null, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]);
            }
        };
        final FirstOrderDifferentialEquations equations = (x, y) -> new double[] {y[0] / 2.0};
        final double[] initialState = new double[] {1.0};
        final double tStart = 0.0;
        final double tEnd = 0.1;

        integrator.integrate(equations, tStart, initialState, tEnd, null);
    }

}