package org.apache.commons.math3.ode;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.analysis.solvers.UnivariateSolver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AbstractIntegratorTest {
    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("Test Name");
        String name = integrator.getName();
        assertEquals("Test Name", name);
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler handler = new StepHandler() {
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
            public boolean requiresDenseOutput() { return false; }
            public void reset() {}
        };
        integrator.addStepHandler(handler);
        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertTrue(handlers.contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler handler = new StepHandler() {
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
            public boolean requiresDenseOutput() { return false; }
            public void reset() {}
        };
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertFalse(handlers.contains(handler));
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandler eventHandler = new EventHandler() {
            public Action eventOccurred(double t, double[] y, boolean increasing) { return Action.CONTINUE; }
            public double g(double t, double[] y) { return 0; }
            public void init(double t0, double[] y0, double t) {}
            public void resetState(double t, double[] y) {}
        };
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 100);
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertTrue(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandler eventHandler = new EventHandler() {
            public Action eventOccurred(double t, double[] y, boolean increasing) { return Action.CONTINUE; }
            public double g(double t, double[] y) { return 0; }
            public void init(double t0, double[] y0, double t) {}
            public void resetState(double t, double[] y) {}
        };
        integrator.addEventHandler(eventHandler, 0.1, 0.01, 100);
        integrator.clearEventHandlers();
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertFalse(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        integrator.setMaxEvaluations(100);
        int maxEvaluations = integrator.getMaxEvaluations();
        assertEquals(100, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        integrator.setMaxEvaluations(100);
        int evaluations = integrator.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        double t0 = 0.0;
        double[] y0 = new double[] {1.0, 2.0, 3.0};
        double t = 1.0;
        integrator.initIntegration(t0, y0, t);
        // no assertion possible as this method only performs side effects
    }

    @Test
    public void testSetEquations() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        ExpandableStatefulODE equations = new ExpandableStatefulODE(null);
        integrator.setEquations(equations);
        ExpandableStatefulODE result = integrator.getExpandable();
        assertEquals(equations, result);
    }

    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        double t = 0.0;
        double[] y = new double[] {1.0, 2.0, 3.0};
        double[] yDot = new double[3];
        integrator.computeDerivatives(t, y, yDot);
        // no assertion possible as this method only performs side effects
    }

    private static class AbstractIntegratorImpl extends AbstractIntegrator {
        public AbstractIntegratorImpl() {
            super();
        }
        public AbstractIntegratorImpl(String name) {
            super(name);
        }
        @Override
        public void integrate(ExpandableStatefulODE equations, double t) {}
    }
}