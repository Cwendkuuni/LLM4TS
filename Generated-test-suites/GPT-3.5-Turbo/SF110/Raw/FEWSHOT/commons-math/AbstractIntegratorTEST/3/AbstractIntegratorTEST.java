
package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
        assertEquals("test", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler handler = new StepHandlerImpl();
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        StepHandler handler = new StepHandlerImpl();
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
        integrator.clearStepHandlers();
        assertFalse(integrator.getStepHandlers().contains(handler));
    }

    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandler handler = new EventHandlerImpl();
        integrator.addEventHandler(handler, 1.0, 1.0, 1);
        assertTrue(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        EventHandler handler = new EventHandlerImpl();
        integrator.addEventHandler(handler, 1.0, 1.0, 1);
        assertTrue(integrator.getEventHandlers().contains(handler));
        integrator.clearEventHandlers();
        assertFalse(integrator.getEventHandlers().contains(handler));
    }

    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        assertEquals(0, integrator.getEvaluations());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_DimensionMismatchException() throws DimensionMismatchException, NoBracketingException, MaxCountExceededException {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        double[] y0 = new double[2];
        double[] y = new double[1];
        integrator.integrate(new FirstOrderDifferentialEquationsImpl(), 0, y0, 1, y);
    }

    @Test
    public void testIntegrate() throws DimensionMismatchException, NoBracketingException, MaxCountExceededException {
        AbstractIntegrator integrator = new AbstractIntegratorImpl();
        double[] y0 = new double[1];
        y0[0] = 1.0;
        double[] y = new double[1];
        double t = integrator.integrate(new FirstOrderDifferentialEquationsImpl(), 0, y0, 1, y);
        assertEquals(2.0, y[0], 1e-10);
        assertEquals(1.0, t, 1e-10);
    }

    private static class AbstractIntegratorImpl extends AbstractIntegrator {
        public AbstractIntegratorImpl(String name) {
            super(name);
        }

        public AbstractIntegratorImpl() {
            super();
        }

        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        }
    }

    private static class StepHandlerImpl implements StepHandler {
        public void init(double t0, double[] y0, double t) {
        }

        public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
        }
    }

    private static class EventHandlerImpl implements EventHandler {
        public void init(double t0, double[] y0, double t) {
        }

        public double g(double t, double[] y) throws MaxCountExceededException {
            return 0;
        }

        public Action eventOccurred(double t, double[] y, boolean increasing) throws MaxCountExceededException {
            return null;
        }

        public void resetState(double t, double[] y) throws MaxCountExceededException {
        }
    }

    private static class FirstOrderDifferentialEquationsImpl implements FirstOrderDifferentialEquations {
        public int getDimension() {
            return 1;
        }

        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = 1.0;
        }
    }
}
