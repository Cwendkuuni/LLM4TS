package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.junit.Test;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.events.DefaultValueEventHandler;
import org.apache.commons.math.ode.events.Event;
import org.apache.commons.math.ode.events.GoalType;
import org.apache.commons.math.ode.events.RelativisticEvent;
import org.apache.commons.math.ode.events.ValueEventHandler;
import org.apache.commons.math.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math.ode.stephandler.StepHandler;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Create a simple differential equation
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 1;
            }

            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.exp(y[0]);
            }
        };

        // Create a Runge-Kutta integrator
        double[][] a = new double[][] {
            new double[] { 0.0, 0.0, 0.0, 0.5, 0.5 },
            new double[] { 0.0, 0.5, 0.5, 0.5, 0.0 },
            new double[] { 0.5, 0.5, 0.0, 0.0, 0.5 }
        };
        double[] c = new double[] { 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 };
        double[] b = new double[] { 1.0 / 3.0, 1.0 / 3.0, 1.0 };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, c, a, b, new RungeKuttaStepInterpolator(), 1e-10, 1e-10) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(yDotK[2][i] - y1[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        // Test the integrator with a simple initial condition
        double[] y0 = new double[] { 1.0 };
        double t0 = 0.0;
        double tEnd = 2.0;

        // Set up a value event handler
        ValueEventHandler valueEventHandler = new DefaultValueEventHandler("y", new double[] { 1.5 }, GoalType.REACH);

        // Set up a step handler
        StepHandler stepHandler = new StepHandler() {
            public void handleStep(StepHandlerContext context, int stepNumber) {
                assertTrue(context.isAccepted());
                System.out.println("Step number: " + stepNumber);
            }
        };

        // Integrate the differential equation
        try {
            double t = integrator.integrate(equations, t0, y0, tEnd, null);
            assertEquals(tEnd, t, 1e-10);
            assertArrayEquals(y0, equations.getValue(t), 1e-10);
            assertFalse(valueEventHandler.isTriggered());
            assertFalse(stepHandler.isTriggered());
        } catch (DerivativeException | IntegratorException e) {
            throw new RuntimeException(e);
        }

        // Integrate the differential equation with an event
        equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 1;
            }

            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.exp(y[0]);
                if (y[0] > 2.0) {
                    throw new RelativisticEvent(t, new Object());
                }
            }
        };
        valueEventHandler = new DefaultValueEventHandler("y", new double[] { 2.0 }, GoalType.EXCEED);
        stepHandler = new StepHandler() {
            public void handleStep(StepHandlerContext context, int stepNumber) {
                assertTrue(context.isRejected());
                System.out.println("Step number: " + stepNumber);
            }
        };
        try {
            integrator.integrate(equations, t0, y0, tEnd, stepHandler, valueEventHandler);
            fail("Expected an IntegratorException due to an event.");
        } catch (DerivativeException | IntegratorException e) {
            // expected
        } catch (MaxCountExceededException | PrecisionException e) {
            throw new RuntimeException(e);
        }

        // Integrate the differential equation with a very small step
        integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, c, a, b, new RungeKuttaStepInterpolator(), 1e-15, 1e-15) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(yDotK[2][i] - y1[i]), 2);
                }
                return Math.sqrt(error) * 1e-5; // very small error tolerance
            }
        };
        try {
            integrator.integrate(equations, t0, y0, tEnd, null);
            fail("Expected an IntegratorException due to a small step size.");
        } catch (DerivativeException | IntegratorException e) {
            // expected
        }

    }

}