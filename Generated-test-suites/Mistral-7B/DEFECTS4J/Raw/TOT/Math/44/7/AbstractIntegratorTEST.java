package org.apache.commons.math.ode.tests;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.apache.commons.math.ode.sampling.StepInterpolator;

/**
 * Test suite for the AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    /**
     * Test the AbstractIntegrator class with a simple set of differential equations.
     * @throws MathIllegalArgumentException if the set of equations is invalid
     * @throws MathIllegalStateException if the integrator cannot perform integration
     * @throws NumberIsTooSmallException if the integration span is too small
     */
    @Test
    public void testIntegration()
        throws MathIllegalArgumentException, MathIllegalStateException, NumberIsTooSmallException {
        final AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // perform integration
                super.integrate(equations, t);
            }
        };

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                System.out.println("Step handler: " + isLastStep);
            }
        });

        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handleState(double t, double[] y) {
                System.out.println("Event handler: " + t + ", " + FastMath.arrayToString(y));
            }
        }, 0.5, 0.01, 5);

        integrator.setMaxEvaluations(10);
        integrator.setEquations(equations);

        // integrate from t0=0 to t=1
        integrator.integrate(0.0, 1.0, new double[2], 1.0);

        // check that the integrator did not exceed the maximum number of evaluations
        assertEquals(10, integrator.getEvaluations());

        // check that the integration was correct
        final double[] expected = new double[] {1.0, 0.0};
        assertArrayEquals("Unexpected state at final time", expected, integrator.getState());

    }

    /**
     * Test the AbstractIntegrator class with an invalid set of differential equations.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidEquations() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setEquations(null);
        integrator.integrate(null, 0.0, new double[0], 1.0);
    }

    /**
     * Test the AbstractIntegrator class with a too small integration span.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallIntegrationSpan() {
        final AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.setEquations(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        });
        integrator.setMaxEvaluations(10);

        // the integration span is too small, it should fail
        integrator.integrate(0.0, 1e-12, new double[1], 1.0);

    }

}