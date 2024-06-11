package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class RungeKuttaIntegratorTest {
    
    private RungeKuttaIntegrator integrator;

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        double[] c = {0.0, 1.0, 2.0, 3.0};
        double[][] a = {{0.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}, {0.0, 0.0, 0.0}};
        double[] b = {0.0, 0.0, 0.0, 0.0};
        RungeKuttaStepInterpolator prototype = mock(RungeKuttaStepInterpolator.class);
        double step = 0.1;

        integrator = new RungeKuttaIntegratorImpl(c, a, b, prototype, step);

        FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        double result = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(1.0, result, 0.0);
        assertArrayEquals(y0, y, 0.0);
    }

    private class RungeKuttaIntegratorImpl extends RungeKuttaIntegrator {

        public RungeKuttaIntegratorImpl(double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double step) {
            super("Test", c, a, b, prototype, step);
        }

        @Override
        protected void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {

        }
    }
}