package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultEvaluationEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.IncrementEvent;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] y0 = new double[]{0.0, 0.0};
    private double t0 = 0.0;
    private double t1 = 1.0;
    private double stepSize;
    private double absoluteTolerance = 1e-12;
    private double relativeTolerance = 1e-6;

    @Before
    public void setUp() {
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };

        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[]{0.25, 0.25, 0.5, 0.5},
                new double[][]{ {0.25, 0}, {0.25, 0.25}, {0.5, 0.5}, {0.5, 0.5}},
                new double[]{0.25, 0.25, 0.5, 0.5}, new RungeKuttaStepInterpolator(),
                0.001, 0.01, absoluteTolerance, relativeTolerance) {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + h;
            }
        };
    }

    @Test
    public void testIntegration() throws Exception {
        double[] y = new double[]{0.1, 0.1};
        double t = 0.0;
        double[] result = integrator.integrate(equations, t0, y0, t1, y);
        assertEquals(t1, result[0], absoluteTolerance);
        assertArrayEquals(y1, y, relativeTolerance);

        y = new double[]{1.0, 1.0};
        t = t1;
        result = integrator.integrate(equations, t0, y0, t1, y);
        assertEquals(t1, result[0], absoluteTolerance);
        assertArrayEquals(y1, y, relativeTolerance);

        y = new double[]{-1.0, -1.0};
        t = -t1;
        result = integrator.integrate(equations, t0, y0, t1, y);
        assertEquals(-t1, result[0], absoluteTolerance);
        assertArrayEquals(y1, y, relativeTolerance);
    }

    @Test
    public void testEventDetection() throws Exception {
        integrator.setStepHandlers(new StepHandler[]{
                new StepHandler() {
                    @Override
                    public void handleStep(StepHandlerContext context, int stepNumber) {
                        if (stepNumber == 1) {
                            EventManager events = context.getEventsManager();
                            Event event = new IncrementEvent(1.0);
                            events.addEvent(event);
                        }
                    }
                }
        });

        integrator.setEventsHandlersManager(new EventManager());

        double[] y = new double[]{0.1, 0.1};
        double t = 0.0;
        double[] result = integrator.integrate(equations, t0, y0, t1, y);

        EventManager events = integrator.getEventsManager();
        Event event = events.nextEvent();
        assertNotNull(event);
        assertEquals(DefaultEvaluationEvent.class, event.getClass());
        assertEquals(1.0, event.getValue(), absoluteTolerance);

        event = events.nextEvent();
        assertNull(event);
    }

    @Test
    public void testStepControl() throws Exception {
        integrator = new EmbeddedRungeKuttaIntegrator("RKF45", true, new double[]{0.25, 0.25, 0.25, 0.25, 0.5},
                new double[][]{ {0.25, 0}, {0.25, 0.25}, {0.25, 0.25}, {0.5, 0.5}, {0.5, 0.5}},
                new double[]{0.25, 0.25, 0.25, 0.25, 0.5}, new RungeKuttaStepInterpolator(),
                0.001, 0.01, null, null) {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + h;
            }
        };

        double[] y = new double[]{0.1, 0.1};
        double t = 0.0;
        double[] result = integrator.integrate(equations, t0, y0, t1, y);
        assertEquals(t1, result[0], absoluteTolerance);
        assertArrayEquals(y1, y, relativeTolerance);

        integrator.setMinReduction(0.5);
        integrator.setMaxGrowth(2.0);

        t = 2.0;
        result = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, result[0], absoluteTolerance);
        assertArrayEquals(y, y, relativeTolerance);

        integrator.setSafety(0.8);

        t = 1.5;
        stepSize = integrator.getStepSize();
        assertTrue(stepSize > 0.0);
        assertFalse(integrator.isLastStep());

        t = 1.5 + stepSize;
        result = integrator.integrate(equations, t, y, t1, y);
        assertEquals(t1, result[0], absoluteTolerance);
        assertArrayEquals(y, y, relativeTolerance);

        integrator.reset();
        integrator.setStepSize(1.0);

        stepSize = integrator.getStepSize();
        assertEquals(1.0, stepSize, absoluteTolerance);
        assertFalse(integrator.isLastStep());

        t = 1.0 + stepSize;
        result = integrator.integrate(equations, t, y, t1, y);
        assertEquals(t1, result[0], absoluteTolerance);
        assertArrayEquals(y, y, relativeTolerance);

        integrator.reset();
        integrator.setStepSize(0.1);

        stepSize = integrator.getStepSize();
        assertEquals(0.1, stepSize, absoluteTolerance);
        assertFalse(integrator.isLastStep());

        t = 1.0 + stepSize;
        result = integrator.integrate(equations, t, y, t1, y);
        assertEquals(t1, result[0], absoluteTolerance);
        assertArrayEquals(y, y, relativeTolerance);

        integrator.reset();
        integrator.setStepSize(0.0);

        stepSize = integrator.getStepSize();
        assertEquals(0.0, stepSize, absoluteTolerance);
        assertTrue(integrator.isLastStep());
    }
}