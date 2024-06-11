package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
        TestEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = { 1.0, 2.0, 3.0 };
        double t = 1.0;
        double[] y = new double[y0.length];

        double stopTime = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(t, stopTime, 0.0);
        assertArrayEquals(new double[] { 2.0, 4.0, 6.0 }, y, 0.0);
    }

    private static class TestRungeKuttaIntegrator extends RungeKuttaIntegrator {
        private TestRungeKuttaIntegrator() {
            super("Test", new double[] { 0.5 }, new double[][] { { 0.5 } }, new double[] { 1.0 }, null, 0.1);
        }
    }

    private static class TestEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 3;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            yDot[0] = y[0] * 2.0;
            yDot[1] = y[1] * 2.0;
            yDot[2] = y[2] * 2.0;
        }
    }
}