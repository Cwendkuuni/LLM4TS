
package org.apache.commons.math3.ode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private TestStepHandler handler;
    private TestEquations equations;
    private DummyIntegrator integrator;

    @Before
    public void setUp() {
        handler = new TestStepHandler();
        equations = new TestEquations();
        integrator = new DummyIntegrator();
        integrator.addStepHandler(handler);
    }

    @Test
    public void testAddStepHandler() {
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertEquals(1, stepHandlers.size());
        assertTrue(stepHandlers.contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        integrator.clearStepHandlers();
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        assertEquals(0, stepHandlers.size());
        assertFalse(stepHandlers.contains(handler));
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new TestEventHandler();
        integrator.addEventHandler(eventHandler, 1.0, 0.01, 100);
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testClearEventHandlers() {
        EventHandler eventHandler = new TestEventHandler();
        integrator.addEventHandler(eventHandler, 1.0, 0.01, 100);
        integrator.clearEventHandlers();
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertEquals(0, eventHandlers.size());
        assertFalse(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        integrator.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        int evaluationsCount = 100;
        integrator.getEvaluationsCounter().incrementCount(evaluationsCount);
        assertEquals(evaluationsCount, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() {
        double[] y0 = new double[] { 0.0 };
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[y0.length];
        double result = integrator.integrate(equations, t0, y0, t, y);
        assertNotNull(result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatchException() {
        double[] y0 = new double[] { 0.0 };
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[] { 0.0, 0.0 };
        integrator.integrate(equations, t0, y0, t, y);
    }

    private static class DummyIntegrator extends AbstractIntegrator {
        @Override
        public void integrate(ExpandableStatefulODE equations, double t)
                throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        }
    }

    private static class TestStepHandler implements StepHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
        }

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLast)
                throws MaxCountExceededException {
        }
    }

    private static class TestEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot)
                throws MaxCountExceededException, DimensionMismatchException {
        }
    }

    private static class TestEventHandler implements EventHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
        }

        @Override
        public double g(double t, double[] y) {
            return 0.0;
        }

        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing)
                throws MaxCountExceededException {
            return null;
        }

        @Override
        public void resetState(double t, double[] y) {
        }
    }
}

