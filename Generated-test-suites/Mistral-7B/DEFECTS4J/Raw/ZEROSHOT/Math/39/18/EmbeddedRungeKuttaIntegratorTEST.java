package org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.dsolve.Integrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.RungeKuttaStepInterpolator.StepControl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test for EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Embedded Runge-Kutta method 2(3) (Fehlberg 1968)
        // https://en.wikipedia.org/wiki/Embedded_Runge–Kutta_methods#Fehlberg_method_2(3)
        double[] c = { 0.0, 0.231, 0.3923, 0.6381, 0.8335, 1.0 };
        double[][] a = { { 0.0, 0.231, 0.6765, 0.8335, 1.0 },
                         { 0.231, 0.6765, 0.8335, 1.0 },
                         { 0.6765, 0.8335, 1.0 },
                         { 0.8335, 1.0 },
                         { 1.0, 0.0 } };
        double[] b = { 0.231, 0.3923, 0.6381, 0.8335, 1.0 };

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(StepControl.RK23);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Method 2(3)", false, c, a, b,
                                                                                  prototype, 1.0e-10, 1.0e-12) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double err = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    err += FastMath.pow(FastMath.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(err);
            }
        };

        // Test integration of simple function
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = t * y[0];
            }
        };

        integrator.integrate(equations, 0.0);
        assertEquals(0.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 0.0, 0.0 }, equations.getCompleteState(), 1.0e-12);

        integrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 1.0, 1.0 }, equations.getCompleteState(), 1.0e-12);

        integrator.integrate(equations, 2.0);
        assertEquals(2.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 2.0 * 2.0, 2.0 * 3.0 }, equations.getCompleteState(), 1.0e-12);

        integrator.integrate(equations, 3.0);
        assertEquals(3.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 3.0 * 3.0, 3.0 * 4.0 }, equations.getCompleteState(), 1.0e-12);

        integrator.integrate(equations, 4.0);
        assertEquals(4.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 4.0 * 4.0, 4.0 * 5.0 }, equations.getCompleteState(), 1.0e-12);

        integrator.integrate(equations, 10.0);
        assertEquals(10.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 10.0 * 10.0, 10.0 * 11.0 }, equations.getCompleteState(), 1.0e-12);

        // Test integration of Erf(t) function
        ExpandableStatefulODE erfEquations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Erf.erf(t);
            }
        };

        integrator.integrate(erfEquations, 0.0);
        assertEquals(0.0, equations.getTime(), 1.0e-12);
        assertEquals(0.0, equations.getCompleteState()[0], 1.0e-12);

        integrator.integrate(erfEquations, 1.0);
        assertEquals(1.0, equations.getTime(), 1.0e-12);
        assertEquals(0.405831336026934, equations.getCompleteState()[0], 1.0e-4);

        integrator.integrate(erfEquations, 2.0);
        assertEquals(2.0, equations.getTime(), 1.0e-12);
        assertEquals(0.842701464553464, equations.getCompleteState()[0], 1.0e-4);

        integrator.integrate(erfEquations, 3.0);
        assertEquals(3.0, equations.getTime(), 1.0e-12);
        assertEquals(1.265503566029673, equations.getCompleteState()[0], 1.0e-4);

        // Test integration with relative tolerance
        Integrator relTolIntegrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Method 2(3)", false, c, a, b,
                                                                      prototype, 1.0, 1.0e-5) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double err = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    double relErr = FastMath.abs(y1[i] - y0[i]) / (FastMath.abs(y0[i]) + 1.0e-12);
                    err += FastMath.pow(relErr, 2);
                }
                return Math.sqrt(err);
            }
        };

        relTolIntegrator.integrate(equations, 0.0);
        assertEquals(0.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 0.0, 0.0 }, equations.getCompleteState(), 1.0e-12);

        relTolIntegrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 1.0, 1.0 }, equations.getCompleteState(), 1.0e-12);

        relTolIntegrator.integrate(equations, 2.0);
        assertEquals(2.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 2.0 * 2.0, 2.0 * 3.0 }, equations.getCompleteState(), 1.0e-12);

        relTolIntegrator.integrate(equations, 3.0);
        assertEquals(3.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 3.0 * 3.0, 3.0 * 4.0 }, equations.getCompleteState(), 1.0e-12);

        relTolIntegrator.integrate(equations, 10.0);
        assertEquals(10.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 10.0 * 10.0, 10.0 * 11.0 }, equations.getCompleteState(), 1.0e-12);

        relTolIntegrator.integrate(equations, 11.0);
        assertEquals(11.0, equations.getTime(), 1.0e-12);
        assertFalse(integrator.hasReachedFinalState());
        assertFalse(relTolIntegrator.hasReachedFinalState());

        // Test integration with absolute tolerance and relative tolerance
        Integrator absRelTolIntegrator = new EmbeddedRungeKuttaIntegrator("Embedded Runge-Kutta Method 2(3)", false, c, a, b,
                                                                           prototype, 1.0, new double[] { 1.0e-5, 1.0e-5 }) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double err = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    double relErr = FastMath.abs(y1[i] - y0[i]) / (FastMath.abs(y0[i]) + 1.0e-12);
                    double absErr = FastMath.abs(y1[i] - y0[i]);
                    err += FastMath.min(absErr, relErr * 1.0e-5);
                }
                return Math.min(Math.sqrt(err), 1.0e-5);
            }
        };

        absRelTolIntegrator.integrate(equations, 0.0);
        assertEquals(0.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 0.0, 0.0 }, equations.getCompleteState(), 1.0e-12);

        absRelTolIntegrator.integrate(equations, 1.0);
        assertEquals(1.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 1.0, 1.0 }, equations.getCompleteState(), 1.0e-12);

        absRelTolIntegrator.integrate(equations, 2.0);
        assertEquals(2.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 2.0 * 2.0, 2.0 * 3.0 }, equations.getCompleteState(), 1.0e-12);

        absRelTolIntegrator.integrate(equations, 3.0);
        assertEquals(3.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 3.0 * 3.0, 3.0 * 4.0 }, equations.getCompleteState(), 1.0e-12);

        absRelTolIntegrator.integrate(equations, 10.0);
        assertEquals(10.0, equations.getTime(), 1.0e-12);
        assertArrayEquals(new double[] { 10.0 * 10.0, 10.0 * 11.0 }, equations.getCompleteState(), 1.0e-12);

        absRelTolIntegrator.integrate(equations, 11.0);
        assertEquals(11.0, equations.getTime(), 1.0e-12);
        assertTrue(absRelTolIntegrator.hasReachedFinalState());

    }

}