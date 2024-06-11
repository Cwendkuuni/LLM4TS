package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.math3.ode.test.DenseTestFunctions.*;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbedgedRungeKuttaIntegratorTest {

    private static final double TOL = 1e-12;

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", true,
                new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.25, 0.25}, {0.25, 0.25}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(),
                1e-9, 1e-9, null, null) {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double absError = 0;
                for (int i = 0; i < y0.length; i++) {
                    absError += Math.abs(y1[i] - y0[i]);
                }
                return absError;
            }
        };
        equations = new RK4Example();
    }

    @Test
    public void testIntegration() throws Exception {
        double t0 = 0.0, t1 = 1.0;
        double[] y0 = new double[]{1.0, 1.0};
        double[] y1 = new double[]{2.0, 2.0};

        double result = integrator.integrate(equations, t0, y0, t1, y1);
        Assert.assertEquals(t1, result, TOL);

        // check intermediate points
        Event[] events = integrator.getEvents();
        Assert.assertEquals(events.length, 2);
        double[] yInt = new double[]{1.5, 1.5};
        Assert.assertArrayEquals(yInt, equations.computeState(1.0), TOL);
        double timeInt = integrator.getInterpolatedValue(1.0);
        Assert.assertEquals(timeInt, 1.0, TOL);
    }

    @Test(expected = IntegratorException.class)
    public void testExceptionDuringIntegration() throws Exception {
        double t0 = 0.0, t1 = 1.0;
        double[] y0 = new double[]{1.0, 1.0};

        final StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(StepHandler stepHandler, int type) {
                throw new IntegratorException("Test exception");
            }
        };
        integrator.addStepHandler(handler);

        integrator.integrate(equations, t0, y0, t1, null);
    }

    @Test(expected = DerivativeException.class)
    public void testExceptionDuringDerivativeComputation() throws Exception {
        final When event = new When(1.0) {
            @Override
            public boolean isSatisfied(double t, double[] y) {
                throw new DerivativeException("Test exception");
            }
        };
        integrator.addEventManager(event);

        double t0 = 0.0, t1 = 1.0;
        double[] y0 = new double[]{1.0, 1.0};

        integrator.integrate(equations, t0, y0, t1, null);
    }

}