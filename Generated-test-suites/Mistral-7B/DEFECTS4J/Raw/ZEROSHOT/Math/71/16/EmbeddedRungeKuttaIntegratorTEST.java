package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.EmbeddedRungeKuttaMethod;
import org.apache.commons.math3.transform.InterpolatingPolynomial;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * A test system for {@link EmbeddedRungeKuttaIntegrator}.
     */
    public static class TestIntegrator extends EmbeddedRungeKuttaIntegrator {
        private static final double TOL = 1.0e-16;

        /**
         * Constructor for TestIntegrator.
         */
        public TestIntegrator() {
            super("TestIntegrator", false, new double[] { 0.3, 0.47, 0.0781, 0.0152, 0.0 },
                 new double[][] { { 0.0, 0.3, 0.47, 0.7121, 0.8306 },
                                   { 0.3, 0.47, 0.6121, 0.8763, 0.9615 },
                                   { 0.47, 0.6121, 0.7826, 0.9413, 0.9934 },
                                   { 0.6121, 0.7826, 0.9037, 0.9721, 0.9973 },
                                   { 0.7812, 0.9037, 0.9615, 0.9913, 1.0 } },
                                 new double[] { 0.0, 0.2369, 0.6789, 0.8306, 0.9615, 0.9973 },
                 new RungeKuttaStepInterpolator(), 0.0001, 0.001, null, null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getOrder() {
            return 5;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            double error = 0.0;
            for (int i = 0; i < y0.length; i++) {
                error += Math.abs(y1[i] - y0[i]) * (1.0 + Math.abs(y1[i]));
            }
            return error;
        }
    }

    /**
     * A test system for {@link EmbeddedRungeKuttaIntegrator} with dense output.
     */
    public static class TestIntegratorDense extends EmbeddedRungeKuttaIntegrator {
        private static final double TOL = 1.0e-16;

        /**
         * Constructor for TestIntegrator.
         */
        public TestIntegratorDense() {
            super("TestIntegratorDense", false, new double[] { 0.3, 0.47, 0.0781, 0.0152, 0.0 },
                 new double[][] { { 0.0, 0.3, 0.47, 0.7121, 0.8306 },
                                   { 0.3, 0.47, 0.6121, 0.8763, 0.9615 },
                                   { 0.47, 0.6121, 0.7826, 0.9413, 0.9934 },
                                   { 0.6121, 0.7826, 0.9037, 0.9721, 0.9973 },
                                   { 0.7812, 0.9037, 0.9615, 0.9913, 1.0 } },
                 new double[] { 0.0, 0.2369, 0.6789, 0.8306, 0.9615, 0.9973 },
                 new RungeKuttaStepInterpolator(), 0.0001, 0.001, null, null);
            setRequiresDenseOutput(true);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getOrder() {
            return 5;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            double error = 0.0;
            for (int i = 0; i < y0.length; i++) {
                error += Math.abs(y1[i] - y0[i]) * (1.0 + Math.abs(y1[i]));
            }
            return error;
        }
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator}.
     */
    @org.junit.Test
    public void testIntegrator() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.exp(-t) * Math.sin(y[0]);
            }
        };

        double t0 = 0.0;
        double[] y0 = new double[] { 1.0 };

        TestIntegrator integrator = new TestIntegrator();

        double t = integrator.integrate(equations, t0, y0, 3.14, new double[y0.length]);
        assertEquals(3.14, t, TOL);

        double[] y = new double[y0.length];
        System.arraycopy(y0, 0, y, 0, y0.length);
        integrator.interpolate(t, y0, y);
        assertTrue(Math.abs(y[0] - Math.sin(3.14)) < TOL);

        // test with events
        final double t1 = 1.2;
        final double x1 = Math.PI / 2.0;
        final double t2 = 2.5;
        final double x2 = Math.PI;

        final EmbeddedRungeKuttaMethod method = (EmbeddedRungeKuttaMethod) integrator;
        method.setEventsHandlersManager(new CombinedEventsManager() {
            private InterpolatingPolynomial interpolator;

            @Override
            public void addStepHandler(StepHandler handler) {
                if (handler instanceof When) {
                    When when = (When) handler;
                    if (when.getT() == t1) {
                        when.setInterpolator(interpolator);
                    }
                }
            }

            @Override
            public boolean evaluateStep(StepHandler handler) {
                if (handler instanceof When) {
                    When when = (When) handler;
                    if (when.getT() == t1) {
                        interpolator = new InterpolatingPolynomial(y);
                        when.handleStep();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean stop() {
                return false;
            }
        });

        integrator.setMinStep(0.0001);
        integrator.setMaxStep(0.01);

        // integrate from 0 to t1 with event at t1
        integrator.integrate(equations, t0, y0, t1, y);
        assertTrue(Math.abs(y[0] - x1) < TOL);

        // check that the event handler has been called
        assertEquals(x1, interpolator.value(0.0), TOL);

        // test with events at t1 and t2
        final StepHandler handler1 = new StepHandler() {
            @Override
            public void handleStep(StepHandlerEvent event) {
                if (event.getStepNumber() == 1) {
                    assertEquals(t1, event.getStepTime(), TOL);
                    assertEquals(x1, InterpolatingPolynomial.interpolate(y, 0.0), TOL);
                }
            }
        };
        final StepHandler handler2 = new StepHandler() {
            @Override
            public void handleStep(StepHandlerEvent event) {
                if (event.getStepNumber() == 2) {
                    assertEquals(t2, event.getStepTime(), TOL);
                    assertEquals(x2, InterpolatingPolynomial.interpolate(y, 0.0), TOL);
                }
            }
        };
        method.addStepHandler(handler1);
        method.addStepHandler(handler2);

        // integrate from 0 to t2 with events at t1 and t2
        integrator.integrate(equations, t0, y0, t2, y);
        assertTrue(Math.abs(y[0] - x2) < TOL);

        // check that both event handlers have been called
        assertTrue(handler1.isHandled());
        assertTrue(handler2.isHandled());

        // test with dense output
        TestIntegratorDense integratorDense = new TestIntegratorDense();
        integratorDense.setMinStep(0.001);
        integratorDense.setMaxStep(0.05);

        firstOrderDenseTest(integratorDense, equations, t0, y0, t, y);

        // test with fsal method
        final double[] yDotK = new double[6][y0.length];
        integrator = new TestIntegrator();
        integrator.setMinStep(0.001);
        integrator.setMaxStep(0.05);
        integrator.setRequiresDenseOutput(false);

        integrator.integrate(equations, t0, y0, 3.14, yDotK);
        // the first value should be the same as the final value of the previous test
        assertTrue(Math.abs(y[0] - Math.sin(3.14)) < TOL);
        // the following values should be the error estimates
        for (int i = 1; i < yDotK[0].length; i++) {
            assertTrue(Math.abs(yDotK[0][i]) < TOL);
            assertTrue(Math.abs(yDotK[i][i]) < TOL);
            assertTrue(Math.abs(yDotK[i][i] - yDotK[i-1][i]) > TOL);
        }
    }

    /**
     * Tests the {@link EmbeddedRungeKuttaIntegrator} with dense output.
     * @param integrator the integrator to test
     * @param equations the differential equations to integrate
     * @param t0 the initial time
     * @param y0 the initial state
     * @param t the final time
     * @param y the final state
     */
    private void firstOrderDenseTest(EmbeddedRungeKuttaIntegrator integrator,
            FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) {
        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, integrator.getStepStart(), TOL);

        double[][] yDotK = integrator.getStateInterpolator().getInterpolatedValues();
        for (int i = 0; i < y.length; i++) {
            assertEquals(y[i], yDotK[0][i], TOL);
        }
    }

}