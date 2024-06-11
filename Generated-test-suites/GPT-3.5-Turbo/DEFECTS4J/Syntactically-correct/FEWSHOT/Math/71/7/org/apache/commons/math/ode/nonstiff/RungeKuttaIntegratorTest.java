package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.junit.Before;
import org.junit.Test;


public class RungeKuttaIntegratorTest {
    
    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double t0;
    private double[] y0;
    private double t;
    private double[] y;

    @Before
    public void setUp() {
        integrator = new RungeKuttaIntegrator("TestIntegrator", null, null, null, null, 1.0);
        equations = mock(FirstOrderDifferentialEquations.class);
        t0 = 0.0;
        y0 = new double[] {1.0, 2.0, 3.0};
        t = 1.0;
        y = new double[y0.length];
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        double stopTime = integrator.integrate(equations, t0, y0, t, y);
        
        assertEquals(t, stopTime, 0.0);
        verify(equations).computeDerivatives(t0, y0, y);
        // TODO: add more assertions
    }    
}