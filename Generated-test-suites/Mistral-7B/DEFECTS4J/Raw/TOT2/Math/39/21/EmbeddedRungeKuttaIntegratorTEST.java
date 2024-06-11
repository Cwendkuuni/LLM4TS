package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.Verifier;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorConstruction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] { 0.25, 0.25, 0.5, 0.5 }, new double[][] { { 0.25, 0.0 }, { 0.0, 0.25 }, { 0.0, 0.25 }, { 0.25, 0.0 } }, new double[] { 0.25, 0.25, 0.25, 0.25 }, new RungeKuttaStepInterpolator(), 0.001, 0.01, new double[] { 1e-12, 1e-12 }, new double[] { 1e-12, 1e-12 }) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Precision.abs(BesselFunctions.secondKind(0, y1[0] / h) - y1[0]);
            }
        };
        assertEquals("RK4", integrator.getName());
        assertTrue(integrator.isFsal());
        assertArrayEquals(new double[] { 0.25, 0.0, 0.0, 0.25 }, integrator.getC(), 0);
        assertArrayEquals(new double[][] { { 0.25, 0.0 }, { 0.0, 0.25 }, { 0.0, 0.25 }, { 0.25, 0.0 } }, integrator.getA(), 0);
        assertArrayEquals(new double[] { 0.25, 0.25, 0.25, 0.25 }, integrator.getB(), 0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrate() {
        final double tolerance = 1e-12;
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void derive(double t, double[] y, double[] yPrime) {
                yPrime[0] = PolynomialFunctions.quadratic(y[0]);
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] { 0.25, 0.25, 0.5, 0.5 }, new double[][] { { 0.25, 0.0 }, { 0.0, 0.25 }, { 0.0, 0.25 }, { 0.25, 0.0 } }, new double[] { 0.25, 0.25, 0.25, 0.25 }, new RungeKuttaStepInterpolator(), 0.001, 0.01, new double[] { tolerance, tolerance }, new double[] { tolerance, tolerance });

        integrator.integrate(equations, 0.0);
        assertTrue(Precision.abs(equations.getCompleteState()[0] - 0.0) < tolerance);

        integrator.integrate(equations, 1.0);
        assertTrue(Precision.abs(equations.getCompleteState()[0] - 1.0) < tolerance);

        integrator.integrate(equations, 2.0);
        assertTrue(Precision.abs(equations.getCompleteState()[0] - 2.0) < tolerance);

        integrator.integrate(equations, 3.0);
        assertTrue(Precision.abs(equations.getCompleteState()[0] - 3.0) < tolerance);

        Verifier.verify(equations, 0.0, 4.0, tolerance);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegrateWithException() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void derive(double t, double[] y, double[] yPrime) {
                throw new IllegalArgumentException();
            }
        };

        try {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] { 0.25, 0.25, 0.5, 0.5 }, new double[][] { { 0.25, 0.0 }, { 0.0, 0.25 }, { 0.0, 0.25 }, { 0.25, 0.0 } }, new double[] { 0.25, 0.25, 0.25, 0.25 }, new RungeKuttaStepInterpolator(), 0.001, 0.01, new double[] { 1e-12, 1e-12 }, new double[] { 1e-12, 1e-12 });
            integrator.integrate(equations, 0.0);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }
}