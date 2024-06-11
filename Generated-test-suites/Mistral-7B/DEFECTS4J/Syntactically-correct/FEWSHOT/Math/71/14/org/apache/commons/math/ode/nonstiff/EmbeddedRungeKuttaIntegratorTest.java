package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.noperturb.RkStepInterpolator;
import org.apache.commons.math3.ode.noperturb.RkStepInterpolator.StepHandler;
import org.apache.commons.math3.ode.noperturb.RkWadada55;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testWadada55() {
        EmbeddedRungeKuttaIntegrator integrator = new RkWadada55("Wadada55", false,
                new double[]{0.3, 0.25, 0.5, 0.875, 0.625},
                new double[][]{
                        {0.3333333333333333, 0.0, 0.0, 0.0, 0.0, 0.0},
                        {0.5, 0.25, 0.25, 0.0, 0.0, 0.0},
                        {0.8, 0.6, 0.2, 0.2, 0.0, 0.0},
                        {0.1, 0.6, 0.75, 0.45, 0.2, 0.0},
                        {0.3666666666666667, 0.3, 0.3, 0.3, 0.0, 0.0},
                        {0.6888888888888887, 0.4666666666666666, 0.4, 0.2, 0.0, 0.0}},
                new double[]{0.5, 0.5, 0.5, 0.5, 0.5, 1.0},
                new RkStepInterpolator(), 1e-15, 1e-6, new double[]{1e-15, 1e-6});

        FirstOrderDifferentialEquations func = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.sin(y[0]);
            }
        };

        double[] initialValues = new double[]{0.0};
        double[] result = integrator.integrate(func, 0.0, initialValues, 1.0, new double[1]);
        assertEquals(1.0, result[0], 1e-6);

        double[] initialValues2 = new double[]{Math.PI / 2};
        double[] result2 = integrator.integrate(func, 0.0, initialValues2, 2.0, new double[1]);
        assertEquals(Math.sin(Math.PI / 2), result2[0], 1e-6);

        double[] initialValues3 = new double[]{Math.PI};
        try {
            integrator.integrate(func, 0.0, initialValues3, 1.0, new double[1]);
            fail();
        } catch (Exception e) {
            // expected
        }

        StepInterpolator interpolator = integrator.getInterpolator();
        StepHandler handler = new StepHandler() {
            int stepCount = 0;

            @Override
            public void handleStep(StepInterpolator interpolator, boolean last) {
                stepCount++;
            }
        };
        integrator.addStepHandler(handler);
        integrator.integrate(func, 0.0, initialValues, 1.0, new double[1]);
        assertEquals(3, stepCount);

        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(1.1);
        double[] y = new double[]{0.0};
        double[] yDot = new double[7];
        integrator.resetEvaluations();
        integrator.integrate(func, 0.0, y, 1.0, yDot);
        assertEquals(y[0], yDot[0], 1e-6);
        assertEquals(y[0], yDot[1], 1e-6);
        assertEquals(y[0], yDot[2], 1e-6);
        assertEquals(y[0], yDot[3], 1e-6);
        assertEquals(y[0], yDot[4], 1e-6);
        assertEquals(y[0], yDot[5], 1e-6);
        assertEquals(y[0], yDot[6], 1e-6);
    }
}