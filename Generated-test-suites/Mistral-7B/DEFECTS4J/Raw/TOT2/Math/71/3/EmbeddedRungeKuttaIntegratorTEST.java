package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test the basic functionality of the {@link EmbeddedRungeKuttaIntegrator}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBasicFunctionality() throws Exception {
        final double[] y0 = { 1.0, 1.0 };
        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return y0.length;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    for (int i = 0; i < y.length; ++i) {
                        yPrime[i] = y[i] * (1.0 - y[i]);
                    }
                }
            };
        final double[][] expected = { { 1.0, 0.0 }, { 0.5, 0.25 } };
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] y1 = { 0.5, 0.25 };
        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        final double[] result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        assertArrayEquals(expected[0], y0, 0.0);
        assertArrayEquals(expected[1], result, 0.0);
    }

    /**
     * Test that the integrator correctly handles an event.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEventHandling() throws Exception {
        final double[] y0 = { 1.0, 1.0 };
        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return y0.length;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    for (int i = 0; i < y.length; ++i) {
                        yPrime[i] = y[i] * (1.0 - y[i]);
                    }
                }
            };
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] y1 = { 0.5, 0.25 };
        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        final double[] result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        assertArrayEquals(y0, y1, 0.0);
        final StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int step) {
                assertEquals(1.0, context.getTime(), 0.0);
                assertEquals(1.0, context.getY()[0], 0.0);
                assertEquals(1.0, context.getY()[1], 0.0);
            }
        };
        final Event event = new When(1.0);
        integrator.addStepHandler(event, handler);
        final double[] y2 = { 1.0, 1.0 };
        integrator.integrate(equations, t0, y0, t1 + 0.1, y2);
        assertEquals(1.0, y2[0], 0.0);
        assertEquals(1.0, y2[1], 0.0);
    }

    /**
     * Test that the integrator correctly handles an error.
     * @throws Exception if an error occurs
     */
    @Test(expected = IntegratorException.class)
    public void testErrorHandling() throws Exception {
        final double[] y0 = { 1.0, 1.0 };
        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return y0.length;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    for (int i = 0; i < y.length; ++i) {
                        yPrime[i] = -y[i] * y[i];
                    }
                }
            };
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] y1 = { 0.5, 0.25 };
        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator} with a
     * non-fsal method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testNonFsalMethod() throws Exception {
        final double[] y0 = { 1.0, 1.0 };
        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return y0.length;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    for (int i = 0; i < y.length; ++i) {
                        yPrime[i] = y[i] * (1.0 - y[i]);
                    }
                }
            };
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] y1 = { 0.5, 0.25 };
        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaCashKarpMethod();
        final double[] result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        assertArrayEquals(expected[0], y0, 0.0);
        assertArrayEquals(expected[1], result, 0.0);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator} with a
     * fsal method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testFsalMethod() throws Exception {
        final double[] y0 = { 1.0, 1.0 };
        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return y0.length;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    for (int i = 0; i < y.length; ++i) {
                        yPrime[i] = y[i] * (1.0 - y[i]);
                    }
                }
            };
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] y1 = { 0.5, 0.25 };
        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg23Method();
        final double[] result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        assertArrayEquals(expected[0], y0, 0.0);
        assertArrayEquals(expected[1], result, 0.0);
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator} with a
     * polynomial differential equation.
     * @throws Exception if an error occurs
     */
    @Test
    public void testPolynomialEquation() throws Exception {
        final double[] y0 = { 1.0 };
        final FirstOrderDifferentialEquations equations =
            new FirstOrderDifferentialEquations() {
                @Override
                public int getNumberOfEquations() {
                    return y0.length;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yPrime) {
                    yPrime[0] = PolynomialFunctions.exp(t) * y[0];
                }
            };
        final double t0 = 0.0;
        final double t1 = 1.0;
        final double[] y1 = { Math.E, Math.E };
        final EmbeddedRungeKuttaIntegrator integrator = new RungeKuttaFehlberg78Method();
        final double[] result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        assertArrayEquals(expected[0], y0, 0.0);
        assertArrayEquals(expected[1], result, 0.0);
    }

}