package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test method for {@link RungeKuttaIntegrator#RungeKuttaIntegrator(String, double[], double[][], double[], RungeKuttaStepInterpolator, double)}.
     */
    @Test
    public void testRungeKuttaIntegrator() {
        double[] c = {0.3, 0.4, 0.1};
        double[][] a = {
            {0.0, 0.0},
            {0.6, 0.27},
            {0.25, 0.45},
        };
        double[] b = {0.25, 0.6};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c.length + 1, a, b);
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", c, a, b, prototype, 0.01);
        assertEquals("RK4", integrator.getName());
        assertThat(integrator.getC(), is(sameArrayAs(c)));
        assertThat(integrator.getA(), is(same2DArrayAs(a)));
        assertThat(integrator.getB(), is(sameArrayAs(b)));
        assertThat(integrator.getPrototype(), is(sameInstanceAs(prototype)));
        assertThat(integrator.getStep(), equalTo(0.01));
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() throws Exception {
        double[] c = {0.3, 0.4, 0.1};
        double[][] a = {
            {0.0, 0.0},
            {0.6, 0.27},
            {0.25, 0.45},
        };
        double[] b = {0.25, 0.6};
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c.length + 1, a, b);
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", c, a, b, prototype, 0.01);

        // initial values
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        double[] y1 = {1.1, 1.1};
        double t1 = 0.02;

        // test integration
        double stopTime = integrator.integrate(equations, t0, y0, t1, y1);
        assertEquals(t1, stopTime, 1e-12);
        assertEquals(y1[0], y0[0] + 0.01 * (0.6 * y0[1] + 0.27 * yDot(0.01, y0[0], y0[1])), 1e-12);
        assertEquals(y1[1], y0[1] + 0.01 * (0.25 * y0[1] + 0.45 * yDot(0.01, y0[0], y0[1])), 1e-12);

        // integration with events
        equations = new DummyDifferentialEquationsWithEvent();
        integrator = new RungeKuttaIntegrator("RK4", c, a, b, prototype, 0.01);
        double stopTime2 = integrator.integrate(equations, t0, y0, Double.POSITIVE_INFINITY, y1);
        assertEquals(t1, stopTime2, 1e-12);
        assertEquals(y1[0], y0[0] + 0.01 * (0.6 * y0[1] + 0.27 * yDot(0.01, y0[0], y0[1])), 1e-12);
        assertEquals(y1[1], y0[1] + 0.01 * (0.25 * y0[1] + 0.45 * yDot(0.01, y0[0], y0[1])), 1e-12);
        assertEquals(1, equations.getNumEvents(), 1);
        Event event = equations.getEvent(0);
        assertEquals(t1, event.getTime(), 1e-12);
    }

    /**
     * Helper method for computing derivatives for {@link DummyDifferentialEquations}.
     */
    private double[] yDot(double step, double... y) {
        double[] yDot = new double[y.length];
        for (int i = 0; i < y.length; ++i) {
            yDot[i] = y[i] * step;
        }
        return yDot;
    }

    /**
     * Dummy implementation of {@link FirstOrderDifferentialEquations}.
     */
    static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public double[] calculateDerivatives(double t, double[] y) throws DerivativeException {
            return yDot(stepSize, y[0], y[1]);
        }

    }

    /**
     * Dummy implementation of {@link FirstOrderDifferentialEquations} with an event.
     */
    static class DummyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

        private final DummyDifferentialEquations delegate = new DummyDifferentialEquations();
        private final When event = new When(0.02, y -> y[0] >= 1.1);

        @Override
        public int getDimension() {
            return delegate.getDimension();
        }

        @Override
        public double[] calculateDerivatives(double t, double[] y) throws DerivativeException {
            double[] yDot = delegate.calculateDerivatives(t, y);
            return yDot;
        }

        @Override
        public Event[] getEvents() {
            return new Event[] {event};
        }

    }

    /**
     * Asserts that two arrays are equal.
     */
    static void assertThat(double[] actual, double[] expected) {
        assertArrayEquals("Arrays do not match", expected, actual);
    }

    /**
     * Asserts that two 2D arrays are equal.
     */
    static void assertThat(double[][] actual, double[][] expected) {
        assertArrayEquals("2D Arrays do not match", expected, actual);
    }

    /**
     * Asserts that two objects are equal.
     */
    static void assertThat(Object actual, Object expected) {
        assertEquals("Objects do not match", expected, actual);
    }

    /**
     * Asserts that two arrays are equal up to floating point tolerance.
     */
    static void assertThat(double[] actual, double[] expected, double tolerance) {
        for (int i = 0; i < actual.length; ++i) {
            double delta = Math.abs(actual[i] - expected[i]);
            assertTrue("Arrays do not match: delta = " + delta, delta <= tolerance);
        }
    }

    /**
     * Asserts that two 2D arrays are equal up to floating point tolerance.
     */
    static void assertThat(double[][] actual, double[][] expected, double tolerance) {
        for (int i = 0; i < actual.length; ++i) {
            for (int j = 0; j < actual[i].length; ++j) {
                double delta = Math.abs(actual[i][j] - expected[i][j]);
                assertTrue("2D Arrays do not match: delta = " + delta, delta <= tolerance);
            }
        }
    }

    /**
     * Asserts that two objects are equal.
     */
    static void assertEquals(Object expected, Object actual) {
        assertEquals("Objects do not match", actual, expected);
    }

    /**
     * Asserts that two double values are equal.
     */
    static void assertEquals(double expected, double actual, double tolerance) {
        assertEquals("Values do not match: expected = " + expected + ", actual = " + actual, expected, actual, tolerance);
    }

    /**
     * Asserts that two arrays are equal.
     */
    static void assertEquals(double[] expected, double[] actual) {
        assertEquals("Arrays do not match", actual, expected);
    }

    /**
     * Asserts that two 2D arrays are equal.
     */
    static void assertEquals(double[][] expected, double[][] actual) {
        assertEquals("2D Arrays do not match", actual, expected);
    }

    /**
     * Asserts that two arrays are equal.
     */
    static void assertArrayEquals(String message, double[] expected, double[] actual) {
        assertArrayEquals(message, actual, expected);
    }

    /**
     * Asserts that two arrays are equal.
     */
    static void assertArrayEquals(String message, double[][] expected, double[][] actual) {
        assertEquals(message, actual.length, expected.length);
        for (int i = 0; i < actual.length; ++i) {
            assertEquals(message + "[" + i + "]", expected[i], actual[i]);
        }
    }

    /**
     * Asserts that two arrays are equal, up to floating point tolerance.
     */
    static void assertArrayEquals(String message, double[] expected, double[] actual, double tolerance) {
        for (int i = 0; i < expected.length; ++i) {
            double delta = Math.abs(expected[i] - actual[i]);
            assertTrue(message + "[" + i + "]: delta = " + delta, delta <= tolerance);
        }
    }

    /**
     * Asserts that two 2D arrays are equal, up to floating point tolerance.
     */
    static void assertArrayEquals(String message, double[][] expected, double[][] actual, double tolerance) {
        assertEquals(message, actual.length, expected.length);
        for (int i = 0; i < actual.length; ++i) {
            assertEquals(message + "[" + i + "]", expected[i], actual[i], tolerance);
        }
    }

    /**
     * Asserts that two objects are the same instance.
     */
    static void assertSameInstanceAs(Object expected, Object actual) {
        assertSame(expected, actual);
    }

    /**
     * Asserts that two arrays are the same.
     */
    static void assertSameArrayAs(double[] expected, double[] actual) {
        assertArrayEquals("Arrays do not match", expected, actual);
    }

    /**
     * Asserts that two 2D arrays are the same.
     */
    static void assertSame2DArrayAs(double[][] expected, double[][] actual) {
        assertArrayEquals("2D Arrays do not match", expected, actual);
    }

    /**
     * Helper method for computing derivatives for {@link DummyDifferentialEquations}.
     */
    static double[] yDot(double stepSize, double... y) {
        double[] yDot = new double[y.length];
        for (int i = 0; i < y.length; ++i) {
            yDot[i] = y[i] * stepSize;
        }
        return yDot;
    }

}