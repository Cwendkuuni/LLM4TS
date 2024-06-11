package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.ForbiddenRegionEvent;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.util.ODETestUtilities;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** Indicator for {@link EmbeddedRungeKuttaIntegrator} methods. */
    private EmbeddedRungeKuttaIntegrator integrator;

    /** Differential equations for testing. */
    private FirstOrderDifferentialEquations equations;

    /** Step handler for testing. */
    private StepHandler stepHandler;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        double[] c = new double[] { 0.0, 0.25, 0.5, 0.875, 1.0 };
        double[][] a = new double[][] {
            { 0.0 }, { 0.25, 0.0 }, { 0.5, 0.25, 0.0 }, { 0.875, 0.5, 0.25, 0.0 },
            { 1.0, 0.875, 0.5, 0.25, 0.0 }
        };
        double[] b = new double[] { 0.0, 0.25, 0.5, 0.875, 1.0 };
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, c, a, b,
                new RungeKuttaStepInterpolator(), 1.0E-12, 1.0E-12) {
            @Override
            public int getOrder() {
                return 5;
            }
        };

        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        stepHandler = new StepHandlerAdapter() {
            private int count = 0;

            @Override
            public void handleStep(StepHandler event, double t, double[] y) {
                count++;
            }
        };
    }

    /**
     * Tests normal integration.
     * @throws Exception if an error occurs
     */
    @Test
    public void testNormalIntegration() throws Exception {
        double t0 = 0.0;
        double y0 = 0.0;
        double t1 = 2 * Math.PI;

        integrator.setStepHandlers(stepHandler);

        double result = integrator.integrate(equations, t0, new double[] { y0 }, t1, null);

        assertEquals(t1, result, 1.0E-8);
        assertEquals(2 * Math.PI, stepHandler.getCount(), 1);
    }

    /**
     * Tests integration with events.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIntegrationWithEvents() throws Exception {
        double t0 = 0.0;
        double y0 = 0.0;
        double t1 = Math.PI;

        integrator.setStepHandlers(stepHandler);

        ForbiddenRegionEvent event = new ForbiddenRegionEvent(Math.PI, Double.POSITIVE_INFINITY) {
            @Override
            public boolean isForbidden(double t, double[] y) {
                return y[0] >= Math.PI;
            }
        };

        integrator.addEvent(event);

        double result = integrator.integrate(equations, t0, new double[] { y0 }, t1, null);

        assertEquals(Math.PI, result, 1.0E-8);
        assertEquals(1, stepHandler.getCount());
    }

    /**
     * Tests integration with negative step size.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrationWithNegativeStepSize() throws Exception {
        double[] y0 = new double[] { 0.0 };
        integrator.integrate(equations, 0.0, y0, -1.0, null);
    }

    /**
     * Tests integration with zero step size.
     * @throws Exception if an error occurs
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrationWithZeroStepSize() throws Exception {
        double[] y0 = new double[] { 0.0 };
        integrator.integrate(equations, 0.0, y0, 0.0, null);
    }

}