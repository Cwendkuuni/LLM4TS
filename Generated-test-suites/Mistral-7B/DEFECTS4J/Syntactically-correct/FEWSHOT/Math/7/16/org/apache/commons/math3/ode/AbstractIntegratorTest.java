package org.apache.commons.math3.ode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.MockEventState;
import org.apache.commons.math3.ode.sampling.MockStepHandler;
import org.apache.commons.math3.ode.sampling.MockStepInterpolator;
import org.apache.commons.math3.ode.stepper.Dopri55Stepper;
import org.apache.commons.math3.ode.stepper.RungeKutta54Stepper;
import org.apache.commons.math3.ode.stepper.Stepper;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.apache.commons.math3.util.Precision;

import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest extends AbstractMathTestCase {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws DimensionMismatchException, NoBracketingException,
                           MaxCountExceededException {
                // empty implementation
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot)
                    throws MaxCountExceededException, DimensionMismatchException {
                // empty implementation
            }
        };

        integrator.setMaxEvaluations(10);
    }

    @Test
    public void testAddStepHandlers() {
        StepHandler handler1 = new MockStepHandler();
        StepHandler handler2 = new MockStepHandler();
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(2, handlers.size());
        assertEquals(handler1, handlers.get(0));
        assertEquals(handler2, handlers.get(1));
    }

    @Test
    public void testClearStepHandlers() {
        StepHandler handler1 = new MockStepHandler();
        StepHandler handler2 = new MockStepHandler();
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        integrator.clearStepHandlers();
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertTrue(handlers.isEmpty());
    }

    @Test
    public void testIntegrateWithNoEventHandlerOrStepHandler() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();
        try {
            integrator.integrate(equations, 1.0);
            fail();
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testIntegrateWithSingleEventHandler() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();
        integrator.addEventHandler(new MockEventState(1.0), 0.01, 1.0e-6, 10, new Dopri55Stepper());
        integrator.initIntegration(0.0, new double[equations.getDimension()], 1.0);
        integrator.integrate(equations, 1.0);
        // verify event state
        MockEventState eventState = (MockEventState) integrator.getEventHandlers().get(0);
        assertEquals(1.0, eventState.getEventTime(), 1e-6);
        assertFalse(eventState.isStop());
        // verify step handlers
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(0, handlers.size());
        // reset evaluations counter
        integrator.evaluations.resetCount();
    }

    @Test
    public void testIntegrateWithSingleEventHandlerAndStepHandler() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();
        integrator.addEventHandler(new MockEventState(1.0), 0.01, 1.0e-6, 10, new Dopri55Stepper());
        integrator.addStepHandler(new MockStepHandler());
        integrator.initIntegration(0.0, new double[equations.getDimension()], 1.0);
        integrator.integrate(equations, 1.0);
        // verify event state
        MockEventState eventState = (MockEventState) integrator.getEventHandlers().get(0);
        assertEquals(1.0, eventState.getEventTime(), 1e-6);
        assertFalse(eventState.isStop());
        // verify step handlers
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(1, handlers.size());
        StepHandler stepHandler = handlers.get(0);
        assertEquals(1.0, stepHandler.getTime(), 1e-6);
        assertEquals(0.0, stepHandler.getY()[0], 1e-6);
        assertEquals(1.0, stepHandler.getY()[1], 1e-6);
        // reset evaluations counter
        integrator.evaluations.resetCount();
    }

    @Test
    public void testIntegrateWithMultipleEventHandlers() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();
        integrator.addEventHandler(new MockEventState(0.5), 0.01, 1.0e-6, 10, new Dopri55Stepper());
        integrator.addEventHandler(new MockEventState(1.5), 0.01, 1.0e-6, 10, new RungeKutta54Stepper());
        integrator.initIntegration(0.0, new double[equations.getDimension()], 2.0);
        integrator.integrate(equations, 2.0);
        // verify event states
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(2, eventHandlers.size());
        MockEventState eventState1 = (MockEventState) eventHandlers.get(0);
        assertEquals(0.5, eventState1.getEventTime(), 1e-6);
        assertFalse(eventState1.isStop());
        MockEventState eventState2 = (MockEventState) eventHandlers.get(1);
        assertEquals(1.5, eventState2.getEventTime(), 1e-6);
        assertFalse(eventState2.isStop());
        // verify step handlers
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(0, handlers.size());
        // reset evaluations counter
        integrator.evaluations.resetCount();
    }

    @Test
    public void testIntegrateWithMultipleEventHandlersAndStepHandlers() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();
        integrator.addEventHandler(new MockEventState(0.5), 0.01, 1.0e-6, 10, new Dopri55Stepper());
        integrator.addEventHandler(new MockEventState(1.5), 0.01, 1.0e-6, 10, new RungeKutta54Stepper());
        integrator.addStepHandler(new MockStepHandler());
        integrator.initIntegration(0.0, new double[equations.getDimension()], 2.0);
        integrator.integrate(equations, 2.0);
        // verify event states
        List<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(2, eventHandlers.size());
        MockEventState eventState1 = (MockEventState) eventHandlers.get(0);
        assertEquals(0.5, eventState1.getEventTime(), 1e-6);
        assertFalse(eventState1.isStop());
        MockEventState eventState2 = (MockEventState) eventHandlers.get(1);
        assertEquals(1.5, eventState2.getEventTime(), 1e-6);
        assertFalse(eventState2.isStop());
        // verify step handlers
        List<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(1, handlers.size());
        StepHandler stepHandler = handlers.get(0);
        assertEquals(1.0, stepHandler.getTime(), 1e-6);
        assertEquals(0.0, stepHandler.getY()[0], 1e-6);
        assertEquals(1.0, stepHandler.getY()[1], 1e-6);
        // reset evaluations counter
        integrator.evaluations.resetCount();
    }

    @Test
    public void testMaxEvaluations() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();
        integrator.setMaxEvaluations(5);
        integrator.initIntegration(0.0, new double[equations.getDimension()], 3.0);
        try {
            integrator.integrate(equations, 3.0);
            fail();
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testResetOccurred() {
        ExpandableStatefulODE equations = new MockExpandableStatefulODE();
        integrator.addEventHandler(new MockEventState(1.0), 0.01, 1.0e-6, 10, new Dopri55Stepper());
        integrator.initIntegration(0.0, new double[equations.getDimension()], 1.0);
        integrator.integrate(equations, 1.0);
        assertTrue(integrator.resetOccurred);
        integrator.resetOccurred = false;
        integrator.integrate(equations, 1.0);
        assertFalse(integrator.resetOccurred);
    }

    static class MockExpandableStatefulODE implements ExpandableStatefulODE {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // empty implementation
        }

        @Override
        public void setTime(double t) {
            // empty implementation
        }

        @Override
        public void setPrimaryState(double[] y) {
            // empty implementation
        }

    }

    static class MockEventState implements EventState {

        private double eventTime;
        private boolean stop;
        private EventHandler eventHandler;

        public MockEventState(double eventTime) {
            this.eventTime = eventTime;
        }

        @Override
        public double getEventTime() {
            return eventTime;
        }

        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return true;
        }

        @Override
        public void stepAccepted(double eventTime, double[] eventY) {
            this.eventTime = eventTime;
            this.stop = false;
        }

        @Override
        public void reset(double eventTime, double[] eventY) {
            this.eventTime = eventTime;
            this.stop = true;
        }

        @Override
        public EventHandler getEventHandler() {
            return eventHandler;
        }

        @Override
        public void init(double t0, double[] y0, double t) {
            // empty implementation
        }

        public void setEventHandler(EventHandler eventHandler) {
            this.eventHandler = eventHandler;
        }

    }

    static class MockStepHandler implements StepHandler {

        private double time;
        private double[] y;

        @Override
        public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
            // empty implementation
        }

        @Override
        public void init(double t0, double[] y0, double t) {
            this.time = t;
            this.y = y0;
        }

        @Override
        public double getTime() {
            return time;
        }

        @Override
        public double[] getY() {
            return y;
        }

    }

    static class MockStepInterpolator implements AbstractStepInterpolator {

        private double softCurrentTime;
        private double softPreviousTime;

        @Override
        public double getGlobalCurrentTime() {
            return softCurrentTime;
        }

        @Override
        public double getGlobalPreviousTime() {
            return softPreviousTime;
        }

        @Override
        public void setInterpolatedTime(double t) {
            softCurrentTime = t;
        }

        @Override
        public double[] getInterpolatedState() {
            return new double[0];
        }

        @Override
        public void setInterpolatedState(double[] y) {
            // empty implementation
        }

        @Override
        public void setSoftPreviousTime(double previousTime) {
            softPreviousTime = previousTime;
        }

        @Override
        public void setSoftCurrentTime(double currentTime) {
            softCurrentTime = currentTime;
        }

        @Override
        public Stepper getStepper() {
            return new Dopri55Stepper();
        }

    }

}