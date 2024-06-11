package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.util.UnmodifiedStateAndDerivativeStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /** Integrator to test. */
    private AbstractIntegrator integrator;

    /** Differential equations. */
    private static final FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

    /**
     * Set up a new test instance.
     */
    @Before
    public void setUp() {
        integrator = new TestIntegrator("TestIntegrator");
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handleEvent(double t, double[] y) {
                // do nothing
            }

            @Override
            public double getEventTime() {
                return 1;
            }
        }, 0.01, 1.0e-6, 10, new BracketingNthOrderBrentSolver(1.0e-6, 5));
    }

    /**
     * Test method integration.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIntegration() throws Exception {
        final double[] y0 = { 1.0, 0.0 };
        final double tEnd = 2.0;
        final double[] y = new double[equations.getDimension()];
        final double t = integrator.integrate(equations, 0.0, y0, tEnd, y);
        assertEquals(tEnd, t, 1e-6);
        assertEquals(y0[0] + equations.getDerivative(0.0, y0)[0] * tEnd, y[0], 1e-6);
        assertEquals(y0[1] + equations.getDerivative(0.0, y0)[1] * tEnd, y[1], 1e-6);
    }

    /**
     * Test event handling.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEventHandling() throws Exception {
        assertEquals(0, integrator.getEvaluations());
        final double[] y0 = { 1.0, 0.0 };
        integrator.integrate(equations, 0.0, y0, 1.1, new double[equations.getDimension()]);
        assertTrue(integrator.isLastStep());
        assertFalse(integrator.getResetOccurred());
        assertEquals(1, integrator.getEvaluations());
        integrator.resetEvaluations();
        integrator.integrate(equations, 0.0, y0, 1.1, new double[equations.getDimension()]);
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.getResetOccurred());
        assertEquals(2, integrator.getEvaluations());
    }

    /**
     * Test exception handling.
     * @throws Exception if an error occurs
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() throws Exception {
        final double[] y0 = { 1.0 };
        integrator.integrate(equations, 0.0, y0, 1.1, new double[equations.getDimension() - 1]);
    }

    /**
     * Test maximum evaluations.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws NumberIsTooSmallException, Exception {
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[equations.getDimension()], 1.1, new double[equations.getDimension()]);
    }

    /**
     * Test integration with too small span.
     * @throws Exception if an error occurs
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws NumberIsTooSmallException, Exception {
        integrator.setMaxEvaluations(-1);
        integrator.integrate(equations, 0.0, new double[equations.getDimension()], 1e-6, new double[equations.getDimension()]);
    }

    /**
     * Test integration with step handlers.
     * @throws Exception if an error occurs
     */
    @Test
    public void testStepHandlers() throws NumberIsTooSmallException, Exception {
        final List<Double> yHistory = new ArrayList<>();
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                final double[] y = interpolator.getInterpolatedState();
                yHistory.add(y[0]);
            }
        };
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, 0.0, new double[equations.getDimension()], 2.0, new double[equations.getDimension()]);
        assertEquals(3, yHistory.size());
        assertEquals(0.0, yHistory.get(0), 1e-6);
        assertEquals(1.0, yHistory.get(1), 1e-6);
        assertEquals(2.0, yHistory.get(2), 1e-6);
    }

    /**
     * Test integration with events.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEvents() throws NumberIsTooSmallException, Exception {
        final double[] y0 = { 1.0, 0.0 };
        final double tEnd = 2.0;
        final double[] y = new double[equations.getDimension()];
        integrator.integrate(equations, 0.0, y0, tEnd, y);
        assertEquals(y0[0] + equations.getDerivative(0.0, y0)[0] * tEnd, y[0], 1e-6);
        assertEquals(y0[1] + equations.getDerivative(0.0, y0)[1] * tEnd, y[1], 1e-6);
        assertEquals(1, integrator.getEvaluations());
        integrator.resetEvaluations();
        integrator.integrate(equations, 0.0, y0, tEnd, y);
        assertEquals(2, integrator.getEvaluations());
        integrator.resetEvaluations();
        integrator.integrate(equations, 0.0, y0, tEnd, y);
        assertEquals(3, integrator.getEvaluations());
    }

    /**
     * Test integration with step interpolator.
     * @throws Exception if an error occurs
     */
    @Test
    public void testStepInterpolator() throws NumberIsTooSmallException, Exception {
        final double[] y0 = { 1.0, 0.0 };
        final double tEnd = 2.0;
        final double[] y = new double[equations.getDimension()];
        integrator.integrate(equations, 0.0, y0, tEnd, y,
            new UnmodifiedStateAndDerivativeStepInterpolator(0.01));
        assertEquals(y0[0] + equations.getDerivative(0.0, y0)[0] * tEnd, y[0], 1e-6);
        assertEquals(y0[1] + equations.getDerivative(0.0, y0)[1] * tEnd, y[1], 1e-6);
        assertEquals(1 + PrecisionUtils.round(tEnd / 0.01), integrator.getEvaluations());
    }

    /**
     * Test integration with events and step interpolator.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEventsAndStepInterpolator() throws NumberIsTooSmallException, Exception {
        final double[] y0 = { 1.0, 0.0 };
        final double tEnd = 2.0;
        final double[] y = new double[equations.getDimension()];
        integrator.integrate(equations, 0.0, y0, tEnd, y,
            new UnmodifiedStateAndDerivativeStepInterpolator(0.01),
            Collections.singletonList(new EventHandler() {
                @Override
                public void handleEvent(double t, double[] y) {
                    // do nothing
                }

                @Override
                public double getEventTime() {
                    return 1;
                }
            }));
        assertEquals(y0[0] + equations.getDerivative(0.0, y0)[0] * tEnd, y[0], 1e-6);
        assertEquals(y0[1] + equations.getDerivative(0.0, y0)[1] * tEnd, y[1], 1e-6);
        assertEquals(1 + PrecisionUtils.round(tEnd / 0.01) * 2, integrator.getEvaluations());
    }

    /**
     * Test integration with step interpolator and reset on event.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEventsAndStepInterpolatorWithReset() throws NumberIsTooSmallException, Exception {
        final double[] y0 = { 1.0, 0.0 };
        final double tEnd = 2.0;
        final double[] y = new double[equations.getDimension()];
        integrator.integrate(equations, 0.0, y0, tEnd, y,
            new UnmodifiedStateAndDerivativeStepInterpolator(0.01),
            Collections.singletonList(new EventHandler() {
                @Override
                public void handleEvent(double t, double[] y) {
                    y[0] = 2.0;
                }

                @Override
                public double getEventTime() {
                    return 1;
                }
            }));
        assertEquals(2.0, y[0], 1e-6);
        assertEquals(y0[1] + equations.getDerivative(0.0, y0)[1] * tEnd, y[1], 1e-6);
        assertEquals(1 + PrecisionUtils.round(tEnd / 0.01) * 2, integrator.getEvaluations());
    }

    /**
     * Dummy differential equations used for testing.
     */
    private static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public double[] getDerivative(double t, double[] y) {
            return new double[] { y[0] + y[1], y[0] };
        }

        @Override
        public void setTime(double time) {
            // do nothing
        }

        @Override
        public void setPrimaryState(double[] primaryState) {
            // do nothing
        }

    }

    /**
     * Test integrator.
     */
    private static class TestIntegrator extends AbstractIntegrator {

        /**
         * Constructor.
         * @param name name of the integrator
         */
        public TestIntegrator(String name) {
            super(name);
        }

        /**
         * Integration of the dummy differential equations.
         * @param equations equations to integrate
         * @param t target time for the integration
         * @throws MathIllegalStateException if the integrator cannot perform integration
         * @throws MathIllegalArgumentException if integration parameters are wrong
         */
        @Override
        public void integrate(ExpandableStatefulODE equations, double t)
            throws MathIllegalStateException, MathIllegalArgumentException {
            sanityChecks(equations, t);
            super.integrate(equations, t);
        }

        /**
         * Dummy integration.
         * @param equations equations to integrate
         * @param t target time for the integration
         * @param y state vector at step end time, must be reset if an event asks for resetting
         * @param yDot placeholder array where to put the time derivative of the state vector
         * @throws MathIllegalStateException if the value of one event state cannot be evaluated
         */
        @Override
        protected void acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd)
            throws MathIllegalStateException {
            super.acceptStep(interpolator, y, yDot, tEnd);
        }

        /**
         * Sanity checks.
         * @param equations equations to integrate
         * @param t target time for the integration
         * @throws NumberIsTooSmallException if integration span is too small
         */
        protected void sanityChecks(ExpandableStatefulODE equations, double t)
            throws NumberIsTooSmallException {
            super.sanityChecks(equations, t);
        }

    }

}