package org.apache.commons.math3.ode.integrators.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.ode.util.UnivariateFunction;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    /** Simple differential equation for testing. */
    private static final FirstOrderDifferentialEquations TEST_DIFF_EQ = new FirstOrderDifferentialEquations() {

        /** {@inheritDoc} */
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0] + y[1];
            yDot[1] = -y[0] - y[1];
        }

        /** {@inheritDoc} */
        public int getDimension() {
            return 2;
        }

    };

    /** Helper class for testing event handlers. */
    private static class TestEventHandler implements EventHandler {

        /** {@inheritDoc} */
        public void handleEvent(double t, double[] y) {
            assertEquals(0, y[0], 1e-12);
            assertEquals(1, y[1], 1e-12);
        }

        /** {@inheritDoc} */
        public void init(double t0, double[] y0, double t) {
            // empty
        }

    }

    /** Helper class for testing step handlers. */
    private static class TestStepHandler implements StepHandler {

        /** {@inheritDoc} */
        public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
            if (lastStep) {
                assertTrue(interpolator.isLastStep());
            }
        }

        /** {@inheritDoc} */
        public void init(double t0, double[] y0, double t) {
            // empty
        }

    }

    /** Helper function for testing events. */
    private static double testEvent(AbstractIntegrator integrator, double t0, double[] y0, double t)
        throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException,
               NoBracketingException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(TEST_DIFF_EQ);
        integrator.initIntegration(t0, y0, t);
        integrator.integrate(equations, t);
        return equations.getTime();
    }

    /** Helper function for testing integration with no events. */
    @Before
    public void setUp() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            /** {@inheritDoc} */
            protected void addEventHandler(EventHandler handler, double maxCheckInterval,
                                           double convergence, int maxIterationCount,
                                           UnivariateSolver solver) {
                // empty
            }
        };
        integrator.addStepHandler(new TestStepHandler());
    }

    /** Test integration with no events. */
    @Test
    public void testNoEventsIntegration()
        throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException,
               NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.addStepHandler(new TestStepHandler());
        double t0 = 0;
        double[] y0 = new double[] { 1, 1 };
        double t = 2 * Math.PI;
        double tIntegrated = testEvent(integrator, t0, y0, t);
        assertEquals(t, tIntegrated, 1e-12);
    }

    /** Test integration with an event. */
    @Test
    public void testEventIntegration()
        throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException,
               NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator() {

            /** {@inheritDoc} */
            protected void addEventHandler(EventHandler handler, double maxCheckInterval,
                                           double convergence, int maxIterationCount,
                                           UnivariateSolver solver) {
                integrator.addEventHandler(new TestEventHandler(), 0, 1e-12, 10, new BracketingNthOrderBrentSolver(1e-12, 5));
            }

        };
        integrator.addStepHandler(new TestStepHandler());
        double t0 = 0;
        double[] y0 = new double[] { 1, 1 };
        double t = 2 * Math.PI;
        double tIntegrated = testEvent(integrator, t0, y0, t);
        assertEquals(t, tIntegrated, 1e-12);
    }

    /** Test integration with two events. */
    @Test
    public void testTwoEventsIntegration()
        throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException,
               NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator() {

            /** {@inheritDoc} */
            protected void addEventHandler(EventHandler handler, double maxCheckInterval,
                                           double convergence, int maxIterationCount,
                                           UnivariateSolver solver) {
                integrator.addEventHandler(new TestEventHandler(), 0, 1e-12, 10,
                        new BracketingNthOrderBrentSolver(1e-12, 5));
                integrator.addEventHandler(new TestEventHandler(), 2 * Math.PI, 1e-12, 10,
                        new BracketingNthOrderBrentSolver(1e-12, 5));
            }

        };
        integrator.addStepHandler(new TestStepHandler());
        double t0 = 0;
        double[] y0 = new double[] { 1, 1 };
        double t = 4 * Math.PI;
        try {
            double tIntegrated = testEvent(integrator, t0, y0, t);
            fail("Test should not have reached here.");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    /** Test integration with an event that triggers a state reset. */
    @Test
    public void testEventStateResetIntegration()
        throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException,
               NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegrator() {

            /** {@inheritDoc} */
            protected void addEventHandler(EventHandler handler, double maxCheckInterval,
                                           double convergence, int maxIterationCount,
                                           UnivariateSolver solver) {
                integrator.addEventHandler(new TestEventHandlerWithStateReset(), 0, 1e-12, 10,
                        new BracketingNthOrderBrentSolver(1e-12, 5));
            }

        };
        integrator.addStepHandler(new TestStepHandler());
        double t0 = 0;
        double[] y0 = new double[] { 1, 1 };
        double t = 2 * Math.PI;
        double tIntegrated = testEvent(integrator, t0, y0, t);
        assertEquals(t, tIntegrated, 1e-12);
    }

    /** Helper class for testing event handlers with state resets. */
    private static class TestEventHandlerWithStateReset implements EventHandler {

        /** {@inheritDoc} */
        public void handleEvent(double t, double[] y) {
            // reset state
            y[0] = 0;
            y[1] = 0;
        }

        /** {@inheritDoc} */
        public void init(double t0, double[] y0, double t) {
            // empty
        }

    }

}