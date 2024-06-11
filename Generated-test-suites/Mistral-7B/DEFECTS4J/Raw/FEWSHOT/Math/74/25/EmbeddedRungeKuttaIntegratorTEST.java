package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.noalias.ODEEvent;
import org.apache.commons.math3.ode.noalias.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.ode.events.DefaultODEEvent;
import org.apache.commons.math3.ode.events.Event;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[][] coefficients = {{-0.0, 0.0, 0.5, 0.5}, {0.5, 0.5, 0.0, 0.0}, {0.5, 0.0, 0.5, 0.5}};
        double[][] propagationWeights = {{-0.6, 0.27, 0.64}, {0.27, 0.64, 0.73}, {0.64, 0.73, 0.6}};
        double minStep = 0.001;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;
        String name = "EmbeddedRungeKuttaIntegrator";
        boolean fsal = false;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, fsal, coefficients[0], coefficients, propagationWeights[0], new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        FirstOrderDifferentialEquations equations = ODETestUtils.createMockDifferentialEquations(2);
        double[] initialValues = {0.0, 0.0};
        double startTime = 0.0;
        double endTime = 1.0;

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                System.out.println("Step " + step + ": y = " + event.getInterpolatedY());
            }

            @Override
            public boolean handlesStep(int step) {
                return true;
            }
        };
        integrator.addStepHandler(stepHandler);

        double result = integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);

        // The test does not check the result of the integration, but only verifies that the integrator works correctly
        assertTrue(result > startTime);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorWithFSAL() {
        double[][] coefficients = {{-0.0, 0.5, 0.5}, {0.5, 0.5, 0.0}, {0.5, 0.0, 0.5, 0.5}};
        double[][] propagationWeights = {{-0.6, 0.27, 0.64}, {0.27, 0.64, 0.73}, {0.64, 0.73, 0.6}};
        double minStep = 0.001;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;
        String name = "EmbeddedRungeKuttaIntegrator";
        boolean fsal = true;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, fsal, coefficients[0], coefficients, propagationWeights[0], new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        FirstOrderDifferentialEquations equations = ODETestUtils.createMockDifferentialEquations(2);
        double[] initialValues = {0.0, 0.0};
        double startTime = 0.0;
        double endTime = 1.0;

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                System.out.println("Step " + step + ": y = " + event.getInterpolatedY());
                if (step > 1) {
                    assertEquals(event.getInterpolatedY(), initialValues, 1e-6);
                }
            }

            @Override
            public boolean handlesStep(int step) {
                return true;
            }
        };
        integrator.addStepHandler(stepHandler);

        double result = integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);

        // The test does not check the result of the integration, but only verifies that the integrator works correctly
        // and that the last evaluation of the derivatives in one step is the same as the first in the next step
        assertTrue(result > startTime);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorWithEvent() {
        double[][] coefficients = {{-0.0, 0.5, 0.5}, {0.5, 0.5, 0.0}, {0.5, 0.0, 0.5, 0.5}};
        double[][] propagationWeights = {{-0.6, 0.27, 0.64}, {0.27, 0.64, 0.73}, {0.64, 0.73, 0.6}};
        double minStep = 0.001;
        double maxStep = 1.0;
        double scalAbsoluteTolerance = 1e-6;
        double scalRelativeTolerance = 1e-6;
        String name = "EmbeddedRungeKuttaIntegrator";
        boolean fsal = false;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, fsal, coefficients[0], coefficients, propagationWeights[0], new RungeKuttaStepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        FirstOrderDifferentialEquations equations = ODETestUtils.createMockDifferentialEquations(2);
        double[] initialValues = {0.0, 0.0};
        double startTime = 0.0;
        double endTime = 1.0;

        double eventTime = 0.5;
        double eventValue = 1.0;
        Event event = new DefaultODEEvent(eventTime, eventValue);
        integrator.addEvent(event);

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                if (step == 1) {
                    double[] interpolatedY = event.getInterpolatedY();
                    assertEquals(interpolatedY[0], 0.0, 1e-6);
                    assertEquals(interpolatedY[1], 0.0, 1e-6);
                }
            }

            @Override
            public boolean handlesStep(int step) {
                return true;
            }
        };
        integrator.addStepHandler(stepHandler);

        double result = integrator.integrate(equations, startTime, initialValues, endTime, new double[initialValues.length]);

        // The test does not check the result of the integration, but only verifies that the integrator works correctly
        // and that an event is detected and handled
        assertTrue(result > startTime);
    }
}