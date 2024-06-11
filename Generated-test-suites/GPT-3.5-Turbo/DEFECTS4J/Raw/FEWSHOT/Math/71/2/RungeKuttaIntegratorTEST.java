package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private class TestEquations implements FirstOrderDifferentialEquations {

        public int getDimension() {
            return 2;
        }

        public void computeDerivatives(double t, double[] y, double[] yDot)
                throws DerivativeException {
            yDot[0] = y[0] + t;
            yDot[1] = y[1] - t;
        }
    }

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double t0;
    private double[] y0;
    private double t;
    private double[] y;

    @Before
    public void setUp() {
        integrator = new RungeKuttaIntegrator("", null, null, null, null, 0);
        equations = new TestEquations();
        t0 = 0;
        y0 = new double[]{1, 2};
        t = 1;
        y = new double[2];
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        integrator.integrate(equations, t0, y0, t, y);
        assertEquals(y0[0] + t, y[0], 1e-10);
        assertEquals(y0[1] - t, y[1], 1e-10);
    }
}