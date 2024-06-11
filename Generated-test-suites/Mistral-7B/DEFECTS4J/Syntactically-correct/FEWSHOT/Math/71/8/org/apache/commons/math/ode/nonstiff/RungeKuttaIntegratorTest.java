package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.noexcept.NoExceptionStepHandler;
import org.junit.Test;

public class RungeKuttaIntegratorTest {
    @Test
    public void testIntegrator() {
        double[][] a = {{0.0, 0.5, 0.5, 0.5}, {0.0, 0, 0.5, 0.5}, {0.0, 0, 0, 1.0}};
        double[] b = {0.5, 0.5, 1.0};
        double[] c = {0.0, 0.5, 0.5};
        double step = 0.1;

        RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", c, a, b, new DummyStepInterpolator(), step);

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double[] initialValues = new double[]{0.0, 0.0};
        double[] result = new double[]{0.1, 0.101};

        double t = integrator.integrate(equations, 0.0, initialValues, 0.1, result);

        assertEquals(0.1, t, 0.0001);
        assertArrayEquals(result, initialValues, 0.0001);

        // Test with a step handler
        integrator = new ClassicalRungeKuttaIntegrator("Runge-Kutta23", c, a, b, new DummyStepInterpolator(), step);
        integrator.addStepHandler(new NoExceptionStepHandler());

        double[] initialValues2 = new double[]{0.0, 0.0};
        double[] result2 = new double[]{0.1, 0.100512732}; // expected value from the Runge-Kutta23 algorithm

        t = integrator.integrate(equations, 0.0, initialValues2, 0.1, result2);

        assertEquals(0.1, t, 0.0001);
        assertArrayEquals(result2, initialValues2, 0.0001);
    }
}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        System.arraycopy(y, 0, yPrime, 0, y.length);
    }
}