package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;

/**
 * Test case for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends EmbeddedRungeKuttaIntegratorTest {

    @Override
    public void testEmbeddedRungeKuttaIntegrator() {
        double[][] coefficients = {
            {0.0, 0.0},
            {0.25, 0.25},
            {0.5,  0.5},
            {0.875, 0.875}
        };
        double[] timeSteps = {0.25, 0.5, 0.875};
        double[][] internalWeights = {
            {0.5555555555555555d, 0.2777777777777778d},
            {0.25,                0.5d},
            {0.1666666666666667d, 0.3333333333333333d},
            {0.1111111111111111d, 0.1111111111111111d}
        };
        double[] propagationWeights = {0.5555555555555555d, 0.2777777777777778d};
        double minStep = 0.001;
        double maxStep = 10.0;
        double scalAbsoluteTolerance = 1e-8;
        double scalRelativeTolerance = 1e-6;
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "Runge-Kutta-Fehlberg-45",
                false,
                timeSteps,
                coefficients,
                propagationWeights,
                new RungeKuttaStepInterpolator(),
                minStep,
                maxStep,
                scalAbsoluteTolerance,
                scalRelativeTolerance
        ) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    double diff = y1[i] - y0[i];
                    error += diff * diff;
                }
                return Math.sqrt(error / (2.0 * y0.length));
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(y[0]) - 2.0;
            }
        };

        double[] initialValues = {0.0};
        double startTime = 0.0;
        double endTime = 3.0;
        double[] result = integrator.integrate(equations, startTime, initialValues, endTime, new double[1]);

        double expectedEndTime = 3.0;
        assertEquals(expectedEndTime, result[0], 1e-6);

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                MockEvent event = new MockEvent() {
                    @Override
                    public double getRelativeDistance(double t, double[] y) {
                        return Math.abs(y[0] - Math.PI / 2.0);
                    }

                    @Override
                    public boolean isActive(double t, double[] y) {
                        return getRelativeDistance(t, y) <= 0.2;
                    }

                    @Override
                    public void handle(double t, double[] y) {
                        System.out.println("Event at t=" + t + ", y=" + java.util.Arrays.toString(y));
                    }
                };
                integrator.addStepHandler(event);
            }
        };
        integrator.addStepHandler(stepHandler);

        initialValues = new double[] {Math.PI / 4.0};
        startTime = 0.0;
        endTime = Math.PI;
        result = integrator.integrate(equations, startTime, initialValues, endTime, new double[1]);
        double expectedEndTime = Math.PI;
        assertEquals(expectedEndTime, result[0], 1e-6);

        // check events
        assertEquals(1, stepHandler.getEventCount());
        assertEquals(Math.PI / 2.0, stepHandler.getLastEvent().getT(), 1e-6);
        double[] eventValues = stepHandler.getLastEvent().getValues();
        assertEquals(1, eventValues.length);
        assertEquals(Math.PI / 2.0, eventValues[0], 1e-6);

        // reset events
        stepHandler.reset();
        initialValues = new double[] {Math.PI};
        startTime = 0.0;
        endTime = 2 * Math.PI;
        result = integrator.integrate(equations, startTime, initialValues, endTime, new double[1]);
        expectedEndTime = 2 * Math.PI;
        assertEquals(expectedEndTime, result[0], 1e-6);
        assertEquals(0, stepHandler.getEventCount());
    }

}