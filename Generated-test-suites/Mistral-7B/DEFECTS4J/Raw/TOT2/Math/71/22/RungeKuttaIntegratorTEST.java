package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RelativeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Test for the {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test the simple behavior of the {@link RungeKuttaIntegrator}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimple() throws Exception {
        final double[] c = { 0.0, 0.5, 0.5, 1.0 };
        final double[][] a = { { 0.0, 0.5, 0.5 }, { 0.5, 0.5, 1.0 }, { 1.0, 0.0, 0.0 } };
        final double[] b = { 0.25, 0.25, 0.5 };
        final double step = 0.01;
        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestIntegrator", c, a, b, null, step) {
            @Override
            protected void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[1];
                derivatives[1] = -state[0] + Math.sin(time);
            }
        };

        final double initialTime = 0.0;
        final double[] initialState = { 1.0, 0.0 };
        final double finalTime = Math.PI;
        final double[] finalState = { 0.0, Math.sin(finalTime) };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return initialState.length;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                integrator.computeDerivatives(time, state, derivatives);
            }
        };

        assertArrayEquals(initialState, integrator.initialize(equations, initialTime, initialState));
        assertEquals(initialTime, integrator.getStateTime(), Precision.SAFE_DOUBLE_EPSILON);

        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
        assertEquals(finalTime, integrator.getStateTime(), Precision.SAFE_DOUBLE_EPSILON);
        assertArrayEquals(finalState, integrator.getStateVector(), Precision.SAFE_DOUBLE_EPSILON);

        // test with a simple step handler
        integrator = new RungeKuttaIntegrator("TestIntegrator", c, a, b, null, step) {
            @Override
            protected void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[1];
                derivatives[1] = -state[0] + Math.sin(time);
            }
        };
        integrator.addStepHandler((StepHandler) h -> h.handleStep(integrator, false));
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);

        // test exception handling
        assertThrows(MathIllegalArgumentException.class, () -> new RungeKuttaIntegrator("TestIntegrator", null, null, null, null, step));
        assertThrows(IllegalArgumentException.class, () -> new RungeKuttaIntegrator("TestIntegrator", c, null, b, null, step));
        assertThrows(IllegalArgumentException.class, () -> new RungeKuttaIntegrator("TestIntegrator", c, a, null, null, step));
        assertThrows(IllegalArgumentException.class, () -> new RungeKuttaIntegrator("TestIntegrator", c, a, b, null, -step));
        assertThrows(NumberIsTooSmallException.class, () -> new RungeKuttaIntegrator("TestIntegrator", c, a, b, null, 0.0));

        // test with an event
        final double eventTime = Math.PI / 2.0;
        final double eventValue = Math.sin(eventTime);
        final RelativeEvent event = new RelativeEvent(eventTime, integrator.getStepSize(), "TestEvent") {
            @Override
            public void handleEvent(double time, double[] state) {
                assertEquals(eventValue, state[0], Precision.SAFE_DOUBLE_EPSILON);
            }
        };
        integrator.addEvent(event);
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
        integrator.addStepHandler((StepHandler) h -> {
            assertEquals(integrator.getStepSize(), h.getStepSize(), Precision.SAFE_DOUBLE_EPSILON);
            assertEquals(integrator.getStep(), h.getStep(), Precision.SAFE_DOUBLE_EPSILON);
        });
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
    }

    /**
     * Test the behavior of the {@link RungeKuttaIntegrator} with a simple differential equation
     * with multiple solutions.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBessel() throws Exception {
        final double[] c = { 0.0, 0.5, 0.5, 1.0 };
        final double[][] a = { { 0.0, 0.5, 0.5 }, { 0.5, 0.5, 1.0 }, { 1.0, 0.0, 0.0 } };
        final double[] b = { 0.25, 0.25, 0.5 };
        final double step = 0.01;
        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestIntegrator", c, a, b, null, step) {
            @Override
            protected void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[1] - BesselFunctions.secondKind0(2, time) * BesselFunctions.secondKind0(2, time);
                derivatives[1] = state[0];
            }
        };

        final double initialTime = 0.0;
        final double[] initialState = { 1.0, 1.0 };
        final double finalTime = 2.0 * Math.PI;

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return initialState.length;
            }

            @Override
            public void computeDerivatives(double time, double[] state, double[] derivatives) {
                integrator.computeDerivatives(time, state, derivatives);
            }
        };

        integrator.integrate(equations, initialTime, initialState, finalTime, initialState);
        assertEquals(initialTime, integrator.getStateTime(), Precision.SAFE_DOUBLE_EPSILON);
        assertEquals(BesselFunctions.secondKind0(2, initialTime), initialState[0], Precision.SAFE_DOUBLE_EPSILON);
        assertEquals(BesselFunctions.secondKind1(2, initialTime), initialState[1], Precision.SAFE_DOUBLE_EPSILON);

        integrator = new RungeKuttaIntegrator("TestIntegrator", c, a, b, null, step) {
            @Override
            protected void computeDerivatives(double time, double[] state, double[] derivatives) {
                derivatives[0] = state[1] - BesselFunctions.secondKind0(2, time) * BesselFunctions.secondKind0(2, time);
                derivatives[1] = state[0];
            }
        };
        integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
        assertEquals(finalTime, integrator.getStateTime(), Precision.SAFE_DOUBLE_EPSILON);
        assertEquals(BesselFunctions.secondKind0(2, finalTime), finalState[0], Precision.SAFE_DOUBLE_EPSILON);
        assertEquals(BesselFunctions.secondKind1(2, finalTime), finalState[1], Precision.SAFE_DOUBLE_EPSILON);
    }
}