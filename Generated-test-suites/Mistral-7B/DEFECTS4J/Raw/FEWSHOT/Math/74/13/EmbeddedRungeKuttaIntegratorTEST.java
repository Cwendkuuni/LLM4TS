package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.SimpleReleaseEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrator() {
        double[][] coefficients = new double[][]{{0.0, 0.5, 0.5, 1.0}, {0.0, 0.25, 0.5, 0.25, 0.5, 1.0}};
        double[] absoluteTolerance = {1.0e-6, 1.0e-6};
        double[] relativeTolerance = {1.0e-6, 1.0e-6};
        double minStep = 1.0e-6;
        double maxStep = 1.0e6;
        EmbeddedRungeKuttaIntegrator integrator1 = new EmbeddedRungeKuttaIntegrator("RK2", false, coefficients[0], null, coefficients[0], new DenseStepInterpolator(), minStep, maxStep, null, relativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (Math.abs(y1[0]) + Math.abs(y0[0]));
            }
        };
        EmbeddedRungeKuttaIntegrator integrator2 = new EmbeddedRungeKuttaIntegrator("RK4", true, coefficients[1], null, coefficients[1], new DenseStepInterpolator(), minStep, maxStep, absoluteTolerance, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialValues = {1.0, 1.0};
        double start = 0.0;
        double end = 10.0;
        StepHandler stepHandler = new StepCounter();

        integrator1.reset();
        double result1 = integrator1.integrate(equations, start, initialValues, end, null);
        integrator1.reset();
        integrator1.setStepSize(1.0);
        integrator1.addStepHandler(stepHandler);
        integrator1.integrate(equations, start, initialValues, end, null);
        assertEquals(11, stepHandler.getNumberOfSteps());

        integrator2.reset();
        double result2 = integrator2.integrate(equations, start, initialValues, end, null);
        integrator2.reset();
        integrator2.integrate(equations, start, initialValues, end, null);
        integrator2.addStepHandler(stepHandler);
        integrator2.integrate(equations, start, initialValues, end, null);
        assertEquals(11, stepHandler.getNumberOfSteps());

        integrator1.reset();
        integrator1.setStepSize(1.0e-6);
        integrator1.addStepHandler(new SimpleReleaseEvent(5.0));
        integrator1.integrate(equations, start, initialValues, end, null);
        assertEquals(6, stepHandler.getNumberOfSteps());

        integrator2.reset();
        integrator2.setStepSize(1.0e-6);
        integrator2.addStepHandler(new SimpleReleaseEvent(5.0));
        integrator2.integrate(equations, start, initialValues, end, null);
        assertEquals(6, stepHandler.getNumberOfSteps());
        assertEquals(result1, result2, 1.0e-6);
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[0];
    }

}

class StepCounter implements StepHandler {

    private int steps = 0;

    @Override
    public void handleStep(StepHandler event, int step) {
        steps++;
    }

    public int getNumberOfSteps() {
        return steps;
    }

}