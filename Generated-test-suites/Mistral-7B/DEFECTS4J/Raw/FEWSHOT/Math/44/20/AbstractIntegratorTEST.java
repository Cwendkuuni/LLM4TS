package org.apache.commons.math.ode;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.SolverProperty;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractStatefulEventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;

public class AbstractIntegratorTest {

    private static class TestDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[0] * (1 - y[0]);
        }
    }

    @SuppressWarnings("unchecked")
    private static class TestEventHandler extends AbstractStatefulEventHandler {
        private final double eventTime;
        private final double[] expectedState;

        public TestEventHandler(double eventTime, double[] expectedState) {
            this.eventTime = eventTime;
            this.expectedState = expectedState;
        }

        @Override
        public double getEventTime() {
            return eventTime;
        }

        @Override
        public void stepAccepted(double currentTime, double[] currentState) {
            assertTrue(Precision.isClose(currentTime, eventTime, 1E-9));
            assertArrayEquals(expectedState, currentState);
        }

        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return Precision.isClose(interpolator.getGlobalCurrentTime(), eventTime, 1E-9);
        }

        @Override
        public void reset(double currentTime, double[] currentState) {
            assertNull(currentState);
        }

        @Override
        public boolean stop() {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private static class TestStepHandler implements StepHandler {
        private final double[] expectedState;

        public TestStepHandler(double[] expectedState) {
            this.expectedState = expectedState;
        }

        @Override
        public void handleStep(StepInterpolator interpolator, boolean lastStep) {
            double[] state = interpolator.getInterpolatedState();
            assertArrayEquals(expectedState, state);
        }
    }

    @Test
    public void testAcceptStep() throws NumberIsTooSmallException, MaxCountExceededException {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no-op
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestDifferentialEquations());
        equations.setTime(0.0);
        equations.setPrimaryState(new double[]{1.0});

        integrator.setEquations(equations);
        integrator.setMaxEvaluations(10);

        double tTarget = 3.0;
        double[] yInitial = new double[]{1.0};

        StepInterpolator interpolator = new StepInterpolator(new SecantSolver(), SolverProperty.ABS_TOL, 1.0e-6);

        integrator.integrate(interpolator, yInitial, tTarget);

        double currentT = interpolator.getGlobalCurrentTime();
        double[] currentY = interpolator.getInterpolatedState();

        // Test with no events or step handlers.
        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{0.36787944117144233}, currentY);

        // Test with one event.
        EventHandler eventHandler = new TestEventHandler(1.5, new double[]{0.5});
        integrator.addEventHandler(eventHandler, 0.1, 1.0e-6, 5, new SecantSolver());

        integrator.integrate(interpolator, yInitial, tTarget);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{0.5}, currentY);

        // Test with one step handler.
        StepHandler stepHandler = new TestStepHandler(new double[]{0.6});
        integrator.addStepHandler(stepHandler);

        integrator.integrate(interpolator, yInitial, tTarget);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{0.6}, currentY);

        // Test with one event and one step handler.
        integrator.clearEventHandlers();
        integrator.clearStepHandlers();
        integrator.addEventHandler(eventHandler, 0.1, 1.0e-6, 5, new SecantSolver());
        integrator.addStepHandler(stepHandler);

        integrator.integrate(interpolator, yInitial, tTarget);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{0.6}, currentY);

        // Test with multiple events.
        List<EventHandler> eventHandlers = new ArrayList<>();
        eventHandlers.add(new TestEventHandler(1.0, new double[]{0.1}));
        eventHandlers.add(new TestEventHandler(2.0, new double[]{0.2}));

        integrator.clearEventHandlers();
        integrator.addEventHandler(eventHandlers.iterator().next());
        integrator.addEventHandler(eventHandlers.subList(1, eventHandlers.size()));

        integrator.integrate(interpolator, yInitial, tTarget);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{0.2}, currentY);

        // Test with multiple step handlers.
        List<StepHandler> stepHandlersList = new ArrayList<>();
        stepHandlersList.add(new TestStepHandler(new double[]{0.3}));
        stepHandlersList.add(new TestStepHandler(new double[]{0.4}));

        integrator.clearStepHandlers();
        integrator.addStepHandler(stepHandlersList.iterator().next());
        integrator.addStepHandler(stepHandlersList.subList(1, stepHandlersList.size()));

        integrator.integrate(interpolator, yInitial, tTarget);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{0.4}, currentY);

        // Test with multiple events and multiple step handlers.
        integrator.clearEventHandlers();
        integrator.clearStepHandlers();
        integrator.addEventHandler(eventHandlers.iterator().next());
        integrator.addEventHandler(eventHandlers.subList(1, eventHandlers.size()));
        integrator.addStepHandler(stepHandlersList.iterator().next());
        integrator.addStepHandler(stepHandlersList.subList(1, stepHandlersList.size()));

        integrator.integrate(interpolator, yInitial, tTarget);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{0.4}, currentY);

        // Test with a very small integration interval.
        integrator.clearEventHandlers();
        integrator.clearStepHandlers();
        integrator.setMaxEvaluations(100);
        double dt = 1.0e-12;
        integrator.integrate(interpolator, yInitial, tTarget + dt);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget + dt, currentT);
        assertArrayEquals(new double[]{FunctionUtils.round(Math.exp(tTarget * 0.01), 6)}, currentY);

        // Test with an integration interval that is too small.
        integrator.clearEventHandlers();
        integrator.clearStepHandlers();
        integrator.setMaxEvaluations(100);
        dt = 1.0e-15;

        try {
            integrator.integrate(interpolator, yInitial, tTarget + dt);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        // Test with an event that causes a state reset.
        integrator.clearEventHandlers();
        integrator.clearStepHandlers();
        integrator.addEventHandler(new TestEventHandler(1.0, new double[]{0.0}), new LaguerreSolver(0.1));
        integrator.integrate(interpolator, yInitial, tTarget);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{0.0}, currentY);

        // Test with a step handler that causes a state reset.
        integrator.clearEventHandlers();
        integrator.clearStepHandlers();
        integrator.addStepHandler(new TestStepHandler(new double[]{0.0}));
        integrator.integrate(interpolator, yInitial, tTarget);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{0.0}, currentY);

        // Test with an event that stops integration.
        integrator.clearEventHandlers();
        integrator.clearStepHandlers();
        integrator.addEventHandler(new TestEventHandler(1.0, new double[]{Double.NEGATIVE_INFINITY}), new BrentSolver(1.0e-6));
        integrator.integrate(interpolator, yInitial, tTarget);

        assertFalse(integrator.isLastStep());

        integrator.integrate(interpolator, yInitial, tTarget);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{Double.NEGATIVE_INFINITY}, currentY);

        assertTrue(integrator.isLastStep());

        // Test with a step handler that stops integration.
        integrator.clearEventHandlers();
        integrator.clearStepHandlers();
        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean lastStep) {
                interpolator.setStopRequested(true);
            }
        });
        integrator.integrate(interpolator, yInitial, tTarget);

        assertFalse(integrator.isLastStep());

        integrator.integrate(interpolator, yInitial, tTarget);

        currentT = interpolator.getGlobalCurrentTime();
        currentY = interpolator.getInterpolatedState();

        assertEquals(tTarget, currentT);
        assertArrayEquals(new double[]{Double.NaN}, currentY);

        assertTrue(integrator.isLastStep());
    }

}