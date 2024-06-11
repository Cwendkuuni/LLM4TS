package org.apache.commons.math3.ode;

import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.sampling.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegratorImpl();
    }

    private class AbstractIntegratorImpl extends AbstractIntegrator {
        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {}

        @Override
        public double getCurrentStepStart() {
            return 0.0;
        }

        @Override
        public double getCurrentSignedStepsize() {
            return 0.0;
        }
    }

    @Test
    public void testGetName() {
        assertEquals(null, integrator.getName());

        AbstractIntegrator customIntegrator = new AbstractIntegratorImpl("Custom");
        assertEquals("Custom", customIntegrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };

        integrator.addStepHandler(handler);

        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(1, handlers.size());
        assertTrue(handlers.contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        StepHandler handler = new StepHandler() {
            public void init(double t0, double[] y0, double t) {}
            public void handleStep(StepInterpolator interpolator, boolean isLast) {}
        };

        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();

        Collection<StepHandler> handlers = integrator.getStepHandlers();
        assertEquals(0, handlers.size());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) { return 0.0; }
            public Action eventOccurred(double t, double[] y, boolean increasing) { return Action.CONTINUE; }
            public void resetState(double t, double[] y) {}
        };

        integrator.addEventHandler(handler, 0.0, 0.0, 0);

        Collection<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(1, handlers.size());
        assertTrue(handlers.contains(handler));
    }

    @Test
    public void testClearEventHandlers() {
        EventHandler handler = new EventHandler() {
            public void init(double t0, double[] y0, double t) {}
            public double g(double t, double[] y) { return 0.0; }
            public Action eventOccurred(double t, double[] y, boolean increasing) { return Action.CONTINUE; }
            public void resetState(double t, double[] y) {}
        };

        integrator.addEventHandler(handler, 0.0, 0.0, 0);
        integrator.clearEventHandlers();

        Collection<EventHandler> handlers = integrator.getEventHandlers();
        assertEquals(0, handlers.size());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());

        integrator.setMaxEvaluations(100);
        assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() { return 0; }
            public void computeDerivatives(double t, double[] y, double[] yDot) {}
        };
        double t0 = 0.0;
        double[] y0 = new double[0];
        double t = 1.0;
        double[] y = new double[0];
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (DimensionMismatchException e) {
            assertEquals("Length mismatch: expected 0, got 0", e.getMessage());
        }
    }

    @Test
    public void testComputeDerivatives() {
        double t = 0.0;
        double[] y = new double[0];
        double[] yDot = new double[0];
        try {
            integrator.computeDerivatives(t, y, yDot);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (DimensionMismatchException e) {
            assertEquals("Length mismatch: expected 0, got 0", e.getMessage());
        }
    }
}