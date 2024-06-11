package org.apache.commons.math3.ode;

import org.junit.Test;

public class AbstractIntegratorTest {
    @Test
    public void testGetName() {
        AbstractIntegrator integrator = new MockIntegrator();
        assertEquals("MockIntegrator", integrator.getName());
    }
    
    @Test
    public void testAddStepHandler() {
        AbstractIntegrator integrator = new MockIntegrator();
        StepHandler handler = new MockStepHandler();
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        assertTrue(integrator.getStepHandlers().contains(handler));
    }
    
    @Test
    public void testClearStepHandlers() {
        AbstractIntegrator integrator = new MockIntegrator();
        StepHandler handler = new MockStepHandler();
        integrator.addStepHandler(handler);
        assertEquals(1, integrator.getStepHandlers().size());
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testAddEventHandler() {
        AbstractIntegrator integrator = new MockIntegrator();
        EventHandler handler = new MockEventHandler();
        integrator.addEventHandler(handler, 1.0, 0.5, 100);
        assertEquals(1, integrator.getEventHandlers().size());
        assertTrue(integrator.getEventHandlers().contains(handler));
    }
    
    @Test
    public void testClearEventHandlers() {
        AbstractIntegrator integrator = new MockIntegrator();
        EventHandler handler = new MockEventHandler();
        integrator.addEventHandler(handler, 1.0, 0.5, 100);
        assertEquals(1, integrator.getEventHandlers().size());
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }
    
    @Test
    public void testGetMaxEvaluations() {
        AbstractIntegrator integrator = new MockIntegrator();
        assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        AbstractIntegrator integrator = new MockIntegrator();
        assertEquals(0, integrator.getEvaluations());
    }
    
    @Test
    public void testIntegration() {
        AbstractIntegrator integrator = new MockIntegrator();
        FirstOrderDifferentialEquations equations = new MockDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = new double[] {0.0, 0.0};
        double t = 1.0;
        double[] y = new double[] {0.0, 0.0};
        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    private static class MockIntegrator extends AbstractIntegrator {
        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) {
            // do nothing
        }
    }
    
    private static class MockDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // do nothing
        }
        
        @Override
        public int getDimension() {
            return 2;
        }
    }
    
    private static class MockStepHandler implements StepHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
            // do nothing
        }
        
        @Override
        public void handleStep(StepInterpolator interpolator, boolean isLast) {
            // do nothing
        }
    }
    
    private static class MockEventHandler implements EventHandler {
        @Override
        public void init(double t0, double[] y0, double t) {
            // do nothing
        }
        
        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            return Action.CONTINUE;
        }
        
        @Override
        public void resetState(double t, double[] y) {
            // do nothing
        }
    }
}