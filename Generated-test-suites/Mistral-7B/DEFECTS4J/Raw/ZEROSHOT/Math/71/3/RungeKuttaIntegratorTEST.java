package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.BesselFunctions;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @org.junit.Test
    public void testIntegrate() {
        final double[] initialValues = {0.0, 1.0};
        final double start = 0.0;
        final double end = Math.PI;
        final double step = 0.1;

        final RungeKuttaIntegrator rk4 = new RungeKutta23FehlbergIntegrator(
                "RK23", new double[]{0.25, 0.45, 0.55, 0.7, 1.0},
                new double[][]{
                        {0.25, 0.0, 0.0, 0.0, 0.0},
                        {0.45, 0.25, 0.25, 0.0, 0.0},
                        {0.55, 0.45, 0.45, 0.45, 0.0},
                        {0.7, 0.55, 0.55, 0.55, 0.55},
                        {1.0, 0.7, 0.7, 0.7, 0.7}
                },
                new double[]{0.25, 0.4, 0.3, 0.875, 0.0},
                new RungeKuttaStepInterpolator());

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = -y[i] + BesselFunctions.sin(BesselFunctions.I_0(t));
                }
            }
        };

        // integrate using RK23
        double[] result = rk4.integrate(equations, start, initialValues, end, new double[initialValues.length]);
        assertEquals(end, result[0], 1e-9);
        assertArrayEquals(BesselFunctions.sin(new double[]{Math.PI, Math.PI, Math.PI, Math.PI, Math.PI}), result, 1e-9);

        // integrate using RK4
        final RungeKuttaIntegrator rk45 = new RungeKutta4Integrator("RK4", new double[]{0.5, 0.5, 0.5, 0.5},
                new double[][]{
                        {0.0, 0.0, 0.0, 0.0},
                        {0.25, 0.25, 0.25, 0.25},
                        {0.25, 0.25, 0.25, 0.25},
                        {0.5, 0.5, 0.5, 0.5}
                }, new double[]{1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0},
                new RungeKuttaStepInterpolator());

        result = rk45.integrate(equations, start, initialValues, end, new double[initialValues.length]);
        assertEquals(end, result[0], 1e-9);
        assertArrayEquals(BesselFunctions.sin(new double[]{Math.PI, Math.PI, Math.PI, Math.PI, Math.PI}), result, 1e-9);

        // test events handling
        final double[] finalValues = {BesselFunctions.sin(Math.PI), BesselFunctions.sin(Math.PI),
                BesselFunctions.sin(Math.PI), BesselFunctions.sin(Math.PI), BesselFunctions.sin(Math.PI)};
        double switchValue = Math.PI * Math.sqrt(2.0);
        when(new When() {

            @Override
            public boolean test(double t, double[] y) {
                return BesselFunctions.sin(t) > switchValue;
            }

            @Override
            public double[] value(double t, double[] y) {
                return new double[]{Math.PI * 2.0, BesselFunctions.sin(Math.PI * 2.0),
                        BesselFunctions.sin(Math.PI * 2.0), BesselFunctions.sin(Math.PI * 2.0),
                        BesselFunctions.sin(Math.PI * 2.0)};
            }

        }).addTo(rk4.getEventsManager());
        result = rk4.integrate(equations, start, initialValues, end, new double[initialValues.length]);
        assertEquals(end, result[0], 1e-9);
        assertArrayEquals(finalValues, result, 1e-9);

    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    @org.junit.Test
    public void testIntegrateWithStepHandler() {
        final double[] initialValues = {0.0, 1.0};
        final double start = 0.0;
        final double end = Math.PI;
        final double step = 0.1;

        final RungeKuttaIntegrator rk4 = new RungeKutta4Integrator("RK4", new double[]{0.5, 0.5, 0.5, 0.5},
                new double[][]{
                        {0.0, 0.0, 0.0, 0.0},
                        {0.25, 0.25, 0.25, 0.25},
                        {0.25, 0.25, 0.25, 0.25},
                        {0.5, 0.5, 0.5, 0.5}
                }, new double[]{1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0},
                new RungeKuttaStepInterpolator());

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = -y[i] + BesselFunctions.sin(BesselFunctions.I_0(t));
                }
            }
        };

        final double[] result = new double[initialValues.length];
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                final double t = context.getTime();
                final double[] y = context.getY();
                System.arraycopy(y, 0, result, 0, initialValues.length);
            }
        };
        rk4.addStepHandler(stepHandler);

        rk4.integrate(equations, start, initialValues, end, new double[initialValues.length]);
        assertEquals(end, result[0], 1e-9);
        assertArrayEquals(BesselFunctions.sin(new double[]{Math.PI, Math.PI, Math.PI, Math.PI, Math.PI}), result, 1e-9);

    }

}