package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.LindenbergSchellerEvent;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventsManager;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private RungeKuttaStepInterpolator prototype;
    private double[][] testEquations;
    private double[] y0;
    private double t0;
    private double tStop;
    private EventsManager eventsManager;

    @Before
    public void setUp() throws Exception {
        prototype = new RungeKuttaStepInterpolator(4, 5, 0.0);
        // Create a dummy integrator for testing purposes
        integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false,
                new double[]{0.23269, 0.67729, 0.05486, 0.02783},
                new double[][]{{0.23269, 0.46768, 0.23269},
                        {0.58666, 0.27338, 0.58666},
                        {0.19846, 0.23269, 0.83667},
                        {0.0, 0.0, 0.12966}},
                new double[]{0.23269, 0.35294, 0.44721, 0.07713, 0.0},
                prototype, 0.001, 0.01, null, null) {

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.max(Math.abs(y1[0] - y0[0]), Math.abs(y1[1] - y0[1]));
            }
        };
        testEquations = new double[][]{{0.0, 0.0}, {1.0, 1.0}};
        y0 = new double[]{0.0, 0.0};
        t0 = 0.0;
        tStop = 10.0;
        eventsManager = new EventsManager();
    }

    @Test
    public void testIntegration() throws PrecisionException, MaxCountExceededException, IntegratorException {
        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 2;
            }

            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }

            @Override
            public double value(double t, double[] y) {
                return 0.0;
            }
        }, t0, y0, tStop, new double[2]);
        assertEquals(tStop, integrator.getStepStart(), 1e-14);
        assertArrayEquals(new double[]{1.0, 0.0}, integrator.getSolution(), 1e-14);
    }

    @Test
    public void testIntegrationWithEvents() throws PrecisionException, MaxCountExceededException, IntegratorException {
        integrator.setStepHandler(step -> {
            assertTrue(step.isLast());
            assertEquals(1.0, step.getY()[0], 1e-14);
            assertEquals(0.0, step.getY()[1], 1e-14);
            assertEquals(10.0, step.getTime(), 1e-14);
        });
        integrator.addEventHandler(new LindenbergSchellerEvent(1.0, 1.0, new AbstractEvent() {
            @Override
            public void handle(double t, double[] y) {
                assertEquals(1.0, y[0], 1e-14);
                assertEquals(0.0, y[1], 1e-14);
            }
        }));
        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 2;
            }

            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }

            @Override
            public double value(double t, double[] y) {
                return 0.0;
            }
        }, t0, y0, tStop, new double[2]);
        assertTrue(eventsManager.isEmpty());
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrationWithRejectedStep() throws PrecisionException, MaxCountExceededException, IntegratorException {
        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.pow(y[0], 2);
            }

            @Override
            public double value(double t, double[] y) {
                return 0.0;
            }
        }, t0, y0, tStop, new double[1]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrationWithMaximalGrowth() throws PrecisionException, MaxCountExceededException, IntegratorException {
        integrator.setMaxGrowth(1.0);
        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.pow(y[0], 2);
            }

            @Override
            public double value(double t, double[] y) {
                return 0.0;
            }
        }, t0, y0, tStop, new double[1]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrationWithMinimalReduction() throws PrecisionException, MaxCountExceededException, IntegratorException {
        integrator.setMinReduction(0.0);
        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.pow(y[0], 2);
            }

            @Override
            public double value(double t, double[] y) {
                return 0.0;
            }
        }, t0, y0, tStop, new double[1]);
    }

    @Test
    public void testIntegrationWithZeroStepSize() throws PrecisionException, MaxCountExceededException, IntegratorException {
        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getNumberOfEquations() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
            }

            @Override
            public double value(double t, double[] y) {
                return 0.0;
            }
        }, t0, y0, tStop, new double[1]);
        assertFalse(integrator.isLastStep());
        assertEquals(0.0, integrator.getStepSize(), 1e-14);
    }
}