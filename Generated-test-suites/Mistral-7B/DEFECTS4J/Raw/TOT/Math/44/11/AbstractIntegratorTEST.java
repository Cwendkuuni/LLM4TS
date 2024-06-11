package org.apache.commons.math.ode.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.Precision;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.SimpleEvent;
import org.apache.commons.math3.ode.integrators.AbstractIntegrator;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /** Test integrator. */
    private AbstractIntegrator integrator;

    /** Test equations. */
    private static final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

        /** {@inheritDoc} */
        @Override
        public int getDimension() {
            return 1;
        }

        /** {@inheritDoc} */
        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0] + 1;
        }

        /** {@inheritDoc} */
        @Override
        public double getTime() {
            return 0;
        }

        /** {@inheritDoc} */
        @Override
        public void setTime(double t) {
            // empty
        }

        /** {@inheritDoc} */
        @Override
        public double[] getPrimaryState() {
            return new double[] { 0 };
        }

        /** {@inheritDoc} */
        @Override
        public void setPrimaryState(double[] y) {
            // empty
        }

    };

    /** Test event handler. */
    private static class TestEventHandler implements EventHandler {

        /** {@inheritDoc} */
        public void handleEvent(double t, double[] y) {
            // empty
        }

        /** {@inheritDoc} */
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return Precision.equals(interpolator.getInterpolatedTime(), 1);
        }

        /** {@inheritDoc} */
        public EventState getEventState(double eventTime, double[] y) {
            return new SimpleEvent(eventTime, this);
        }

        /** {@inheritDoc} */
        public void reinitializeBegin(AbstractStepInterpolator interpolator) {
            // empty
        }

        /** {@inheritDoc} */
        public void stepAccepted(double eventTime, double[] eventY) {
            // empty
        }

        /** {@inheritDoc} */
        public boolean stop() {
            return false;
        }

        /** {@inheritDoc} */
        public double getEventTime() {
            return 1;
        }

    } ;

    /** Test step handler. */
    private static class TestStepHandler implements StepHandler {

        /** {@inheritDoc} */
        public void handleStep(AbstractIntegrator integrator, boolean isLastStep) {
            if (isLastStep) {
                assertFalse("Unexpected last step", integrator.isLastStep());
            }
        }

    }

    /** Test step interpolator. */
    private StepInterpolator interpolator;

    /** Test event handler. */
    private EventHandler eventHandler;

    /** Test step handler. */
    private StepHandler stepHandler;

    /** Test equations. */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {

            /** {@inheritDoc} */
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
                // empty
            }

            /** {@inheritDoc} */
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException {
                super.computeDerivatives(t, y, yDot);
            }

            /** {@inheritDoc} */
            @Override
            public boolean isLastStep() {
                return false;
            }

            /** {@inheritDoc} */
            @Override
            public void reset() {
                // empty
            }

        };
        integrator.setEquations(equations);
        interpolator = new StepInterpolator(0.1);
        integrator.addStepHandler(new TestStepHandler());
        eventHandler = new TestEventHandler();
        integrator.addEventHandler(eventHandler, 1, 1e-10, 100);
    }

    /** Test integration. */
    @Test
    public void testIntegration() throws Exception {
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 0, new double[] { 0 }, 2, new double[] { 2 });
        assertTrue("Integration not successful", integrator.isLastStep());
        assertEquals("Unexpected final time", 2, integrator.getTime(), Precision.SAFE_EPSILON);
        assertNotNull("Unexpected null result", integrator.getPrimaryState());
        assertTrue("Unexpected reset indicator", !integrator.resetOccurred);
    }

    /** Test integration with events. */
    @Test
    public void testIntegrationWithEvents() throws Exception {
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 0, new double[] { 0 }, 2, new double[] { 2 });
        assertTrue("Unexpected event state", integrator.getEventHandlers().isEmpty());
        assertTrue("Unexpected last step", integrator.isLastStep());
        assertEquals("Unexpected final time", 2, integrator.getTime(), Precision.SAFE_EPSILON);
        assertNotNull("Unexpected null result", integrator.getPrimaryState());
        assertTrue("Unexpected reset indicator", !integrator.resetOccurred);
        integrator.reset();
        integrator.integrate(equations, 0, new double[] { 0 }, 1.5, new double[] { 1 });
        assertEquals("Unexpected last step", false, integrator.isLastStep());
        assertEquals("Unexpected final time", 1.5, integrator.getTime(), Precision.SAFE_EPSILON);
        assertNull("Unexpected non-null result", integrator.getPrimaryState());
        assertTrue("Unexpected reset indicator", integrator.resetOccurred);
        assertEquals("Unexpected event handler count", 1, integrator.getEventHandlers().size());
        EventState eventState = ((SimpleEvent) integrator.getEventHandlers().iterator().next()).getEventState();
        assertNotNull("Unexpected null event state", eventState);
        assertNotNull("Unexpected null y", eventState.getEventY());
        assertEquals("Unexpected event time", 1, eventState.getEventTime(), Precision.SAFE_EPSILON);
    }

    /** Test integration with events and step handlers. */
    @Test
    public void testIntegrationWithEventsAndStepHandlers() throws Exception {
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 0, new double[] { 0 }, 2, new double[] { 2 });
        assertTrue("Unexpected last step", integrator.isLastStep());
        assertEquals("Unexpected final time", 2, integrator.getTime(), Precision.SAFE_EPSILON);
        assertNotNull("Unexpected null result", integrator.getPrimaryState());
        assertTrue("Unexpected reset indicator", !integrator.resetOccurred);
        assertTrue("Unexpected event handler count", 1, integrator.getEventHandlers().size());
        integrator.reset();
        integrator.integrate(equations, 0, new double[] { 0 }, 1.5, new double[] { 1 });
        assertEquals("Unexpected last step", false, integrator.isLastStep());
        assertEquals("Unexpected final time", 1.5, integrator.getTime(), Precision.SAFE_EPSILON);
        assertNull("Unexpected non-null result", integrator.getPrimaryState());
        assertTrue("Unexpected reset indicator", integrator.resetOccurred);
        integrator.addEventHandler(eventHandler, 1, 1e-10, 100);
        integrator.integrate(equations, 0, new double[] { 0 }, 2, new double[] { 2 });
        assertTrue("Unexpected last step", integrator.isLastStep());
        assertEquals("Unexpected final time", 2, integrator.getTime(), Precision.SAFE_EPSILON);
        assertNotNull("Unexpected null result", integrator.getPrimaryState());
        assertTrue("Unexpected reset indicator", !integrator.resetOccurred);
        assertTrue("Unexpected event handler count", 1, integrator.getEventHandlers().size());
        integrator.reset();
        integrator.integrate(equations, 0, new double[] { 0 }, 1.5, new double[] { 1 });
        assertEquals("Unexpected last step", false, integrator.isLastStep());
        assertEquals("Unexpected final time", 1.5, integrator.getTime(), Precision.SAFE_EPSILON);
        assertNull("Unexpected non-null result", integrator.getPrimaryState());
        assertTrue("Unexpected reset indicator", integrator.resetOccurred);
        integrator.addStepHandler(new TestStepHandler());
        integrator.integrate(equations, 0, new double[] { 0 }, 1, new double[] { 1 });
        assertTrue("Unexpected last step", false, integrator.isLastStep());
        assertEquals("Unexpected final time", 1, integrator.getTime(), Precision.SAFE_EPSILON);
        assertNotNull("Unexpected null result", integrator.getPrimaryState());
        assertTrue("Unexpected reset indicator", integrator.resetOccurred);
    }

    /** Test integration with too small step size. */
    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrationWithTooSmallStepSize() throws Exception {
        integrator.setMaxEvaluations(10);
        integrator.integrate(equations, 0, new double[] { 0 }, 0.0001, new double[] { 0.0001 });
    }

}