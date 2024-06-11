package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEventHandler;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.RelativetimeEvent;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.stepper.DormandPrince853Stepper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator() throws Exception {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.3, 0.0}, {0.0, 0.6, 0.6}, {0.0, 1.0, 1.0}};
        double[] b = {0.23692688505667133, 0.6787241259331469, 0.865063363712086};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(
                "Dormand-Prince 853", true, c, a, b, prototype, 1.0e-12, 1.0e-12);
        assertEquals("Dormand-Prince 853", integrator.getName());
        assertEquals(c.length, integrator.getOrder());
        assertEquals(true, integrator.isFsal());
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double[], double[])}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator2() throws Exception {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.3, 0.0}, {0.0, 0.6, 0.6}, {0.0, 1.0, 1.0}};
        double[] b = {0.23692688505667133, 0.6787241259331469, 0.865063363712086};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(
                "Dormand-Prince 853", true, c, a, b, prototype, 1.0e-12, new double[]{1.0e-12, 1.0e-12});
        assertEquals("Dormand-Prince 853", integrator.getName());
        assertEquals(c.length, integrator.getOrder());
        assertEquals(true, integrator.isFsal());
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#estimateError(double[][], double[], double[], double)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testEstimateError() throws Exception {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.3, 0.0}, {0.0, 0.6, 0.6}, {0.0, 1.0, 1.0}};
        double[] b = {0.23692688505667133, 0.6787241259331469, 0.865063363712086};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(
                "Dormand-Prince 853", true, c, a, b, prototype, 1.0e-12, 1.0e-12);
        double[] y0 = {1.0, 0.0};
        double[] y1 = {1.3, 1.0};
        double h = 0.1;
        double error = integrator.estimateError(new double[][] {y0, y1}, y0, y1, h);
        assertEquals(1.3, error, 1.0e-12);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testIntegrate() throws Exception {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.3, 0.0}, {0.0, 0.6, 0.6}, {0.0, 1.0, 1.0}};
        double[] b = {0.23692688505667133, 0.6787241259331469, 0.865063363712086};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(
                "Dormand-Prince 853", true, c, a, b, prototype, 1.0e-12, 1.0e-12);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };
        double[] y0 = {1.0, 0.0};
        double t0 = 0.0;
        double t1 = 2.0;
        double[] y1 = new double[y0.length];
        integrator.integrate(equations, t0, y0, t1, y1);
        double[] expected = {2.0, 1.0};
        assertEquals(Arrays.toString(expected), Arrays.toString(y1));
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testIntegrateWithEvents() throws Exception {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.3, 0.0}, {0.0, 0.6, 0.6}, {0.0, 1.0, 1.0}};
        double[] b = {0.23692688505667133, 0.6787241259331469, 0.865063363712086};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(
                "Dormand-Prince 853", true, c, a, b, prototype, 1.0e-12, 1.0e-12);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }

            @Override
            public List<Event> getEvents(double t, double[] y, double h) {
                List<Event> events = new ArrayList<>();
                if (y[0] > 1.5) {
                    events.add(new RelativetimeEvent(t + h, new DefaultValueEventHandler(-1.0)));
                }
                return events;
            }
        };
        double[] y0 = {1.0, 0.0};
        double t0 = 0.0;
        double t1 = 3.0;
        double[] y1 = new double[y0.length];
        integrator.integrate(equations, t0, y0, t1, y1);
        assertEquals(1, integrator.getStepCount());
        double[] expected = {3.0, 1.0};
        assertEquals(Arrays.toString(expected), Arrays.toString(y1));

        assertThrows(MaxCountExceededException.class, () -> {
            integrator.integrate(equations, t0, y0, t1, y1);
        });
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double, double[])}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testIntegrateWithIllegalArgument() throws Exception {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.3, 0.0}, {0.0, 0.6, 0.6}, {0.0, 1.0, 1.0}};
        double[] b = {0.23692688505667133, 0.6787241259331469, 0.865063363712086};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        EmbeddedRungeKuttaIntegrator integrator = new DormandPrince853Integrator(
                "Dormand-Prince 853", true, c, a, b, prototype, 1.0e-12, 1.0e-12);
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3; // this should not match the dimension of the integrator
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new MathIllegalArgumentException("Invalid argument");
            }
        };
        double[] y0 = {1.0, 0.0, 0.0};
        double t0 = 0.0;
        double t1 = 2.0;
        double[] y1 = new double[y0.length];
        assertThrows(IllegalArgumentException.class, () -> {
            integrator.integrate(equations, t0, y0, t1, y1);
        });
    }
}