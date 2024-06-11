package org.apache.commons.math.ode.nonstiff.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKutta45Integrator;
import org.apache.commons.math3.ode.nondifferential.ForcedDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepInterpolation;

/**
 * Test for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    public void testEmbeddedRungeKuttaIntegratorConstruction() {
        final double[] c = { 0.0, 0.3, 0.6, 1.0 };
        final double[][] a = { { 0.0, 0.2, 0.3, 0.6 },
                              { 0.0, 0.4, 0.6, 0.8 },
                              { 0.0, 0.5, 0.8, 1.0 },
                              { 1.0, 0.0, 0.0, 0.0 } };
        final double[] b = { 0.23, 0.34, 0.45, 0.56 };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c, a, b,
                                                                                 new RungeKuttaStepInterpolator(4),
                                                                                 0.001, 0.01,
                                                                                 new double[] { 1.0e-12, 1.0e-12 },
                                                                                 new double[] { 1.0e-12, 1.0e-12 });
        Assert.assertEquals("RK4", integrator.getName());
        Assert.assertFalse(integrator.isFSAL());
        Assert.assertArrayEquals(c, integrator.getC());
        Assert.assertArrayEquals(a, integrator.getA());
        Assert.assertArrayEquals(b, integrator.getB());
        Assert.assertNotNull(integrator.getPrototype());
        Assert.assertEquals(4.0, integrator.getOrder());
        Assert.assertEquals(0.9, integrator.getSafety(), 0.001);
        Assert.assertEquals(0.2, integrator.getMinReduction(), 0.001);
        Assert.assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double[], double[])}.
     */
    public void testEmbeddedRungeKuttaIntegratorConstructionWithVectorTolerances() {
        final double[] c = { 0.0, 0.3, 0.6, 1.0 };
        final double[][] a = { { 0.0, 0.2, 0.3, 0.6 },
                              { 0.0, 0.4, 0.6, 0.8 },
                              { 0.0, 0.5, 0.8, 1.0 },
                              { 1.0, 0.0, 0.0, 0.0 } };
        final double[] b = { 0.23, 0.34, 0.45, 0.56 };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c, a, b,
                                                                                 new RungeKuttaStepInterpolator(4),
                                                                                 0.001, 0.01,
                                                                                 new double[] { 1.0e-13, 1.0e-13 },
                                                                                 new double[] { 1.0e-13, 1.0e-13 });
        Assert.assertEquals("RK4", integrator.getName());
        Assert.assertFalse(integrator.isFSAL());
        Assert.assertArrayEquals(c, integrator.getC());
        Assert.assertArrayEquals(a, integrator.getA());
        Assert.assertArrayEquals(b, integrator.getB());
        Assert.assertNotNull(integrator.getPrototype());
        Assert.assertEquals(4.0, integrator.getOrder());
        Assert.assertEquals(0.9, integrator.getSafety(), 0.001);
        Assert.assertEquals(0.2, integrator.getMinReduction(), 0.001);
        Assert.assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegrateLinearEquations() throws Exception {
        final double[][] a = { { 1.0, 0.0 }, { 0.0, 1.0 } };
        final double[] b = new double[] { 1.0, 1.0 };
        FirstOrderDifferentialEquations equations = new ForcedDifferentialEquations(a, b);
        EmbeddedRungeKuttaIntegrator integrator = new RungeKutta45Integrator();
        double[] y0 = { 1.0, 1.0 };
        double[] y = new double[2];
        double t0 = 0.0, t = 1.0;
        integrator.integrate(equations, t0, y0, t, y);
        Assert.assertArrayEquals(new double[] { 2.0, 2.0 }, y, 0.001);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegrateLinearEquationsWithEvents() throws Exception {
        final double[][] a = { { 1.0, 0.0 }, { 0.0, 1.0 } };
        final double[] b = new double[] { 1.0, 1.0 };
        final double[] y0 = { 1.0, 1.0 };
        FirstOrderDifferentialEquations equations = new ForcedDifferentialEquations(a, b);
        RungeKutta45Integrator integrator = new RungeKutta45Integrator();
        double t0 = 0.0;
        double[] y = new double[2];
        double[] yDot = new double[2];
        StepInterpolation interpolation = integrator.createStepInterpolation();

        integrator.addStepHandler(new StepHandler() {

            @Override
            public void handleStep(final StepInterpolation interpolator, final int step) {
                final double[] y = interpolator.getY();
                final double[] yDot = interpolator.getYDot();
                System.out.printf("step %d: y = [%f, %f], y' = [%f, %f]\n", step, y[0], y[1], yDot[0], yDot[1]);
            }

        });

        integrator.integrate(equations, t0, y0, 2.0, y);
        assertEquals(2, integrator.getStepCount());
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegrateNonLinearEquations() throws Exception {
        final double[][] a = { { 0.0, -0.2, 1.0 }, { 0.2, 1.0, 0.0 }, { -1.0, 0.0, 1.0 } };
        final double[] b = new double[] { -0.5, 0.1, 0.0 };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = y[2];
                yPrime[2] = -y[0] * y[1] + y[2];
            }

        };
        EmbeddedRungeKuttaIntegrator integrator = new RungeKutta45Integrator();
        double[] y0 = { 1.0, 1.0, 1.0 };
        double[] y = new double[3];
        double t0 = 0.0, t = 1.0;
        integrator.integrate(equations, t0, y0, t, y);
        Assert.assertArrayEquals(new double[] { 1.834396479014362, 1.731315078751569, 1.0 }, y, 0.001);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     */
    public void testIntegrateNonLinearEquationsWithEvents() throws Exception {
        final double[][] a = { { 0.0, -0.2, 1.0 }, { 0.2, 1.0, 0.0 }, { -1.0, 0.0, 1.0 } };
        final double[] b = new double[] { -0.5, 0.1, 0.0 };
        final double[] y0 = { 1.0, 1.0, 1.0 };
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = y[2];
                yPrime[2] = -y[0] * y[1] + y[2];
            }

        };
        RungeKutta45Integrator integrator = new RungeKutta45Integrator();
        double t0 = 0.0;
        double[] y = new double[3];
        double[] yDot = new double[3];
        double[] events = new double[] { 1.0, 2.0, 3.0 };
        StepInterpolation interpolation = integrator.createStepInterpolation();

        integrator.addStepHandler(new StepHandler() {

            @Override
            public void handleStep(final StepInterpolation interpolator, final int step) {
                final double[] y = interpolator.getY();
                final double[] yDot = interpolator.getYDot();
                System.out.printf("step %d: y = [%f, %f, %f], y' = [%f, %f, %f]\n", step, y[0], y[1], y[2], yDot[0], yDot[1], yDot[2]);
            }

        });

        integrator.addEventHandler(new EventHandler() {

            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public Event[] getEvents(double t) {
                Event[] events = new Event[events.length];
                for (int i = 0; i < events.length; ++i) {
                    events[i] = new When(events[i], new EventHandler() {

                        @Override
                        public void handleEvent(double t, double[] y) {
                            System.out.printf("event at time %f, y = [%f, %f, %f]\n", t, y[0], y[1], y[2]);
                        }

                    });
                }
                return events;
            }

        });

        integrator.integrate(equations, t0, y0, 3.0, y);
        assertEquals(3, integrator.getStepCount());
    }

}