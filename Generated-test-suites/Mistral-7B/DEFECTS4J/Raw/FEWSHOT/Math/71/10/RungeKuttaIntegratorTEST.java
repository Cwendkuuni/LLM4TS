package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testAbstractMethods() {
        assertNull(new RungeKuttaIntegrator("", null, null, null, null, 0).integrate(null, 0, new double[0], 0, new double[0]));
    }

    @Test
    public void testSanityChecks() {
        try {
            new RungeKuttaIntegrator("", new double[1], new double[][]{{0.1}}, new double[1], new RungeKuttaStepInterpolator(1), 0)
                    .integrate(null, 0, new double[0], 0, new double[0]);
            assertFalse(true);
        } catch (DeriveException | IntegratorException e) {
            // expected
        }
    }

    @Test
    public void testIntegrateEmptyEquations() {
        double[][] a = new double[][]{{0.1}};
        double[] b = new double[]{1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1);
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("", new double[0], a, b, prototype, 0.1);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 0;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                // does nothing
            }
        };
        double[] y0 = new double[0];
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[0];

        integrator.integrate(equations, t0, y0, t, y);

        assertEquals(t, integrator.getStepStart(), 0.0);
        assertEquals(t, integrator.getStepEnd(), 0.0);
        assertArrayEquals(y0, y, 0.0);
    }

    @Test
    public void testIntegrateSimpleEquations() {
        double[][] a = new double[][]{{0.1}, {0.1, 0.1}, {0.1, 0.1, 0.1}};
        double[] b = new double[]{0.5, 0.5, 0.5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("", new double[]{0.0, 0.0, 0.0}, a, b, prototype, 0.1);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 3.0 * y[0];
                yPrime[1] = 3.0 * y[1];
                yPrime[2] = 3.0 * y[2];
            }
        };
        double[] y0 = new double[]{0.0, 0.0, 0.0};
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[]{1.0, 1.0, 1.0};

        integrator.integrate(equations, t0, y0, t, y);

        assertEquals(t, integrator.getStepStart(), 0.0);
        assertEquals(t, integrator.getStepEnd(), 0.0);
        assertArrayEquals(y, y0, 0.01);
    }

}