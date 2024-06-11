package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaTest;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * Test suite for the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends EmbeddedRungeKuttaTest {

    /**
     * Construct a test instance.
     */
    public EmbeddedRungeKuttaIntegratorTest() {
        super();
    }

    /**
     * Test the {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        final double[] c = { 0.0, 0.23692688505667133, 0.67949212820525641, 0.8650638869771167, 1.0 };
        final double[][] a = { { 0.0 }, { 0.23692688505667133, 0.47385316961334267 },
                               { 0.67949212820525641, 0.8650638869771167, 1.0 },
                               { 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0, 1.0 } };
        final double[] b = { 0.35475781384432521, 0.83226356839445381, 0.64293736124702261, 0.29661269031475542, 0.0 };
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5);
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKC4", true, c, a, b, prototype, 0.01, 0.04, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.abs(y1[0] - y0[0]) / (2.0 * h);
            }

            @Override
            protected double getOrder() {
                return 4;
            }

            @Override
            protected double getSafety() {
                return 0.9;
            }

            @Override
            protected void setMinReduction(double minReduction) {
                this.minReduction = minReduction;
            }

            @Override
            protected void setMaxGrowth(double maxGrowth) {
                this.maxGrowth = maxGrowth;
            }
        };

        // test the constructor
        assertEquals("RKC4", integrator.getName());
        assertTrue(integrator.isFsal());
        assertArrayEquals(c, integrator.getC());
        assertArrayEquals(a[0], integrator.getA()[0]);
        assertArrayEquals(a[1], integrator.getA()[1]);
        assertArrayEquals(a[2], integrator.getA()[2]);
        assertArrayEquals(a[3], integrator.getA()[3]);
        assertArrayEquals(a[4], integrator.getA()[4]);
        assertArrayEquals(b, integrator.getB());
        assertEquals(prototype, integrator.getPrototype());
        assertEquals(0.01, integrator.getMinStep(), 1e-14);
        assertEquals(0.04, integrator.getMaxStep(), 1e-14);

        // test the integrate method
        final double[] y0 = new double[] { 1.0 };
        final double t0 = 0.0;
        final double[] y1 = new double[] { 1.3 };
        final double t1 = 0.1;
        final double[] y2 = new double[] { 2.0 };
        final double t2 = 0.2;
        final double[] y3 = new double[] { 2.718281828 };
        final double t3 = 1.0;

        final double[] yDot0 = new double[y0.length];
        final double[] yDot1 = new double[y0.length];
        final double[] yDot2 = new double[y0.length];
        final double[] yDot3 = new double[y0.length];
        final double[][] yDotK = new double[5][y0.length];

        final double[][] yTmp = new double[5][y0.length];
        final StepInterpolator interpolator = integrator.getInterpolator();

        // test the forward integration
        integrator.integrate(new MyDifferentialEquations(), t0, y0, t1, y1);
        interpolator.shift();
        integrator.integrate(new MyDifferentialEquations(), t1, y1, t2, y2);
        interpolator.shift();
        integrator.integrate(new MyDifferentialEquations(), t2, y2, t3, y3);
        interpolator.shift();

        // test the reverse integration
        interpolator.reinitialize(integrator, y3, yDotK[4], false);
        integrator.integrate(new MyDifferentialEquations(), t3, y3, t2, y2);
        interpolator.shift();
        integrator.integrate(new MyDifferentialEquations(), t2, y2, t1, y1);
        interpolator.shift();
        integrator.integrate(new MyDifferentialEquations(), t1, y1, t0, y0);
        interpolator.shift();

        // test the step size
        final double h1 = integrator.integrate(new MyDifferentialEquations(), t1, y1, t2, y2) - t1;
        final double h2 = integrator.integrate(new MyDifferentialEquations(), t2, y2, t3, y3) - t2;
        final double h3 = integrator.integrate(new MyDifferentialEquations(), t3, y3, t2, y2);
        assertEquals(h1, h2, 1e-14);
        assertEquals(h1, h3, 1e-14);

        // test the step handler
        final int count = 0;
        final StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean lastStep) {
                if (interpolator.isLastStep()) {
                    assertEquals(t3, interpolator.getTime(), 1e-14);
                    ++count;
                }
            }
        };

        integrator.addStepHandler(stepHandler);
        integrator.integrate(new MyDifferentialEquations(), t0, y0, t3, null);
        assertEquals(1, count);

        // test the events handling
        final double[] y4 = new double[] { 2.718281828 * Math.E };
        final double t4 = 2.0;
        final double[] yDot4 = new double[y0.length];
        final Event event = new When(t4, new MyDifferentialEquations()) {
            @Override
            public void computeDerivative(double t, double[] y, double[] yDot) {
                yDot[0] = -y[0];
            }
        };

        integrator.addEventHandler(event);
        integrator.integrate(new MyDifferentialEquations(), t0, y0, t4, y4);
        assertEquals(t4, integrator.getStepStart(), 1e-14);

    }

}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public void computeDerivative(double t, double[] y, double[] yDot) {
        yDot[0] = y[0] * 0.1;
    }

}