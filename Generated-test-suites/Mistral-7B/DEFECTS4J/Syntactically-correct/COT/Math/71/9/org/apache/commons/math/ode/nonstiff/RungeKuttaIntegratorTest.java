package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultIntegratorEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for RungeKuttaIntegrator.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test method for {@link RungeKuttaIntegrator#RungeKuttaIntegrator(String, double[], double[][], double[], RungeKuttaStepInterpolator, double)}
     * @throws Exception
     */
    @Test
    public void testConstructor() throws Exception {
        Assert.assertNotNull(new RungeKuttaIntegrator("EulerIntegrator", new double[]{}, new double[][]{}, new double[]{0.0}, new RungeKuttaStepInterpolator(), 0.01));
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws Exception
     */
    @Test
    public void testIntegrate() throws Exception {
        double[] y0 = new double[]{0.0};
        double t0 = 0.0;
        double[] y1 = new double[]{1.0};
        double t1 = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] + 1.0;
            }
        };

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RungeKuttaIntegrator", new double[]{0.0}, new double[][]{{0.0, 1.0}, {0.5, 0.5}, {1.0, 0.0}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.01);

        double result = integrator.integrate(equations, t0, y0, t1, y1);
        Assert.assertEquals(t1, result, 0.001);
        Assert.assertArrayEquals(y1, y0, 0.001);
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws Exception
     */
    @Test
    public void testIntegrateWithEvent() throws Exception {
        double[] y0 = new double[]{0.0};
        double t0 = 0.0;
        double[] y1 = new double[]{1.0};
        double t1 = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }

            @Override
            public Event[] getEvents(double t, double[] y) {
                if (y[0] >= 0.5) {
                    return new Event[]{new DefaultIntegratorEvent(t, this, StepHandler.STOP)};
                }
                return new Event[0];
            }
        };

        EventManager eventManager = new EventManager();
        eventManager.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandler holder, int type) {
                // do something
            }
        });

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RungeKuttaIntegrator", new double[]{0.0}, new double[][]{{0.0, 1.0}, {0.5, 0.5}, {1.0, 0.0}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.01);
        integrator.setEventsManager(eventManager);

        try {
            integrator.integrate(equations, t0, y0, t1, y1);
            Assert.fail();
        } catch (IntegratorException expected) {
            // expected exception
        }
    }

    /**
     * Test method for {@link RungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     * @throws Exception
     */
    @Test(expected = MathRuntimeException.class)
    public void testIntegrateWithNegativeStepSize() throws Exception {
        double[] y0 = new double[]{0.0};
        double t0 = 0.0;
        double[] y1 = new double[]{1.0};
        double t1 = 1.0;

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0];
            }
        };

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RungeKuttaIntegrator", new double[]{0.0}, new double[][]{{0.0, 1.0}, {0.5, 0.5}, {1.0, 0.0}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(), -0.01);

        integrator.integrate(equations, t0, y0, t1, y1);
    }

}