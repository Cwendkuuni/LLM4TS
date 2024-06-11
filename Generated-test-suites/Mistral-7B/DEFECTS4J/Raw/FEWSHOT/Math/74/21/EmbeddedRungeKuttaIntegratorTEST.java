package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.junit.Test;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.events.DefaultValueHolder;
import org.apache.commons.math.ode.events.Event;
import org.apache.commons.math.ode.events.MaxStepSizeReachedEvent;
import org.apache.commons.math.ode.events.RelativisticEvent;
import org.apache.commons.math.ode.sampling.RungeKuttaStepInterpolator;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testErrorEstimation() throws Exception {
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {{0.0, 0.5, 0.5}, {0.0, 0.5, 1.0}, {0.0, 1.0, 1.0}};
        double[] b = {0.0, 0.5, 1.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "MyEmbeddedRungeKutta", true, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double x = y0[0];
                double y = y0[1];

                // simple test case: error grows exponentially
                double error = Math.exp(h) - 1.0;
                return error;
            }
        };

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialValues = {0.0, 1.0};
        double startTime = 0.0;
        double endTime = 10.0;

        double step = integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);
        assertEquals(endTime, step, 1e-10);

        double[] expectedValues = {1.0, Math.exp(1.0)};
        assertArrayEquals(expectedValues, initialValues, 1e-10);

        // test error estimation on the same interval
        double[] y = new double[initialValues.length];
        System.arraycopy(initialValues, 0, y, 0, initialValues.length);
        step = integrator.integrate(equations, startTime, y, endTime, y);
        assertEquals(endTime, step, 1e-10);

        expectedValues = new double[] {Math.exp(1.0), Math.exp(Math.exp(1.0))};
        assertArrayEquals(expectedValues, y, 1e-10);

        double error = integrator.getInternalErrorEstimate();
        assertTrue(error > 1.0);
    }

    @Test
    public void testErrorEstimationRejectStep() throws Exception {
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {{0.0, 0.5, 0.5}, {0.0, 0.5, 1.0}, {0.0, 1.0, 1.0}};
        double[] b = {0.0, 0.5, 1.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "MyEmbeddedRungeKutta", true, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double x = y0[0];
                double y = y0[1];

                // error grows too much, step should be rejected
                double error = Math.exp(h) - Math.exp(1.0);
                return error;
            }
        };

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialValues = {0.0, 1.0};
        double startTime = 0.0;
        double endTime = 10.0;

        integrator.setMinReduction(0.5);
        integrator.setMaxGrowth(2.0);

        double step = integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);
        assertEquals(endTime, step, 1e-10);

        double[] expectedValues = {1.0, Math.exp(1.0)};
        assertArrayEquals(expectedValues, initialValues, 1e-10);

        // test error estimation on the same interval, step should be rejected
        double[] y = new double[initialValues.length];
        System.arraycopy(initialValues, 0, y, 0, initialValues.length);
        step = integrator.integrate(equations, startTime, y, endTime, y);
        assertEquals(endTime, step, 1e-10);

        expectedValues = new double[] {Math.exp(1.0), Math.exp(Math.exp(1.0))};
        assertArrayEquals(expectedValues, y, 1e-10);

        double error = integrator.getInternalErrorEstimate();
        assertTrue(error > 1.0);

        assertFalse(integrator.isLastStepAccepted());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {{0.0, 0.5, 0.5}, {0.0, 0.5, 1.0}, {0.0, 1.0, 1.0}};
        double[] b = {0.0, 0.5, 1.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "MyEmbeddedRungeKutta", true, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new TooManyIterationsException();
            }
        };

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialValues = {0.0, 1.0};
        double startTime = 0.0;
        double endTime = 10.0;

        integrator.setMaxIterations(1);
        integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallStep() throws Exception {
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {{0.0, 0.5, 0.5}, {0.0, 0.5, 1.0}, {0.0, 1.0, 1.0}};
        double[] b = {0.0, 0.5, 1.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "MyEmbeddedRungeKutta", true, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return h;
            }
        };

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialValues = {0.0, 1.0};
        double startTime = 0.0;
        double endTime = 10.0;

        integrator.setMinStep(0.0001);
        integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);
    }

    @Test
    public void testEvents() throws Exception {
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {{0.0, 0.5, 0.5}, {0.0, 0.5, 1.0}, {0.0, 1.0, 1.0}};
        double[] b = {0.0, 0.5, 1.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "MyEmbeddedRungeKutta", true, c, a, b,
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0.0;
            }
        };

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1.0;
            }

            @Override
            public Event getEvent(double t, double[] y) {
                if (y[0] > 10.0) {
                    return new MaxStepSizeReachedEvent(t, 1.0);
                }
                return new RelativisticEvent(t, new DefaultValueHolder(y[0]));
            }
        };

        double[] initialValues = {0.0};
        double startTime = 0.0;
        double endTime = 11.0;

        integrator.addStepHandler((handler, lastStep) -> {
            if (lastStep) {
                System.out.println("Last step accepted");
            } else {
                System.out.println("Last step rejected");
            }
        });

        integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        for (int i = 0; i < y.length; i++) {
            yDot[i] = y[i] + 1.0;
        }
    }

    @Override
    public Event getEvent(double t, double[] y) {
        return null;
    }
}