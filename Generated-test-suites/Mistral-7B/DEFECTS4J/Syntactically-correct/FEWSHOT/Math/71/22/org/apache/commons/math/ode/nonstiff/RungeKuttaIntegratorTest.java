package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.Integrator;
import org.apache.commons.math.ode.events.DefaultValueEvent;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.apache.commons.math.ode.stepper.RungeKuttaStepInterpolator;

/**
 * tests for RungeKuttaIntegrator class
 */
public class RungeKuttaIntegratorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructor() {
        new RungeKuttaIntegrator(null, null, null, null, null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        double[] c = new double[] {1.0, 1.0};
        double[][] a = new double[][] {{1.0, 0.0}, {0.5, 0.5}};
        double[] b = new double[] {0.5, 0.5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        new RungeKuttaIntegrator(null, c, a, b, prototype, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullC() {
        String name = "RungeKuttaIntegrator";
        double[] c = null;
        double[][] a = new double[][] {{1.0, 0.0}, {0.5, 0.5}};
        double[] b = new double[] {0.5, 0.5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        new RungeKuttaIntegrator(name, c, a, b, prototype, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullA() {
        String name = "RungeKuttaIntegrator";
        double[] c = new double[] {1.0, 1.0};
        double[][] a = null;
        double[] b = new double[] {0.5, 0.5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        new RungeKuttaIntegrator(name, c, a, b, prototype, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullB() {
        String name = "RungeKuttaIntegrator";
        double[] c = new double[] {1.0, 1.0};
        double[][] a = new double[][] {{1.0, 0.0}, {0.5, 0.5}};
        double[] b = null;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        new RungeKuttaIntegrator(name, c, a, b, prototype, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPrototype() {
        String name = "RungeKuttaIntegrator";
        double[] c = new double[] {1.0, 1.0};
        double[][] a = new double[][] {{1.0, 0.0}, {0.5, 0.5}};
        double[] b = new double[] {0.5, 0.5};
        RungeKuttaStepInterpolator prototype = null;
        new RungeKuttaIntegrator(name, c, a, b, prototype, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStep() {
        String name = "RungeKuttaIntegrator";
        double[] c = new double[] {1.0, 1.0};
        double[][] a = new double[][] {{1.0, 0.0}, {0.5, 0.5}};
        double[] b = new double[] {0.5, 0.5};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        new RungeKuttaIntegrator(name, c, a, b, prototype, -0.01);
    }

    @Test
    public void testEmptyArrays() {
        String name = "RungeKuttaIntegrator";
        double[] c = new double[] {};
        double[][] a = new double[][] {};
        double[] b = new double[] {};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        new RungeKuttaIntegrator(name, c, a, b, prototype, 0.01);
    }

    @Test
    public void testStepIntegration() {
        String name = "RungeKuttaIntegrator";
        double[] c = new double[] {0.3, 0.4, 0.3};
        double[][] a = new double[][] {{0.23692688, 0.0, 0.0},
                                        {0.67872412, 0.4, 0.0},
                                        {0.23692688, 0.0, 0.4}};
        double[] b = new double[] {0.25, 0.5, 0.25};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        Integrator integrator = new RungeKuttaIntegrator(name, c, a, b, prototype, 0.01);

        double[] y0 = new double[] {0.0, 1.0};
        double t0 = 0.0, t = 1.0;

        FirstOrderDifferentialEquations equations = new AbstractDummyEquations();
        double[] result = integrator.integrate(equations, t0, y0, t, new double[y0.length]);
        assertEquals(t, result[0], 0.001);
        assertArrayEquals(y0, result, 0.001);
    }

    @Test
    public void testStepIntegrationWithEvents() {
        String name = "RungeKuttaIntegrator";
        double[] c = new double[] {0.3, 0.4, 0.3};
        double[][] a = new double[][] {{0.23692688, 0.0, 0.0},
                                        {0.67872412, 0.4, 0.0},
                                        {0.23692688, 0.0, 0.4}};
        double[] b = new double[] {0.25, 0.5, 0.25};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        Integrator integrator = new RungeKuttaIntegrator(name, c, a, b, prototype, 0.01);

        double[] y0 = new double[] {0.0, 1.0};
        double t0 = 0.0, t1 = 1.1, t2 = 1.5;
        final double[] expected = new double[] {1.0, 1.1};

        FirstOrderDifferentialEquations equations = new AbstractDummyEquations() {
            @Override
            public int getNumberOfEquations() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * 0.5;
                }
            }
        };

        double[] result = new double[y0.length];
        integrator.addEvent(new DefaultValueEvent(1.1, new double[] {1.0, 1.1}, (f, y) -> {
            System.arraycopy(y, 0, result, 0, result.length);
        }));

        integrator.integrate(equations, t0, y0, t1, result);
        assertArrayEquals(expected, result, 0.001);

        result = new double[y0.length];
        integrator.integrate(equations, t1, result, t2, result);
        assertArrayEquals(new double[] {1.5, 1.5}, result, 0.001);
    }

    @Test(expected = IntegratorException.class)
    public void testStepIntegrationWithMaxCountExceeded() {
        String name = "RungeKuttaIntegrator";
        double[] c = new double[] {1.0};
        double[][] a = new double[][] {{1.0, 0.0}};
        double[] b = new double[] {1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        Integrator integrator = new RungeKuttaIntegrator(name, c, a, b, prototype, 0.0001);

        double[] y0 = new double[] {0.0};
        double t0 = 0.0, t = 0.0;

        FirstOrderDifferentialEquations equations = new AbstractDummyEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                double sum = 0.0;
                for (int i = 0; i < 1000000; ++i) {
                    sum += Math.sin(i) * y[0];
                }
                yPrime[0] = sum;
            }
        };

        integrator.integrate(equations, t0, y0, t, y0);
        integrator.resetEvaluations();
        integrator.integrate(equations, t, y0, t, y0);
        integrator.resetEvaluations();
        integrator.integrate(equations, t, y0, t, y0);
        integrator.resetEvaluations();
        integrator.integrate(equations, t, y0, t, y0);
        integrator.resetEvaluations();
        integrator.integrate(equations, t, y0, t, y0);
        integrator.resetEvaluations();
        integrator.integrate(equations, t, y0, t, y0);
        integrator.resetEvaluations();
        integrator.integrate(equations, t, y0, t, y0);
        integrator.resetEvaluations();
        integrator.integrate(equations, t, y0, t, y0);

        integrator.integrate(equations, t, y0, t, y0);
    }

    private static class AbstractDummyEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getNumberOfEquations() {
            return 2;
        }

    }

}