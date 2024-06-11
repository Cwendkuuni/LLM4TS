package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.Verifier;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.events.IntervalEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        final ExpandableStatefulODE equations = new ExpandableStatefulODE("test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] yDot) {
                yDot[0] = Math.exp(y[0]) - 1;
            }
        };

        final double[][] a = {{-0.2369268850566713E-2, -0.2598291006941593E-2, 0.3670270174532927E-1, 0.1632992166068778E-1, 0.5114843379620158E-1},
                {-0.2499645696303307E-2, -0.6887245247764384E-2, 0.2742955921053138E-1, 0.4336491476635544E-1, 0.3338763617668953E-1},
                {-0.2369268850566713E-2, -0.2598291006941593E-2, 0.3670270174532927E-1, 0.1632992166068778E-1, 0.5114843379620158E-1},
                {-0.2369268850566713E-2, -0.2598291006941593E-2, 0.3670270174532927E-1, 0.1632992166068778E-1, 0.5114843379620158E-1},
                {-0.2369268850566713E-2, 0.0, 0.0, 0.0, 0.0}};
        final double[] b = {0.2369268850566713E-2, 0.3670270174532927E-1, 0.5114843379620158E-1, 0.8378726759065456E-1, 1.0};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, false);
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{0.3, 0.4, 0.2, 0.1, 0.1}, a, b, prototype, 1.0e-10, 1.0e-10) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; ++i) {
                    error += FastMath.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        final double t0 = 0;
        final double t1 = 1;
        final double[] y0 = new double[]{0};
        final double[] y1 = new double[]{1};
        final double[] y2 = new double[]{1};
        final double step = 0.1;
        integrator.integrate(equations, t1);
        assertEquals(t1, equations.getTime());
        assertArrayEquals(y1, equations.getCompleteState(), 1e-10);
        integrator.reset();
        integrator.integrate(equations, t0);
        assertEquals(t0, equations.getTime());
        assertArrayEquals(y0, equations.getCompleteState(), 1e-10);
        integrator.integrate(equations, t1);
        assertEquals(t1, equations.getTime());
        assertArrayEquals(y1, equations.getCompleteState(), 1e-10);
        integrator.integrate(equations, t1 + step);
        assertEquals(t1 + step, equations.getTime());
        assertArrayEquals(new double[]{1 + (1 - Math.exp(1)) * step}, equations.getCompleteState(), 1e-10);
        integrator.integrate(equations, t1 + step * 2);
        assertEquals(t1 + step * 2, equations.getTime());
        assertArrayEquals(new double[]{1 + (1 - Math.exp(1)) * step * 2}, equations.getCompleteState(), 1e-10);
        integrator.integrate(equations, t1 + step * 3);
        assertEquals(t1 + step * 3, equations.getTime());
        assertArrayEquals(new double[]{1 + (1 - Math.exp(1)) * step * 3}, equations.getCompleteState(), 1e-10);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator2() {
        final ExpandableStatefulODE equations = new ExpandableStatefulODE("test") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] yDot) {
                yDot[0] = E.E0 * Math.exp(-time) * y[0];
            }
        };

        final double[][] a = {{-0.2369268850566713E-2, -0.2598291006941593E-2, 0.3670270174532927E-1, 0.1632992166068778E-1, 0.5114843379620158E-1},
                {-0.2499645696303307E-2, -0.6887245247764384E-2, 0.2742955921053138E-1, 0.4336491476635544E-1, 0.3338763617668953E-1},
                {-0.2369268850566713E-2, -0.2598291006941593E-2, 0.3670270174532927E-1, 0.1632992166068778E-1, 0.5114843379620158E-1},
                {-0.2369268850566713E-2, -0.2598291006941593E-2, 0.3670270174532927E-1, 0.1632992166068778E-1, 0.5114843379620158E-1},
                {-0.2369268850566713E-2, 0.0, 0.0, 0.0, 0.0}};
        final double[] b = {0.2369268850566713E-2, 0.3670270174532927E-1, 0.5114843379620158E-1, 0.8378726759065456E-1, 1.0};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(1, false);
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{0.3, 0.4, 0.2, 0.1, 0.1}, a, b, prototype, 1.0e-10, 1.0e-10, new double[]{1.0}, new double[]{1.0e-3}) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; ++i) {
                    error += FastMath.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        final double t0 = 0;
        final double t1 = 1;
        final double[] y0 = new double[]{1};
        final double[] y1 = new double[]{1};
        final Event event = new IntervalEvent(1.5, EventRate.RIGHT, null);
        equations.addEvent(event);
        final Verifier verifier = new Verifier(equations);
        verifier.addStepHandler(e -> System.out.println(e.getTime()));
        integrator.integrate(equations, t1, 1000);
        assertTrue(verifier.verify());
    }

}