package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        double[] c = {0, 1, 2, 3};
        double[][] a = {{0, 0, 0, 0}, {1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}};
        double[] b = {0, 0, 0, 1};
        RungeKuttaStepInterpolator interpolator = new RungeKuttaStepInterpolator();
        double step = 0.1;
        integrator = new RungeKuttaIntegrator("MyIntegrator", c, a, b, interpolator, step);
        equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) throws IntegratorException {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };
    }

    @Test
    public void testIntegrate() throws Exception {
        double t0 = 0;
        double[] y0 = {0, 1};
        double t = 1;
        double[] y = new double[2];
        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(0.5403023058681398, y[0], 1e-15);
        assertEquals(-0.8414709848078965, y[1], 1e-15);
    }
}