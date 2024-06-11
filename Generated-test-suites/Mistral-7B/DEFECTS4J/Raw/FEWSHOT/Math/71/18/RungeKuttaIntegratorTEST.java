package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testConstructors() {
        final double[] c = {0.3, 0.4, 0.1};
        final double[][] a = {{0.0, 0.0}, {0.3, 0.4}, {0.6, 0.2}};
        final double[] b = {0.25, 0.25, 0.5};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 1.0);

        final RungeKuttaIntegrator integrator1 = new RungeKuttaIntegrator("Euler", c, a, b, prototype, 1.0);
        final RungeKuttaIntegrator integrator2 = new RungeKuttaIntegrator("Runge-Kutta23", c, a, b, prototype, 1.0);
        final RungeKuttaIntegrator integrator3 = new RungeKuttaIntegrator("Runge-Kutta45", c, a, b, prototype, 1.0);

        assertNotNull(integrator1);
        assertNotNull(integrator2);
        assertNotNull(integrator3);
    }

    @Test
    public void testIntegrateSimple() {
        final double[] c = {0.3, 0.4, 0.1};
        final double[][] a = {{0.0, 0.0}, {0.3, 0.4}, {0.6, 0.2}};
        final double[] b = {0.25, 0.25, 0.5};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 1.0);

        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Runge-Kutta23", c, a, b, prototype, 0.1);
        final FirstOrderDifferentialEquations equations = new AbstractDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = (y[0] * 0.1);
            }
        };

        final double[] initialState = {1.0};
        final double[] finalState = {1.31622776601683795320305992137};
        final double endTime = 1.05;

        double result = integrator.integrate(equations, 0.0, initialState, endTime, null);
        assertNotNull(result);
        assertEquals(endTime, result, 0.0);
        assertArrayEquals(finalState, initialState, 0.01);
    }

    @Test
    public void testIntegrateWithEvents() {
        final double[] c = {0.3, 0.4, 0.1};
        final double[][] a = {{0.0, 0.0}, {0.3, 0.4}, {0.6, 0.2}};
        final double[] b = {0.25, 0.25, 0.5};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3, 1.0);

        final RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Runge-Kutta23", c, a, b, prototype, 0.1);
        final FirstOrderDifferentialEquations equations = new AbstractDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = (y[0] * 0.1);
            }
        };

        final double[] initialState = {1.0};
        final double endTime = 2.0;

        StepInterpolator interpolator = integrator.createInterpolator();

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                if (interpolator.getLocalTime() > 1.5) {
                    interpolator.addEvent(new When(interpolator.getLocalTime(), () -> {
                        throw new AssertionError("Expected event to be triggered before this!");
                    }));
                }
            }
        });

        integrator.integrate(equations, 0.0, initialState, endTime, null);

        // The test fails if an AssertionError is not thrown during the test.
        // Since the test may fail due to random factors, the test is considered as a pass if
        // the AssertionError is not thrown.
        try {
            integrator.getLastEvent();
            fail("Expected an event to be triggered!");
        } catch (IntegratorException e) {
            // Expected exception
        }
    }

}