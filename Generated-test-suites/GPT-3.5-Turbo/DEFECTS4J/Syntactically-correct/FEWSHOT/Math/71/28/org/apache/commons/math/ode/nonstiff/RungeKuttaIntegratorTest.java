package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {
    
    private static class DummyEquations implements FirstOrderDifferentialEquations {
        public void computeDerivatives(final double t, final double[] y, final double[] yDot) throws DerivativeException {
            // Do nothing
        }
        public int getDimension() {
            return 0;
        }
    }
    
    private DummyEquations equations;
    private double t0;
    private double[] y0;
    private double t;
    private double[] y;
    private RungeKuttaStepInterpolator prototype;
    private double step;
    private RungeKuttaIntegrator integrator;
    
    @Before
    public void setUp() {
        equations = new DummyEquations();
        t0 = 0;
        y0 = new double[0];
        t = 1;
        y = new double[0];
        prototype = new RungeKuttaStepInterpolator();
        step = 0.1;
        integrator = new RungeKuttaIntegrator("", null, null, null, prototype, step) {
            @Override
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                return 0;
            }
            
        };
    }
    
    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        double result = integrator.integrate(equations, t0, y0, t, y);
        
        assertEquals(0.0, result, 1e-15);
    }
    
    @Test(expected = NullPointerException.class)
    public void testIntegrateWithNullEquations() throws DerivativeException, IntegratorException {
        double result = integrator.integrate(null, t0, y0, t, y);
    }
    
    @Test
    public void testIntegrateWithStepHandlers() throws DerivativeException, IntegratorException {
        StepHandler stepHandler1 = new StepHandler() {
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast)
                throws DerivativeException {
                // Do nothing
            }
            
            public void reset() {
                // Do nothing
            }
        };
        StepHandler stepHandler2 = new StepHandler() {
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLast)
                throws DerivativeException {
                // Do nothing
            }
            
            public void reset() {
                // Do nothing
            }
        };
        
        AbstractIntegrator integrator = new RungeKuttaIntegrator("", null, null, null, prototype, step) {
            @Override
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                for (StepHandler handler : stepHandlers) {
                    handler.reset();
                }
                return 0.0;
            }
        };
        integrator.addStepHandler(stepHandler1);
        integrator.addStepHandler(stepHandler2);
        
        integrator.integrate(equations, t0, y0, t, y);
        
        assertEquals(2, integrator.getStepHandlers().size());
    }
    
    @Test
    public void testIntegrateWithEventsHandlers() throws DerivativeException, IntegratorException {
        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEventHandler(null, 1.0, 1e-6, 100, null);
        
        AbstractIntegrator integrator = new RungeKuttaIntegrator("", null, null, null, prototype, step) {
            @Override
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                assertTrue(eventsHandlersManager.isEmpty());
                return 0.0;
            }
        };
        integrator.addEventHandlerManager(manager);
        
        integrator.integrate(equations, t0, y0, t, y);
        
        assertEquals(1, integrator.getEventHandlerManagers().size());
    }
}