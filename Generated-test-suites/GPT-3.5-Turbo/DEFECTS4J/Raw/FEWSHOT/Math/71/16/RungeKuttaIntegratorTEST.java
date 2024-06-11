package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    // Test RungeKuttaIntegrator with a mock implementation of FirstOrderDifferentialEquations
    private static class MockODE implements FirstOrderDifferentialEquations {

        private double[] y;

        public MockODE(double[] y) {
            this.y = y;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            yDot[0] = t * y[0];
        }

        @Override
        public int getDimension() {
            return 1;
        }
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        double[] y0 = {1.0};
        double t0 = 0.0;
        double t = 1.0;
        double step = 0.1;

        AbstractIntegrator integrator = new RungeKuttaIntegrator("Mock", null, null, null, null, step) {
            @Override
            public double step() throws DerivativeException, IntegratorException {
                return 0;
            }
        };
        MockODE ode = new MockODE(y0);

        double result = integrator.integrate(ode, t0, y0, t, y0);

        assertEquals(t, result, 0.0);
        assertEquals(t * y0[0], y0[0], 0.0);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateWithDerivativeException() throws DerivativeException, IntegratorException {
        double[] y0 = {1.0};
        double t0 = 0.0;
        double t = 1.0;
        double step = 0.1;

        AbstractIntegrator integrator = new RungeKuttaIntegrator("Mock", null, null, null, null, step) {
            @Override
            public double step() throws DerivativeException, IntegratorException {
                throw new DerivativeException(new RuntimeException());
            }
        };
        MockODE ode = new MockODE(y0);

        double result = integrator.integrate(ode, t0, y0, t, y0);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithIntegratorException() throws DerivativeException, IntegratorException {
        double[] y0 = {1.0};
        double t0 = 0.0;
        double t = 1.0;
        double step = 0.1;

        AbstractIntegrator integrator = new RungeKuttaIntegrator("Mock", null, null, null, null, step) {
            @Override
            public double step() throws DerivativeException, IntegratorException {
                throw new IntegratorException(new RuntimeException());
            }
        };
        MockODE ode = new MockODE(y0);

        double result = integrator.integrate(ode, t0, y0, t, y0);
    }

    @Test
    public void testStep() throws DerivativeException, IntegratorException {
        double[] y = {1.0};
        double t = 0.0;
        double dt = 0.1;

        AbstractIntegrator integrator = new RungeKuttaIntegrator("Mock", null, null, null, null, dt) {
            @Override
            public double step() throws DerivativeException, IntegratorException {
                return 0;
            }
        };
        MockODE ode = new MockODE(y);

        double result = integrator.step(t, y, dt, y);

        assertEquals(t + dt, result, 0.0);
        assertEquals((t + dt) * y[0], y[0], 0.0);
    }

    @Test(expected = DerivativeException.class)
    public void testStepWithDerivativeException() throws DerivativeException, IntegratorException {
        double[] y = {1.0};
        double t = 0.0;
        double dt = 0.1;

        AbstractIntegrator integrator = new RungeKuttaIntegrator("Mock", null, null, null, null, dt) {
            @Override
            public double step() throws DerivativeException, IntegratorException {
                throw new DerivativeException(new RuntimeException());
            }
        };
        MockODE ode = new MockODE(y);

        double result = integrator.step(t, y, dt, y);
    }

    @Test(expected = IntegratorException.class)
    public void testStepWithIntegratorException() throws DerivativeException, IntegratorException {
        double[] y = {1.0};
        double t = 0.0;
        double dt = 0.1;

        AbstractIntegrator integrator = new RungeKuttaIntegrator("Mock", null, null, null, null, dt) {
            @Override
            public double step() throws DerivativeException, IntegratorException {
                throw new IntegratorException(new RuntimeException());
            }
        };
        MockODE ode = new MockODE(y);

        double result = integrator.step(t, y, dt, y);
    }
}