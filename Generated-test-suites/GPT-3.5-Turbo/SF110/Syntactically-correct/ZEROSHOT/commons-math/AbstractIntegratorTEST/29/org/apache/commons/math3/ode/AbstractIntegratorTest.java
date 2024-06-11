package org.apache.commons.math3.ode;

import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Incrementor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("name");
        assertEquals("name", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("name");
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {}

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler);
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertEquals(1, stepHandlers.size());
        assertTrue(stepHandlers.contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("name");
        StepHandler handler = new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {}

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertEquals(0, stepHandlers.size());
        assertFalse(stepHandlers.contains(handler));
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("name");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {}

            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }

            @Override
            public double g(double t, double[] y) {
                return 0;
            }
        };
        integrator.addEventHandler(eventHandler, 1.0, 1.0, 10);
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("name");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {}

            @Override
            public Action eventOccurred(double t, double[] y, boolean increasing) {
                return Action.CONTINUE;
            }

            @Override
            public double g(double t, double[] y) {
                return 0;
            }
        };
        integrator.addEventHandler(eventHandler, 1.0, 1.0, 10);
        integrator.clearEventHandlers();
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(0, eventHandlers.size());
        assertFalse(eventHandlers.contains(eventHandler));
    }

    private static class AbstractIntegratorImpl extends AbstractIntegrator {
        public AbstractIntegratorImpl(String name) {
            super(name);
        }

        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {

        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {

        }
    }

}