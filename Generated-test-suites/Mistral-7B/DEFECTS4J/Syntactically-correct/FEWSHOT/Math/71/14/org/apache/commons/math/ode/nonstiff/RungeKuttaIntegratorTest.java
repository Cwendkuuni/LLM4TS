package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxIterException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.DummyDifferentialEquations;
import org.apache.commons.math3.ode.test.RungeKuttaTest;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testBasicFunction() throws Exception {
        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new DummyStepInterpolator());
        RungeKuttaTest test = new RungeKuttaTest(integrator);
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        double[] y0 = {1.0};
        double t0 = 0.0, t1 = 1.0;
        double[] y1 = {e};

        integrator.integrate(equations, t0, y0, t1, y1);

        assertArrayEquals(y1, y0);
        assertEquals(t1, test.getActualStepCount(), 0.0);
        assertEquals(t1, test.getActualStepSize(), 0.0);
    }

    @Test(expected = IntegratorException.class)
    public void testSingularMatrix() throws Exception {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator(
                0.1, new DummyStepInterpolator(), new double[]{}, new double[]{}, new RungeKuttaStepInterpolator() {
                    @Override
                    protected double[] computeJacobian(double t, double[] y, double[] dy) {
                        throw new SingularMatrixException("Singular matrix");
                    }
                });
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        double[] y0 = {1.0};
        double t0 = 0.0, t1 = 1.0;
        double[] y1 = {e};

        integrator.integrate(equations, t0, y0, t1, y1);
    }

    @Test(expected = DerivativeException.class)
    public void testFailedDerivatives() throws Exception {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator(
                0.1, new DummyStepInterpolator(), new double[]{}, new double[]{}, new RungeKuttaStepInterpolator() {
                    @Override
                    protected double[] computeDerivatives(double t, double[] y) {
                        throw new MaxIterException("Max iterations exceeded");
                    }
                });
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        double[] y0 = {1.0};
        double t0 = 0.0, t1 = 1.0;
        double[] y1 = {e};

        integrator.integrate(equations, t0, y0, t1, y1);
    }

}