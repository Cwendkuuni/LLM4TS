package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.EmptyEventManager;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.TestDifferentialEquation;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[][] a = {{-0.3, -0.9, 0.6}, {0, 0.6, -0.2}, {0.6, 0.3, 0}};
        double[] b = {0.23692688505667133, 0.67332050807568877, 0.9615731962611102};
        double[] c = {0.0, 0.5, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, c, a, b, prototype, 1e-10, 1e-10) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 - 0.999999;
            }
        };

        FirstOrderDifferentialEquations de = new TestDifferentialEquation();
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {2.0, 2.0};

        try {
            integrator.integrate(de, t0, y0, t1, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        integrator.setMinStep(0.1);
        integrator.setMaxStep(1.0);
        integrator.setSafety(0.95);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int step) {
                assertTrue(context.isLastStep());
            }
        };

        EmptyEventManager eventManager = new EmptyEventManager();
        integrator.setEventsHandlersManager(eventManager);
        integrator.addEndTimeChecker(t1, y1, eventManager);

        integrator.integrate(de, t0, y0, 0.1, y0);
        integrator.integrate(de, t0, y0, 0.2, y0);
        integrator.integrate(de, t0, y0, 0.4, y0);
        integrator.integrate(de, t0, y0, 0.8, y0);

        integrator.integrate(de, t0, y0, t1, y1);
        assertFalse(eventManager.isEmpty());
        assertTrue(eventManager.hasNextEvent());
        assertEquals(t1, eventManager.getNextEvent().getTime(), 1e-12);
        assertArrayEquals(y1, eventManager.getNextEvent().getValue(), 1e-12);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorFsal() {
        double[][] a = {{-0.3, -0.9, 0.6}, {0, 0.6, -0.2}, {0.6, 0.3, 0}};
        double[] b = {0.23692688505667133, 0.67332050807568877, 0.9615731962611102};
        double[] c = {0.0, 0.5, 1.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(3);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", true, c, a, b, prototype, 1e-10, 1e-10) {
            @Override
            protected double estimateError(double[][] yDotK,
                                            double[] y0, double[] y1, double h) {
                return 1.0 - 0.999999;
            }
        };

        FirstOrderDifferentialEquations de = new TestDifferentialEquation();
        double[] y0 = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = {2.0, 2.0};

        try {
            integrator.integrate(de, t0, y0, t1, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        integrator.setMinStep(0.1);
        integrator.setMaxStep(1.0);
        integrator.setSafety(0.95);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int step) {
                if (step > 1) {
                    double[] yDotK = context.getIntegratorData().getYDotK();
                    assertArrayEquals(y1, yDotK, 1e-12);
                }
            }
        };

        EmptyEventManager eventManager = new EmptyEventManager();
        integrator.setEventsHandlersManager(eventManager);
        integrator.addEndTimeChecker(t1, y1, eventManager);

        integrator.integrate(de, t0, y0, 0.1, y0);
        integrator.integrate(de, t0, y0, 0.2, y0);
        integrator.integrate(de, t0, y0, 0.4, y0);
        integrator.integrate(de, t0, y0, 0.8, y0);

        integrator.integrate(de, t0, y0, t1, y1);
        assertFalse(eventManager.isEmpty());
        assertTrue(eventManager.hasNextEvent());
        assertEquals(t1, eventManager.getNextEvent().getTime(), 1e-12);
        assertArrayEquals(y1, eventManager.getNextEvent().getValue(), 1e-12);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testEmbeddedRungeKuttaIntegratorNullPrototype() {
        double[][] a = {{-0.3, -0.9, 0.6}, {0, 0.6, -0.2}, {0.6, 0.3, 0}};
        double[] b = {0.23692688505667133, 0.67332050807568877, 0.9615731962611102};
        double[] c = {0.0, 0.5, 1.0};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, c, a, b, null, 1e-10, 1e-10) {
            @Override
            protected double estimateError(double[][] yDotK,
                                            double[] y0, double[] y1, double h) {
                return 1.0 - 0.999999;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmbeddedRungeKuttaIntegratorEmptyPrototype() {
        double[][] a = {};
        double[] b = {};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK3", false, new double[0], a, b, new RungeKuttaStepInterpolator(3), 1e-10, 1e-10) {
            @Override
            protected double estimateError(double[][] yDotK,
                                            double[] y0, double[] y1, double h) {
                return 1.0 - 0.999999;
            }
        };
    }

}