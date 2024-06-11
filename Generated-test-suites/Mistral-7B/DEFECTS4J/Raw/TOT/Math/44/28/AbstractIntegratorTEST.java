package org.apache.commons.math.ode.test;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Precision;

/**
 * Test for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest extends TestCase {

    /**
     * A simple test equation set.
     */
    private static final FirstOrderDifferentialEquations EQUATIONS = new FirstOrderDifferentialEquations() {

        /** {@inheritDoc} */
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -2 * y[1];
        }

        /** {@inheritDoc} */
        public int getDimension() {
            return 2;
        }

        /** {@inheritDoc} */
        public ExpandableStatefulODE expand(double t, double[] y) {
            return new ExpandableStatefulODE(this) {
                {
                    setTime(t);
                    setPrimaryState(y);
                }
            };
        }

    };

    /**
     * A step handler that just increments a counter.
     */
    private static class CounterStepHandler extends StepHandlerAdapter {

        /** Step handler counter. */
        private int steps;

        /** {@inheritDoc} */
        public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
            steps++;
        }

        /** {@inheritDoc} */
        public int getSteps() {
            return steps;
        }

    }

    /**
     * A test integrator with a single step handler.
     */
    private static class TestIntegrator extends AbstractIntegrator {

        /** {@inheritDoc} */
        @Override
        public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
            super.integrate(equations, t);
            assertEquals(2, getEvaluations());
        }

        /** {@inheritDoc} */
        @Override
        public void addStepHandler(StepHandler handler) {
            super.addStepHandler(new CounterStepHandler());
        }

    }

    /**
     * Test the integration of a simple test equation set.
     */
    public void testIntegration() {

        // set up integrator
        final TestIntegrator integrator = new TestIntegrator("TestIntegrator");
        integrator.setMaxEvaluations(1000);
        integrator.clearStepHandlers();
        integrator.addStepHandler(new CounterStepHandler());

        // integrate simple test equation set
        integrator.setEquations(EQUATIONS);
        try {
            integrator.integrate(integrator.getEquations(), 0.0, new double[] {1.0, 0.0}, 2.0, new double[2]);
        } catch (MathIllegalStateException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // check evaluation count and step handler count
        assertEquals(11, integrator.getEvaluations());
        assertEquals(11, integrator.getCounterStepHandler().getSteps());

        // check results
        final double[] results = integrator.getEquations().getPrimaryState();
        assertEquals(1.543080634824195, results[0], 1e-6);
        assertEquals(-0.618033988747368, results[1], 1e-6);

    }

    /**
     * Test the integration of a simple test equation set with a too small integration span.
     */
    public void testIntegrationTooSmall() {

        // set up integrator
        final TestIntegrator integrator = new TestIntegrator("TestIntegrator");
        integrator.setMaxEvaluations(1000);
        integrator.clearStepHandlers();
        integrator.addStepHandler(new CounterStepHandler());

        // integrate simple test equation set with a too small integration span
        integrator.setEquations(EQUATIONS);
        try {
            integrator.integrate(integrator.getEquations(), 0.0, new double[] {1.0, 0.0}, 0.0, new double[2]);
            fail("Expected exception: " + NumberIsTooSmallException.class.getName());
        } catch (NumberIsTooSmallException e) {
            // expected
        } catch (MathIllegalStateException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

    }

    /**
     * Test the integration of a simple test equation set with a too large integration span.
     */
    public void testIntegrationTooLarge() {

        // set up integrator
        final TestIntegrator integrator = new TestIntegrator("TestIntegrator");
        integrator.setMaxEvaluations(1000);
        integrator.clearStepHandlers();
        integrator.addStepHandler(new CounterStepHandler());

        // integrate simple test equation set with a too large integration span
        integrator.setEquations(EQUATIONS);
        try {
            integrator.integrate(integrator.getEquations(), 0.0, new double[] {1.0, 0.0}, 10000.0, new double[2]);
            fail("Expected exception: " + MaxCountExceededException.class.getName());
        } catch (MaxCountExceededException e) {
            // expected
        } catch (MathIllegalStateException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

    }

    /**
     * Test the integration of a simple test equation set with an event that triggers a reset.
     */
    public void testIntegrationEventReset() {

        // set up integrator
        final TestIntegrator integrator = new TestIntegrator("TestIntegrator");
        integrator.setMaxEvaluations(1000);
        integrator.clearStepHandlers();
        integrator.addStepHandler(new CounterStepHandler());

        // add an event handler that triggers a reset
        integrator.addEventHandler(new MyEventHandler(), 0.5, 1e-6, 1, new BracketingNthOrderBrentSolver(1e-6, 5));

        // integrate simple test equation set with an event that triggers a reset
        integrator.setEquations(EQUATIONS);
        try {
            integrator.integrate(integrator.getEquations(), 0.0, new double[] {1.0, 0.0}, 2.0, new double[2]);
            fail("Expected exception: " + MathIllegalStateException.class.getName());
        } catch (MathIllegalStateException e) {
            // expected
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // check that reset occured and that derivatives were recomputed
        assertTrue(integrator.getResetOccurred());
        assertTrue(integrator.getEvaluations() > 11);

    }

    /**
     * Test the integration of a simple test equation set with multiple events.
     */
    public void testIntegrationMultipleEvents() {

        // set up integrator
        final TestIntegrator integrator = new TestIntegrator("TestIntegrator");
        integrator.setMaxEvaluations(1000);
        integrator.clearStepHandlers();
        integrator.addStepHandler(new CounterStepHandler());

        // add two event handlers
        integrator.addEventHandler(new MyEventHandler1(), 0.25, 1e-6, 1, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.addEventHandler(new MyEventHandler2(), 0.75, 1e-6, 1, new BracketingNthOrderBrentSolver(1e-6, 5));

        // integrate simple test equation set with multiple events
        integrator.setEquations(EQUATIONS);
        try {
            integrator.integrate(integrator.getEquations(), 0.0, new double[] {1.0, 0.0}, 2.0, new double[2]);
            fail("Expected exception: " + MathIllegalStateException.class.getName());
        } catch (MathIllegalStateException e) {
            // expected
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // check that reset occured and that derivatives were recomputed for the second event
        assertTrue(integrator.getResetOccurred());
        assertTrue(integrator.getEvaluations() > 13);

    }

    /**
     * A simple event handler that triggers a reset.
     */
    private static class MyEventHandler extends EventHandlerAdapter {

        /** {@inheritDoc} */
        public void handleEvent(double t, double[] y) {
            if (Precision.abs(y[0], 1e-6) > 1.1) {
                setReset(true);
            }
        }

        /** {@inheritDoc} */
        public boolean stop() {
            return getReset();
        }

        /** {@inheritDoc} */
        public double getEventTime() {
            return 1.0;
        }

        /** {@inheritDoc} */
        public void reinitializeBegin(AbstractStepInterpolator interpolator) {
            setReset(false);
        }

        /** Reset flag. */
        private boolean reset = false;

    }

    /**
     * A simple event handler that triggers a reset for the second event.
     */
    private static class MyEventHandler2 extends MyEventHandler {

        /** {@inheritDoc} */
        public void handleEvent(double t, double[] y) {
            super.handleEvent(t, y);
            if (Precision.abs(y[0], 1e-6) < 0.5) {
                setReset(true);
            }
        }

    }

    /**
     * A simple event handler that triggers a reset for the first event.
     */
    private static class MyEventHandler1 extends MyEventHandler {

        /** {@inheritDoc} */
        public void handleEvent(double t, double[] y) {
            super.handleEvent(t, y);
            if (Precision.abs(y[0], 1e-6) > 1.5) {
                setReset(true);
            }
        }

    }

    /**
     * A step handler counter.
     */
    private static class CounterStepHandler extends StepHandlerAdapter {

        /** Step handler counter. */
        private int steps = 0;

        /** {@inheritDoc} */
        public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
            steps++;
        }

        /** {@inheritDoc} */
        public int getSteps() {
            return steps;
        }

    }

    /**
     * A simple test integrator.
     */
    private static class TestIntegrator extends AbstractIntegrator {

        /** {@inheritDoc} */
        public String getName() {
            return "TestIntegrator";
        }

        /** {@inheritDoc} */
        protected AbstractStepInterpolator createInterpolator() {
            return new AbstractStepInterpolator() {
            };
        }

        /** {@inheritDoc} */
        @Override
        public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
            super.integrate(equations, t);
            assertEquals(2, getEvaluations());
        }

        /** {@inheritDoc} */
        @Override
        public void addStepHandler(StepHandler handler) {
            super.addStepHandler(handler);
        }

        /** {@inheritDoc} */
        @Override
        public void addEventHandler(EventHandler handler, double maxCheckInterval,
                double convergence, int maxIterationCount, UnivariateRealSolver solver) {
            super.addEventHandler(handler, maxCheckInterval, convergence,
                                  maxIterationCount, solver);
        }

        /** {@inheritDoc} */
        @Override
        public Collection<EventHandler> getEventHandlers() {
            return super.getEventHandlers();
        }

        /** {@inheritDoc} */
        @Override
        public void clearEventHandlers() {
            super.clearEventHandlers();
        }

        /** {@inheritDoc} */
        @Override
        public double integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
            super.integrate(equations, t);
            assertEquals(2, getEvaluations());
            return super.getTime();
        }

        /** {@inheritDoc} */
        @Override
        public double getCurrentSignedStepsize() {
            return super.getCurrentSignedStepsize();
        }

        /** {@inheritDoc} */
        @Override
        public void setEquations(ExpandableStatefulODE equations) {
            super.setEquations(equations);
        }

        /** {@inheritDoc} */
        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            super.setMaxEvaluations(maxEvaluations);
        }

        /** {@inheritDoc} */
        @Override
        public int getMaxEvaluations() {
            return super.getMaxEvaluations();
        }

        /** {@inheritDoc} */
        @Override
        public int getEvaluations() {
            return super.getEvaluations();
        }

        /** {@inheritDoc} */
        @Override
        public void resetEvaluations() {
            super.resetEvaluations();
        }

    }

}