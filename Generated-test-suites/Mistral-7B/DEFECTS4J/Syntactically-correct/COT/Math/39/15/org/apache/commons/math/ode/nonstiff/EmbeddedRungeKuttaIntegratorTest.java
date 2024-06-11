package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    

    @Test
    public void testEmbeddedRungeKuttaIntegratorBasicFunctionality() {
        double[] initialValues = new double[]{1.0, 1.0};
        double[] constants = new double[]{0.0, 0.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE("Test ODE", 2, initialValues) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false,
                new double[]{0.3, 0.4, 0.1}, new double[][]{{0.0, 0.44444444444444444444, 0.0},
                {0.0, 0.27777777777777777777, 0.44444444444444444444},
                {0.0, 0.14285714285714285714, 0.61111111111111111111},
                {0.0, 0.03602856466067441861, 0.76923076923076923077}},
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.abs(y1[i] - y0[i]);
                }
                return error;
            }
        };

        integrator.integrate(equations, 2.0);

        // Check the results
        double[] finalValues = equations.getCompleteState();
        assertEquals(1.0, finalValues[0], 0.001);
        assertEquals(-1.0, finalValues[1], 0.001);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testEmbeddedRungeKuttaIntegratorInvalidMinStep() {
        double[] initialValues = new double[]{1.0, 1.0};
        double[] constants = new double[]{0.0, 0.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE("Test ODE", 2, initialValues) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false,
                new double[]{0.3, 0.4, 0.1}, new double[][]{{0.0, 0.44444444444444444444, 0.0},
                {0.0, 0.27777777777777777777, 0.44444444444444444444},
                {0.0, 0.14285714285714285714, 0.61111111111111111111},
                {0.0, 0.03602856466067441861, 0.76923076923076923077}},
                new RungeKuttaStepInterpolator(), -0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.abs(y1[i] - y0[i]);
                }
                return error;
            }
        };

        integrator.integrate(equations, 2.0);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorInvalidMaxStep() {
        double[] initialValues = new double[]{1.0, 1.0};
        double[] constants = new double[]{0.0, 0.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE("Test ODE", 2, initialValues) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false,
                new double[]{0.3, 0.4, 0.1}, new double[][]{{0.0, 0.44444444444444444444, 0.0},
                {0.0, 0.27777777777777777777, 0.44444444444444444444},
                {0.0, 0.14285714285714285714, 0.61111111111111111111},
                {0.0, 0.03602856466067441861, 0.76923076923076923077}},
                new RungeKuttaStepInterpolator(), 0.001, -0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.abs(y1[i] - y0[i]);
                }
                return error;
            }
        };

        try {
            integrator.integrate(equations, 2.0);
            fail("Expected MathIllegalArgumentException to be thrown.");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorExceptionHandling() {
        double[] initialValues = new double[]{1.0, 1.0};
        double[] constants = new double[]{0.0, 0.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE("Test ODE", 2, initialValues) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new MathIllegalArgumentException("Exception during derivatives computation");
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false,
                new double[]{0.3, 0.4, 0.1}, new double[][]{{0.0, 0.44444444444444444444, 0.0},
                {0.0, 0.27777777777777777777, 0.44444444444444444444},
                {0.0, 0.14285714285714285714, 0.61111111111111111111},
                {0.0, 0.03602856466067441861, 0.76923076923076923077}},
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new MathIllegalArgumentException("Exception during error estimation");
            }
        };

        try {
            integrator.integrate(equations, 2.0);
            fail("Expected MathIllegalStateException to be thrown.");
        } catch (MathIllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorEdgeCases() {
        double[] initialValues = new double[]{1.0, 1.0};
        double[] constants = new double[]{0.0, 0.0};
        ExpandableStatefulODE equations = new ExpandableStatefulODE("Test ODE", 2, initialValues) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.pow(y[0], 2.0);
                yPrime[1] = Math.pow(y[1], 2.0);
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false,
                new double[]{0.3, 0.4, 0.1}, new double[][]{{0.0, 0.44444444444444444444, 0.0},
                {0.0, 0.27777777777777777777, 0.44444444444444444444},
                {0.0, 0.14285714285714285714, 0.61111111111111111111},
                {0.0, 0.03602856466067441861, 0.76923076923076923077}},
                new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.abs(Math.pow(y1[i], 2.0) - Math.pow(y0[i], 2.0));
                }
                return error;
            }
        };

        integrator.integrate(equations, 1.0);
        double[] results = equations.getCompleteState();
        assertEquals(1.0, results[0], 0.001);
        assertEquals(1.0, results[1], 0.001);

        integrator.integrate(equations, 2.0);
        results = equations.getCompleteState();
        assertEquals(Math.pow(1.0, 2.0), results[0], 0.001);
        assertEquals(Math.pow(1.0, 2.0), results[1], 0.001);

        integrator.integrate(equations, 3.0);
        results = equations.getCompleteState();
        assertEquals(Math.pow(Math.pow(1.0, 2.0), 2.0), results[0], 0.001);
        assertEquals(Math.pow(Math.pow(1.0, 2.0), 2.0), results[1], 0.001);

        integrator.integrate(equations, 4.0);
        results = equations.getCompleteState();
        assertEquals(Math.pow(Math.pow(Math.pow(1.0, 2.0), 2.0), 2.0), results[0], 0.001);
        assertEquals(Math.pow(Math.pow(Math.pow(1.0, 2.0), 2.0), 2.0), results[1], 0.001);
    }

    
}