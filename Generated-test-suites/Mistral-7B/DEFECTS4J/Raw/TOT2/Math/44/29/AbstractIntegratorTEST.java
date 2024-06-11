package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.ode.test.MockEventHandler;
import org.apache.commons.math3.ode.test.MockStepHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest extends AbstractODETest {

    /** Integrator to test. */
    private AbstractIntegrator integrator;

    /** Test equation. */
    private static final FirstOrderDifferentialEquations EQUATIONS = new FirstOrderDifferentialEquations() {
        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0] * (1 - y[0]);
        }
    };

    /**
     * Set up.
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                sanityChecks(equations, t);
                computeDerivatives(t, equations.getPrimaryState(), equations.getDerivative(t));
            }
        };
        integrator.setEquations(new ExpandableStatefulODE(EQUATIONS));
    }

    /**
     * Test integration.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIntegration() throws Exception {
        final double[] initialValue = new double[] { 0.1 };
        final double targetTime = 2.0;
        assertEquals(1.0, integrator.integrate(EQUATIONS, 0.0, initialValue, targetTime, new double[initialValue.length]), 1e-10);
    }

    /**
     * Test integration with step handlers.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIntegrationWithStepHandlers() throws Exception {
        integrator.addStepHandler(new MockStepHandler());
        final double[] initialValue = new double[] { 0.1 };
        final double targetTime = 2.0;
        assertEquals(1.0, integrator.integrate(EQUATIONS, 0.0, initialValue, targetTime, new double[initialValue.length]), 1e-10);
    }

    /**
     * Test integration with event handlers.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIntegrationWithEventHandlers() throws Exception {
        integrator.addEventHandler(new MockEventHandler(), 0.5, 1e-5, 5);
        final double[] initialValue = new double[] { 0.1 };
        final double targetTime = 2.0;
        integrator.integrate(EQUATIONS, 0.0, initialValue, targetTime);
        assertEquals(1.0, integrator.getEvaluations(), 1);
    }

    /**
     * Test integration with too many evaluations.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIntegrationWithTooManyEvaluations() throws Exception {
        integrator.setMaxEvaluations(1);
        final double[] initialValue = new double[] { 0.1 };
        final double targetTime = 2.0;
        assertThrows(MaxCountExceededException.class, () -> integrator.integrate(EQUATIONS, 0.0, initialValue, targetTime, new double[initialValue.length]));
    }

    /**
     * Test integration with wrong dimension.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIntegrationWithWrongDimension() throws Exception {
        final FirstOrderDifferentialEquations wrongEquations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }
        };
        final double[] initialValue = new double[] { 0.1, 0.2 };
        final double targetTime = 2.0;
        assertThrows(DimensionMismatchException.class, () -> integrator.integrate(wrongEquations, 0.0, initialValue, targetTime, new double[initialValue.length]));
    }

}