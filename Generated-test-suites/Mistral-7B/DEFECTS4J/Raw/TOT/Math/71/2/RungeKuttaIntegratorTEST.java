package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;

/**
 * Test the {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest extends TestCase {

    /**
     * Test the integration of a simple differential equation with no events.
     */
    public void testSimpleIntegration() {
        final double a = 1.0;
        final double b = 2.0;
        final double c = 3.0;
        final double x0 = 0.0;
        final double y0 = 0.0;
        final double t0 = 0.0;
        final double tEnd = 10.0;

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = a * y[0] + b;
            }
        };

        StepHandler handler = new StepHandlerAdapter() {
            private int counter = 0;

            @Override
            public void handleStep(StepHandler handler, int stepNumber) {
                if (stepNumber > 10) {
                    throw new AssertionError("Too many steps: " + stepNumber);
                }
                // print out some debug information
                System.out.println("Step " + stepNumber + " y = " + y[0]);
                counter++;
            }
        };

        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), 0.1);
        try {
            double result = integrator.integrate(equations, t0, new double[] {y0}, tEnd, new double[1]);
            assertTrue(Math.abs(result - 10.0 * (a + b) / 0.1) < 1e-8);
            assertEquals(11, handler.getNumberOfSteps());
        } finally {
            integrator.dispose();
        }
    }

    /**
     * Test the integration of a differential equation with an event.
     */
    public void testIntegrationWithEvent() {
        final double a = 1.0;
        final double b = 2.0;
        final double c = 3.0;
        final double x0 = 0.0;
        final double y0 = 0.0;
        final double t0 = 0.0;
        final double tEnd = 2.0;
        final double eventTime = 1.0;

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = a * y[0] + b;
            }
        };

        StepHandler handler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandler handler, int stepNumber) {
                throw new AssertionError("Should not be called after the event");
            }
        };

        RungeKuttaIntegrator integrator = new RungeKutta255Integrator(0.01, new RungeKutta255StepInterpolator(), 0.01);
        try {
            integrator.setEventsManager(new CombinedEventsManager());
            integrator.addEndTimeChecker(tEnd, new Event[] {new When(eventTime, new StepHandler() {
                @Override
                public void handleStep(StepHandler handler, int stepNumber, double[] y) {
                    System.out.println("Event detected at t = " + handler.getTime());
                }
            })});
            double result = integrator.integrate(equations, t0, new double[] {y0}, tEnd, new double[1]);
            fail("Integration should have stopped at event");
        } finally {
            integrator.dispose();
        }
    }

}