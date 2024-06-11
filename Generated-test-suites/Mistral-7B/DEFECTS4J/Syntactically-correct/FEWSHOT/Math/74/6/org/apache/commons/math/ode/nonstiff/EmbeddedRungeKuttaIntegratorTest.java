package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultCombinedEventManager;
import org.apache.commons.math3.ode.events.RootEvent;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.stepper.StepHandler;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final String DEFAULT_NAME = "Embedded Runge-Kutta Integrator";
    private static final double DEFAULT_MIN_STEP = 1e-9;
    private static final double DEFAULT_MAX_STEP = 1e9;
    private static final double DEFAULT_ABS_TOL = 1e-8;
    private static final double DEFAULT_REL_TOL = 1e-8;
    private static final double DEFAULT_SAFETY = 0.9;
    private static final double DEFAULT_MIN_REDUCTION = 0.2;
    private static final double DEFAULT_MAX_GROWTH = 10.0;

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private CombinedEventsManager eventsManager;
    private StepHandler stepHandler;

    @Before
    public void setUp() {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.0, 0.0, 0.0},
                         {0.3, 0.27, 0.43, 0.12},
                         {0.6, 0.36, 0.67, 0.27},
                         {1.0, 0.64, 0.81, 0.5}};
        double[] b = {0.0, 0.25, 0.45, 0.6};

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        integrator = new EmbeddedRungeKuttaIntegrator(DEFAULT_NAME, false, c, a, b, prototype, DEFAULT_MIN_STEP, DEFAULT_MAX_STEP, DEFAULT_ABS_TOL, DEFAULT_REL_TOL) {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0 - Math.sin(y[0]);
            }
        };
        eventsManager = new DefaultCombinedEventManager();
        stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                assertEquals(DEFAULT_NAME + " step " + stepNumber, context.getIntegratorName());
            }
        };
    }

    @Test
    public void testIntegration() throws Exception {
        final double start = 0.0;
        final double end = 2.0 * Math.PI;
        double[] y0 = {Math.PI / 2.0};

        integrator.addStepHandler(stepHandler);

        double result = integrator.integrate(equations, start, y0, end, new double[y0.length]);

        assertEquals(end, result, 1e-8);
        assertArrayEquals(new double[]{Math.PI, Math.PI}, y0, 1e-8);
    }

    @Test
    public void testIntegrationWithEvents() throws Exception {
        final double start = 0.0;
        final double end = 2.0 * Math.PI;
        double[] y0 = {Math.PI / 2.0};

        // Add a root event
        RootEvent event = new RootEvent(1.0, 0.0, null);
        integrator.addEvent(event);

        integrator.addStepHandler(stepHandler);

        double result = integrator.integrate(equations, start, y0, end, new double[y0.length]);

        assertEquals(end, result, 1e-8);
        assertArrayEquals(new double[]{Math.PI, Math.PI}, y0, 1e-8);
        assertEquals(1, eventsManager.getEventCount());
        assertNotNull(eventsManager.getEvent(0));
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        final double start = 0.0;
        final double end = 2.0 * Math.PI;
        double[] y0 = {Math.PI / 2.0};

        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new DerivativeException("Test DerivativeException");
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, start, y0, end, new double[y0.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        final double start = 0.0;
        final double end = 2.0 * Math.PI;
        double[] y0 = {Math.PI / 2.0};

        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new IntegratorException("Test IntegratorException");
            }
        };

        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, start, y0, end, new double[y0.length]);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 1e-8);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.1);
        assertEquals(0.1, integrator.getMinReduction(), 1e-8);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 1e-8);
    }

}