
package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {
    
    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double t0;
    private double[] y0;
    private double t;
    private double[] y;
    private DummyStepInterpolator interpolator;
    private StepHandler handler;
    private CombinedEventsManager manager;
    
    @Before
    public void setUp() {
        // initialize test objects
        equations = mock(FirstOrderDifferentialEquations.class);
        
        t0 = 0.0;
        y0 = new double[2];
        t = 1.0;
        y = new double[2];
        
        interpolator = mock(DummyStepInterpolator.class);
        
        handler = mock(StepHandler.class);
        
        manager = mock(CombinedEventsManager.class);
        
        // create the integrator
        double[] c = {0.0, 0.5, 1.0};
        double[][] a = {{0.0, 0.0, 0.0}, {0.5, 0.0, 0.0}, {0.0, 1.0, 0.0}};
        double[] b = {0.16666666666666666, 0.6666666666666666, 0.16666666666666666};
        interpolator.setTime(0.0);
        integrator = new RungeKuttaIntegratorImpl("TestIntegrator", c, a, b, interpolator, 0.1);
        integrator.addStepHandler(handler);
        integrator.addEventHandler(manager);

    }
    
    @After
    public void tearDown() {
        integrator = null;
        equations = null;
        interpolator = null;
        handler = null;
        manager = null;
    }
    
    @Test
    public void testIntegrate() throws Exception {
        when(interpolator.getStepStart()).thenReturn(t0);
        when(interpolator.getStepSize()).thenReturn(0.1);
        when(interpolator.getPreviousTime()).thenReturn(t0);
        when(interpolator.getInterpolatedState()).thenReturn(y);
        
        when(manager.evaluateStep(interpolator)).thenReturn(false);

        double stopTime = integrator.integrate(equations, t0, y0, t, y);
        
        assertNotNull(stopTime);
        assertEquals(t, stopTime, 0.0);
    }
    
    @Test(expected = IntegratorException.class)
    public void testIntegrateFailed() throws Exception {
        when(interpolator.getStepStart()).thenReturn(t0);
        when(interpolator.getStepSize()).thenReturn(0.1);
        when(interpolator.getPreviousTime()).thenReturn(t0);
        when(interpolator.getInterpolatedState()).thenReturn(y);
        
        when(manager.evaluateStep(interpolator)).thenReturn(true);
        
        double stopTime = integrator.integrate(equations, t0, y0, t, y);
        
        assertNull(stopTime);
    }
}
