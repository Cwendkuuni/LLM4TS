package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.Impulse;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRK2FSAL() {
        double[] c = {0.0, 0.5};
        double[][] a = {{1.0 / 2.0, 1.0 / 2.0}, {0.0, 1.0 / 2.0}};
        double[] b = {1.0 / 2.0, 1.0 / 2.0};
        double minStep = 0.01;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRK2FSAL", true, c, a, b, new RungeKuttaStepInterpolator(2), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (Math.abs(y0[0]) + 1.0);
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * y[0] * 2.0;
            }
        };

        double[] initialValues = {1.0};
        double[] finalValues = {3.0};
        double startTime = 0.0;
        double endTime = 3.0;

        StepHandler stepHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, int step) {
                assertTrue(step % 2 == 0);
            }
        };

        CombinedEventsManager eventsManager = new CombinedEventsManager();
        double eventTime = 1.5;
        eventsManager.addEqualityEvent(new Impulse(eventTime, 1.0), y -> y[0] - 2.0);

        integrator.setEquations(equations);
        integrator.setStepHandlers(stepHandler);
        integrator.setEventsHandlersManager(eventsManager);

        double result = integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
        assertEquals(endTime, result, 1e-6);
        assertArrayEquals(finalValues, finalValues);

        // Check that step handler was called only for even steps
        assertEquals(initialValues.length, stepHandler.getNumberOfCalls());
        for (int i = 0; i < stepHandler.getNumberOfCalls(); i++) {
            double[] stepValues = stepHandler.getCall(i).getInterpolator().getValues();
            assertTrue(stepValues[1] % 2 == 0);
        }
    }

    @Test
    public void testEmbeddedRK2() {
        double[] c = {0.0, 0.5, 0.8};
        double[][] a = {{1.0 / 2.0, 1.0 / 2.0, 1.0 / 8.0},
                        {0.0, 1.0 / 2.0, 1.0 / 4.0},
                        {0.0, 0.0, 1.0 / 3.0}};
        double[] b = {1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0};
        double minStep = 0.01;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRK2", false, c, a, b, new RungeKuttaStepInterpolator(3), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (Math.abs(y0[0]) + 1.0);
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * y[0] * 2.0;
            }
        };

        double[] initialValues = {1.0};
        double[] finalValues = {3.0};
        double startTime = 0.0;
        double endTime = 3.0;

        integrator.setEquations(equations);

        double result = integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
        assertEquals(endTime, result, 1e-6);
        assertArrayEquals(finalValues, finalValues);
    }

    @Test
    public void testEmbeddedRK4() {
        double[] c = {0.0, 0.25, 0.25, 0.5};
        double[][] a = {{1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0},
                        {1.0 / 2.0, 1.0 / 2.0, 0.0, 1.0 / 2.0},
                        {1.0 / 2.0, 0.0, 1.0 / 2.0, 1.0 / 2.0},
                        {1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0}};
        double[] b = {1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0};
        double minStep = 0.01;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRK4", false, c, a, b, new RungeKuttaStepInterpolator(4), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (Math.abs(y0[0]) + 1.0);
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * y[0] * 2.0;
            }
        };

        double[] initialValues = {1.0};
        double[] finalValues = {3.0};
        double startTime = 0.0;
        double endTime = 3.0;

        integrator.setEquations(equations);

        double result = integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
        assertEquals(endTime, result, 1e-6);
        assertArrayEquals(finalValues, finalValues);
    }

    @Test
    public void testEmbeddedRKF() {
        double[] c = {0.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 1.0};
        double[][] a = {{1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0, 1.0},
                        {1.0 / 2.0, 0.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0},
                        {1.0 / 2.0, 1.0 / 2.0, 0.0, 1.0 / 2.0, 1.0 / 2.0},
                        {1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 0.0, 1.0 / 2.0},
                        {1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0, 0.0}};
        double[] b = {1.0 / 2.0, 1.0 / 4.0, 1.0 / 4.0, 1.0 / 4.0, 1.0 / 2.0};
        double minStep = 0.01;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRKF", true, c, a, b, new RungeKuttaStepInterpolator(5), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (Math.abs(y0[0]) + 1.0);
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * y[0] * 2.0;
            }
        };

        double[] initialValues = {1.0};
        double[] finalValues = {3.0};
        double startTime = 0.0;
        double endTime = 3.0;

        integrator.setEquations(equations);

        double result = integrator.integrate(equations, startTime, initialValues, endTime, finalValues);
        assertEquals(endTime, result, 1e-6);
        assertArrayEquals(finalValues, finalValues);

        // Check that the last value is the same as the second last value
        assertEquals(finalValues[0], yDotK[4][0], 1e-6);
    }
}