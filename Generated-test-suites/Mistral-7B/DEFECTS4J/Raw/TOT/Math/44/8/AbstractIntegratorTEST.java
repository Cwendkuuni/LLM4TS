package org.apache.commons.math.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.interpolators.StepInterpolator;
import org.apache.commons.math3.ode.sampling.AbstractStepHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing the {@code AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    /** Integrator under test. */
    private AbstractIntegrator integrator;

    /** Differential equations to integrate. */
    private static final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

        /** {@inheritDoc} */
        public int getDimension() {
            return 2;
        }

        /** {@inheritDoc} */
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }

    };

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            /** {@inheritDoc} */
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
                // no-op
            }

            /** {@inheritDoc} */
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException {
                equations.computeDerivatives(t, y, yDot);
            }
        };
    }

    /**
     * Test integration from time 0 to 1.
     */
    @Test
    public void testIntegrationFrom0To1()
        throws MathIllegalStateException, MathIllegalArgumentException, NumberIsTooSmallException {

        integrator.setMaxEvaluations(100);
        integrator.addStepHandler(new StepHandlerAdapter() {
            /** {@inheritDoc} */
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                System.out.println("Step " + interpolator.getGlobalCurrentTime());
            }
        });
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 1.0, new double[2]);
        assertEquals(1.0, integrator.getCurrentSignedStepsize(), 1e-12);
        assertEquals(1.0, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(new double[] { FastMath.E, FastMath.SQRT2 }, integrator.getPrimaryState(), 1e-12);
        assertEquals(1.0, integrator.getTime(), 1e-12);

    }

    /**
     * Test integration from time 1 to 0.
     */
    @Test
    public void testIntegrationFrom1To0()
        throws MathIllegalStateException, MathIllegalArgumentException, NumberIsTooSmallException {

        integrator.setMaxEvaluations(100);
        integrator.addStepHandler(new StepHandlerAdapter() {
            /** {@inheritDoc} */
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                System.out.println("Step " + interpolator.getGlobalCurrentTime());
            }
        });
        integrator.integrate(equations, 1.0, new double[] { FastMath.E, FastMath.SQRT2 }, -1.0, new double[2]);
        assertEquals(-1.0, integrator.getCurrentSignedStepsize(), 1e-12);
        assertEquals(-1.0, integrator.getCurrentStepStart(), 1e-12);
        assertEquals(new double[] { 1.0, -FastMath.SQRT2 }, integrator.getPrimaryState(), 1e-12);
        assertEquals(-1.0, integrator.getTime(), 1e-12);

    }

    /**
     * Test integration with initial conditions outside allowed range.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrationWithInitialConditionsOutsideAllowedRange()
        throws MathIllegalStateException, MathIllegalArgumentException, NumberIsTooSmallException {

        integrator.setMaxEvaluations(100);
        integrator.integrate(equations, 0.0, new double[] { 3.0, 0.0 }, 1.0, new double[2]);

    }

    /**
     * Test integration with too small integration span.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrationWithTooSmallIntegrationSpan()
        throws MathIllegalStateException, MathIllegalArgumentException, NumberIsTooSmallException {

        integrator.setMaxEvaluations(100);
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 1e-10, new double[2]);

    }

    /**
     * Test step handler.
     */
    @Test
    public void testStepHandler()
        throws MathIllegalStateException, MathIllegalArgumentException, NumberIsTooSmallException {

        integrator.setMaxEvaluations(100);
        integrator.addStepHandler(new StepHandlerAdapter() {
            /** {@inheritDoc} */
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    fail("Last step called unexpectedly");
                }
            }
        });
        integrator.integrate(equations, 0.0, new double[] { 1.0, 0.0 }, 1.0, new double[2]);
        assertEquals(false, integrator.isLastStep());

    }

}