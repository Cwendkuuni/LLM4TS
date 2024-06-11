package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {
    private RungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        double[] c = {1.0, 2.0};
        double[][] a = {{0.1, 0.2}, {0.3, 0.4}};
        double[] b = {0.5, 0.6};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        integrator = new RungeKuttaIntegratorImpl("Test Integrator", c, a, b, prototype, 0.01);
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.integrate(equations, t0, y0, t, y);

        // TODO: Assert something
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateWithDerivativeException() throws DerivativeException, IntegratorException {
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(2);
        doThrow(new DerivativeException()).when(equations).computeDerivatives(0.0, new double[]{1.0, 2.0}, new double[2]);

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithIntegratorException() throws DerivativeException, IntegratorException {
        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        when(equations.getDimension()).thenReturn(2);
        doThrow(new RuntimeException()).when(equations).computeDerivatives(0.0, new double[]{1.0, 2.0}, new double[2]);

        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.integrate(equations, t0, y0, t, y);
    }
}

class RungeKuttaIntegratorImpl extends RungeKuttaIntegrator {
    public RungeKuttaIntegratorImpl(String name, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double step) {
        super(name, c, a, b, prototype, step);
    }

    @Override
    protected void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
        // TODO: Implement computeDerivatives
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        // TODO: Implement estimateError
        return 0;
    }
}