package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {
    private AbstractIntegrator integrator;
    
    @Before
    public void setUp() {
        integrator = new TestIntegrator();
    }
    
    @Test
    public void testGetName() {
        assertEquals("Test Integrator", integrator.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        StepHandler handler = new TestStepHandler();
        integrator.addStepHandler(handler);
        assertTrue(integrator.getStepHandlers().contains(handler));
    }
    
    @Test
    public void testClearStepHandlers() {
        StepHandler handler = new TestStepHandler();
        integrator.addStepHandler(handler);
        
        assertFalse(integrator.getStepHandlers().isEmpty());
        integrator.clearStepHandlers();
        assertTrue(integrator.getStepHandlers().isEmpty());
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler handler = new TestEventHandler();
        double maxCheckInterval = 1.0;
        double convergence = 0.01;
        int maxIterationCount = 10;
        
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
        
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        assertTrue(eventHandlers.contains(handler));
    }
    
    @Test
    public void testClearEventHandlers() {
        EventHandler handler = new TestEventHandler();
        double maxCheckInterval = 1.0;
        double convergence = 0.01;
        int maxIterationCount = 10;
        
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
        
        assertFalse(integrator.getEventHandlers().isEmpty());
        integrator.clearEventHandlers();
        assertTrue(integrator.getEventHandlers().isEmpty());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, integrator.getEvaluations());
    }
    
    @Test
    public void testSetEquations() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new TestODE());
        integrator.setEquations(equations);
        assertEquals(equations, integrator.getExpandable());
    }

    
    class TestIntegrator extends AbstractIntegrator {
        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) {
            // Some implementation
        }
    }
    
    class TestStepHandler implements StepHandler {
        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLast) {
            // Some implementation
        }
        
        @Override
        public void init(double t0, double[] y0, double t) {
            // Some implementation
        }
    }
    
    class TestEventHandler implements EventHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
            // Some implementation
        }
        
        @Override
        public double g(double t, double[] y) {
            return 0.0;
        }
        
        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            return Action.CONTINUE;
        }
        
        @Override
        public void resetState(double t, double[] y) {
            // Some implementation
        }
    }
    
    class TestODE implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 0;
        }
        
        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // Some implementation
        }
    }
}