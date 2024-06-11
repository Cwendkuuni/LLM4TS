package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.ode.*;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.*;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        AbstractIntegrator integrator = new RungeKuttaIntegrator("Test", null, null, null, null, 0.1);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = t;
                yDot[1] = t * t;
            }
        };
        double t0 = 0.0;
        double[] y0 = { 0.0, 1.0 };
        double t = 1.0;
        double[] y = new double[2];
        double result = integrator.integrate(equations, t0, y0, t, y);
        Assert.assertEquals(1.0, result, 1e-10);
        Assert.assertEquals(1.0, y[0], 1e-10);
        Assert.assertEquals(1.0 / 3.0, y[1], 1e-10);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateDerivativeException() throws DerivativeException, IntegratorException {
        AbstractIntegrator integrator = new RungeKuttaIntegrator("Test", null, null, null, null, 0.1);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                throw new DerivativeException();
            }
        };
        double t0 = 0.0;
        double[] y0 = { 0.0, 1.0 };
        double t = 1.0;
        double[] y = new double[2];
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateIntegratorException() throws DerivativeException, IntegratorException {
        AbstractIntegrator integrator = new RungeKuttaIntegrator("Test", null, null, null, null, 0.1);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = t;
                yDot[1] = t * t;
            }
        };
        double t0 = 0.0;
        double[] y0 = { 0.0, 1.0 };
        double t = 1.0;
        double[] y = new double[1];
        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test
    public void testIntegrateWithStepHandler() throws DerivativeException, IntegratorException {
        AbstractIntegrator integrator = new RungeKuttaIntegrator("Test", null, null, null, null, 0.1);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = t;
                yDot[1] = t * t;
            }
        };
        double t0 = 0.0;
        double[] y0 = { 0.0, 1.0 };
        double t = 1.0;
        double[] y = new double[2];
        DummyStepHandler handler = new DummyStepHandler();
        integrator.addStepHandler(handler);
        double result = integrator.integrate(equations, t0, y0, t, y);
        Assert.assertEquals(1.0, result, 1e-10);
        Assert.assertEquals(1.0, y[0], 1e-10);
        Assert.assertEquals(1.0 / 3.0, y[1], 1e-10);
        Assert.assertEquals(10, handler.getSteps());
    }

    public class DummyStepHandler implements StepHandler {
        int steps = 0;

        public void handleStep(StepInterpolator interpolator, boolean isLast) {
            steps++;
        }

        public void reset() {
            steps = 0;
        }

        public int getSteps() {
            return steps;
        }
    }
}