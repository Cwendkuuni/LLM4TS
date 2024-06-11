package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumericalMethodConvergenceException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativisticEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @Test
    public void testIntegrate() {
        // Test with a simple equation
        double[] y0 = new double[] { 0.0 };
        FirstOrderDifferentialEquations equations = new SimpleDifferentialEquations();
        double t0 = 0.0;
        double t = 2.0;
        double[] y = new double[] { 1.0 };
        try {
            double result = new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.25, 0.25 },
                    new double[][] { { 0.25, 0 }, { 0.25, 0.25 }, { 0.25, 0.25 }, { 0.25, 0.25 } },
                    new double[] { 0.5, 0.5, 0.5, 0.5 }, new RungeKuttaStepInterpolator(), 0.01)
                    .integrate(equations, t0, y0, t, y);
            assertEquals(y[0], result, 1e-12);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a differential equation with a singularity
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int step) {
                if (interpolator.getTime() <= 1.0) {
                    throw new SingularMatrixException("Singular matrix");
                }
            }
        };
        try {
            RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.25, 0.25 },
                    new double[][] { { 0.25, 0 }, { 0.25, 0.25 }, { 0.25, 0.25 }, { 0.25, 0.25 } },
                    new double[] { 0.5, 0.5, 0.5, 0.5 }, new RungeKuttaStepInterpolator(), 0.01);
            integrator.addStepHandler(stepHandler);
            double[] y0_sing = new double[] { 1.0 };
            double t0_sing = 0.0;
            double t_sing = 1.1;
            double[] y_sing = new double[] { 1.1 };
            try {
                integrator.integrate(new SingularDifferentialEquations(), t0_sing, y0_sing, t_sing, y_sing);
                fail("Expected SingularMatrixException");
            } catch (SingularMatrixException e) {
                // expected
            }
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a differential equation with a relativistic event
        FirstOrderDifferentialEquations relativisticEquations = new RelativisticEquations();
        double t0_rel = 0.0;
        double t_rel = 2.0;
        double[] y0_rel = new double[] { 1.0, 1.0 };
        double[] y_rel = new double[] { 1.2, 1.1 };
        try {
            RungeKuttaIntegrator integrator_rel = new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.25, 0.25 },
                    new double[][] { { 0.25, 0 }, { 0.25, 0.25 }, { 0.25, 0.25 }, { 0.25, 0.25 } },
                    new double[] { 0.5, 0.5, 0.5, 0.5 }, new RungeKuttaStepInterpolator(), 0.01);
            integrator_rel.setEventsManager(new CombinedEventsManager());
            RungeKuttaIntegrator.RelativisticEvent relativisticEvent = new RungeKuttaIntegrator.RelativisticEvent(1.5);
            integrator_rel.addEvent(relativisticEvent);
            integrator_rel.integrate(relativisticEquations, t0_rel, y0_rel, t_rel, y_rel);
            assertEquals(y[0], y_rel[0], 1e-12);
            assertEquals(y[1], y_rel[1], 1e-12);
        } catch (DerivativeException | IntegratorException | NoBracketingException | MaxIterationsExceededException | NumericalMethodConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a differential equation with a limit cycle
        FirstOrderDifferentialEquations limitCycleEquations = new LimitCycleEquations();
        double t0_cycle = 0.0;
        double t_cycle = 4.0 * Math.PI;
        double[] y0_cycle = new double[] { 1.0 };
        double[] y_cycle = new double[] { 1.0 };
        try {
            RungeKuttaIntegrator integrator_cycle = new RungeKuttaIntegrator("RK4", new double[] { 0.25, 0.25, 0.25, 0.25 },
                    new double[][] { { 0.25, 0 }, { 0.25, 0.25 }, { 0.25, 0.25 }, { 0.25, 0.25 } },
                    new double[] { 0.5, 0.5, 0.5, 0.5 }, new RungeKuttaStepInterpolator(), 0.01);
            integrator_cycle.integrate(limitCycleEquations, t0_cycle, y0_cycle, t_cycle, y_cycle);
            assertEquals(y[0], y_cycle[0], 1e-12);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}