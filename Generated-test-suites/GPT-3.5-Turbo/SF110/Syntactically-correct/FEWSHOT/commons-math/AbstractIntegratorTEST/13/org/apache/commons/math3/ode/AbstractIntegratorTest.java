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
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                // Not implemented for testing purposes
            }
        };
    }
    
    @Test
    public void testAddStepHandler() {
        StepHandler handler = new MockStepHandler();
        integrator.addStepHandler(handler);
        
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        
        assertTrue(stepHandlers.contains(handler));
    }
    
    @Test
    public void testClearStepHandlers() {
        StepHandler handler = new MockStepHandler();
        integrator.addStepHandler(handler);
        integrator.clearStepHandlers();
        
        Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
        
        assertFalse(stepHandlers.contains(handler));
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler handler = new MockEventHandler();
        double maxCheckInterval = 1.0;
        double convergence = 0.01;
        int maxIterationCount = 100;
        
        integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
        
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        
        assertTrue(eventHandlers.contains(handler));
    }
    
    @Test
    public void testClearEventHandlers() {
        EventHandler handler = new MockEventHandler();
        integrator.addEventHandler(handler, 1.0, 0.01, 100);
        integrator.clearEventHandlers();
        
        Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
        
        assertFalse(eventHandlers.contains(handler));
    }
    
    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 1000;
        integrator.setMaxEvaluations(maxEvaluations);
        
        assertEquals(maxEvaluations, integrator.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        int evaluations = 10;
        MockIntegrator mockIntegrator = new MockIntegrator();
        mockIntegrator.setEvaluations(evaluations);
        
        assertEquals(evaluations, mockIntegrator.getEvaluations());
    }
    
    @Test
    public void testSetName() {
        String name = "Test Integrator";
        AbstractIntegrator testIntegrator = new AbstractIntegratorTestClass(name);
        
        assertEquals(name, testIntegrator.getName());
    }
    
    @Test
    public void testIntegrate() {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        
        MockIntegrator mockIntegrator = new MockIntegrator();
        mockIntegrator.integrate(equations, t0, y0, t, y);
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_ThrowsDimensionMismatchException() {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length - 1];
        
        ExpandableStatefulODE equations = new ExpandableStatefulODE();
        
        integrator.integrate(equations, t0, y0, t, y);
    }
    
    @Test
    public void testComputeDerivatives() {
        double t = 0.0;
        double[] y = {1.0, 2.0};
        double[] yDot = new double[y.length];
        
        MockIntegrator mockIntegrator = new MockIntegrator();
        mockIntegrator.computeDerivatives(t, y, yDot);
    }
    
    private class MockStepHandler implements StepHandler {
        public void init(double t0, double[] y0, double t) {
        }
        public void handleStep(StepInterpolator interpolator, boolean isLast) {
        }
    }
    
    private class MockEventHandler implements EventHandler {
        public void init(double t0, double[] y0, double t) {
        }
        public void handleStep(StepInterpolator interpolator, boolean isLast) {
        }
        public Action eventOccurred(StepInterpolator interpolator, boolean increasing) {
            return Action.CONTINUE;
        }
        public double g(double t, double[] y) {
            return 0.0;
        }
        public void resetState(double t, double[] y) {
        }
    }
    
    private class MockIntegrator extends AbstractIntegrator {
        private int evaluations;
        
        public void setEvaluations(int evaluations) {
            this.evaluations = evaluations;
        }
        
        @Override
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // Not implemented for testing purposes
        }
        
        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
            evaluations++;
        }
        
        @Override
        public int getEvaluations() {
            return evaluations;
        }
    }
    
    private class AbstractIntegratorTestClass extends AbstractIntegrator {
        public AbstractIntegratorTestClass(String name) {
            super(name);
        }
        
        public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
            // Not implemented for testing purposes
        }
    }
}