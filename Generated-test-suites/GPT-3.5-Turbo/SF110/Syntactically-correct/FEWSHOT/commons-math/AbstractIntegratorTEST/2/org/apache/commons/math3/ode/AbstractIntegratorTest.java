package org.apache.commons.math3.ode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.Incrementor;
import org.junit.Test;

public class AbstractIntegratorTest {
    @Test
    public void testAbstractIntegrator() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("Test");
        
        assertEquals("Test", integrator.getName());
        assertTrue(integrator.getStepHandlers().isEmpty());
        assertTrue(integrator.getEventHandlers().isEmpty());
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0);
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0);
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
    }
    
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("Test");
        StepHandler handler = new StepHandlerTestImpl();
        
        assertTrue(integrator.getStepHandlers().isEmpty());
        
        integrator.addStepHandler(handler);
        
        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(handler));
    }
    
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("Test");
        StepHandler handler1 = new StepHandlerTestImpl();
        StepHandler handler2 = new StepHandlerTestImpl();
        
        integrator.addStepHandler(handler1);
        integrator.addStepHandler(handler2);
        
        assertEquals(2, integrator.getStepHandlers().size());
        
        integrator.clearStepHandlers();
        
        assertTrue(integrator.getStepHandlers().isEmpty());
    }
    
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("Test");
        EventHandler handler = new EventHandlerTestImpl();
        
        assertTrue(integrator.getEventHandlers().isEmpty());
        
        integrator.addEventHandler(handler, 0.1, 0.01, 100);
        
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(handler));
    }
    
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("Test");
        EventHandler handler1 = new EventHandlerTestImpl();
        EventHandler handler2 = new EventHandlerTestImpl();
        
        integrator.addEventHandler(handler1, 0.1, 0.01, 100);
        integrator.addEventHandler(handler2, 0.1, 0.01, 100);
        
        assertEquals(2, integrator.getEventHandlers().size());
        
        integrator.clearEventHandlers();
        
        assertTrue(integrator.getEventHandlers().isEmpty());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("Test");
        
        assertEquals(-1, integrator.getMaxEvaluations());
        
        integrator.setMaxEvaluations(100);
        
        assertEquals(100, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testInitIntegration() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("Test");
        
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        
        // Initialize integration
        integrator.initIntegration(t0, y0, t);
        
        // Check that evaluations are reset
        assertEquals(0, integrator.getEvaluations());
    }
    
    @Test
    public void testComputeDerivatives() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("Test");
        
        double t = 0.0;
        double[] y = {1.0, 2.0};
        double[] yDot = new double[2];
        
        Incrementor evaluationsCounter = integrator.getEvaluationsCounter();
        
        // Compute derivatives
        integrator.computeDerivatives(t, y, yDot);
        
        // Check that evaluations counter is incremented
        assertEquals(1, evaluationsCounter.getCount());
        
        // Check that derivatives are computed correctly
        assertEquals(2.0, yDot[0], 0);
        assertEquals(-1.0, yDot[1], 0);
    }
    
    @Test
    public void testIntegrate() throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl("Test");
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquationsTestImpl();
        
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];
        
        // Integrate
        double finalTime = integrator.integrate(equations, t0, y0, t, y);
        
        // Check final time
        assertEquals(t, finalTime, 0);
        
        // Check final state
        assertEquals(2.0, y[0], 0);
        assertEquals(1.0, y[1], 0);
    }

    private static class AbstractIntegratorTestImpl extends AbstractIntegrator {
        public AbstractIntegratorTestImpl(String name) {
            super(name);
        }

        @Override
        public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {

        }
    }
    
    private static class StepHandlerTestImpl implements StepHandler {

        @Override
        public void init(double t0, double[] y0, double t) {

        }

        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLast) {

        }
    }
    
    private static class EventHandlerTestImpl implements EventHandler {

        @Override
        public void init(double t0, double[] y0, double t) {

        }

        @Override
        public double g(double t, double[] y) {
            return 0;
        }

        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            return null;
        }

        @Override
        public void resetState(double t, double[] y) {

        }
    }
    
    private static class FirstOrderDifferentialEquationsTestImpl implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }
}