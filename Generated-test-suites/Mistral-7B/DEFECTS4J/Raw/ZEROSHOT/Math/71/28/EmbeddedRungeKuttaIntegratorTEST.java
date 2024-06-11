package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.ODETestUtils;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for EmbeddedRungeKuttaIntegrator.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @org.junit.Test
    public void testIntegrate() {
        // Define a simple system of ODEs
        final double[][] coefficients = {
            {0.0, 1.0 / 4.0, 0.5},
            {0.0, 2.0 / 4.0, 1.0 / 2.0, 1.0 / 4.0},
            {0.0, 1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0, 1.0 / 6.0}};

        final double[][] internalWeights = {
            {1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0},
            {1.0 / 6.0, 1.2 / 3.0, 1.0 / 2.0, 1.0 / 3.0},
            {1.0 / 6.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 2.0, 1.0 / 6.0}};

        final double[] propagationWeights = {1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0};

        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                "RKF45", true, coefficients[0], coefficients[1], propagationWeights,
                new RungeKuttaStepInterpolator(), 1.0e-12, 1.0e-12) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1,
                                           double h) {
                // The error ratio is the maximum absolute error between the
                // estimated and the actual values, divided by the step size.
                double maxError = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    maxError = Math.max(maxError, Math.abs(y1[i] - y0[i]) / h);
                }
                return maxError;
            }
        };

        // Define a function to check the results of the integration
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(-t) * y[0];
            }
        };

        // Define a step handler to print some information
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int step) {
                System.out.println("Step " + step + " : t=" + context.getLocalTime()
                        + ", y=" + context.getLocalValue()[0]);
            }
        };

        // Perform the integration from t=0 to t=1
        final double[] y0 = new double[] {1.0};
        final double  t0  = 0.0;
        final double  t1  = 1.0;
        final double  stepSize = 1.0e-5;
        final double  toleranceRel = 1.0e-12;
        final double  toleranceAbs = 1.0e-12;
        double      t    = 0.0;
        double[]     y    = new double[1];
        double[]     yDot = new double[1];

        ODETestUtils.testIntegrator(equations, t0, y0, t1, stepSize,
                toleranceRel, toleranceAbs, integrator, stepHandler);

        // Check the results
        assertEquals("Final y value is incorrect.", 0.36787944117144233, y[0], toleranceAbs);

        // Define an event at t=2.5
        final Event event = new When(2.5);

        // Perform the integration from t=0 to t=3
        final EmbeddedRungeKuttaIntegrator integratorWithEvent = new EmbeddedRungeKuttaIntegrator(
                "RKF45", true, coefficients[0], coefficients[1], propagationWeights,
                new RungeKuttaStepInterpolator(), 1.0e-12, 1.0e-12, null, null) {
            @Override
            public void addStepHandler(StepHandler handler) {
                super.addStepHandler(handler);
                eventsHandlersManager.add(event);
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1,
                                           double h) {
                // The error ratio is the maximum absolute error between the
                // estimated and the actual values, divided by the step size.
                double maxError = 0.0;
                for (int i = 0; i < y0.length; ++i) {
                    maxError = Math.max(maxError, Math.abs(y1[i] - y0[i]) / h);
                }
                return maxError;
            }
        };

        ODETestUtils.testIntegrator(equations, t0, y0, t1, stepSize,
                toleranceRel, toleranceAbs, integratorWithEvent, stepHandler);

        // Check the results
        assertEquals("Step handler was not called.", 1, integratorWithEvent.getNumSteps());
        assertTrue("An event was not detected.", integratorWithEvent.getEventsDetected());
        assertEquals("Event time is incorrect.", 2.5, integratorWithEvent.getEventTime(),
                1.0e-12);
        assertEquals("Final y value is incorrect.", 0.0013533528323061385, y[0], toleranceAbs);
    }

}