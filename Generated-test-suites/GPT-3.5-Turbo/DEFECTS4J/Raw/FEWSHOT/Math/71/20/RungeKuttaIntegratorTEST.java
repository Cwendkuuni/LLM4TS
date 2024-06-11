package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        double[] c = {0.0, 0.0, 0.0, 0.0};
        double[][] a = {{0.0}, {0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0, 0.0}};
        double[] b = {0.0, 0.0, 0.0, 0.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double step = 0.0;
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("test", c, a, b, prototype, step) {
            @Override
            public double getCurrentStepStart() {
                return 0.0;
            }

            @Override
            public void integrateStep(FirstOrderDifferentialEquations equations, double t, double[] y, double stepSize) throws DerivativeException {
                // Do nothing
            }
        };

        double[] y0 = {0.0};
        double[] y = {0.0};
        double t0 = 0.0;
        double t = 0.0;
        double result = integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                // Do nothing
            }
        }, t0, y0, t, y);

        assertEquals(0.0, result, 0.0);
    }
}