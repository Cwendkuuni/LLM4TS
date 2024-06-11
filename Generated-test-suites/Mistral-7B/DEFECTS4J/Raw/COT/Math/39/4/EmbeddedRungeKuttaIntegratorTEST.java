package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.VectorUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;
    private double[][] coefficients;
    private RungeKuttaStepInterpolator prototype;

    @Before
    public void setup() {
        coefficients = new double[][]{{0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 8.0},
                {0.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 2.0},
                {1.0 / 4.0, 0.0, 1.0 / 3.0, 1.0 / 2.0},
                {1.0 / 3.0, 1.0 / 4.0, 0.0, 1.0 / 3.0},
                {1.0 / 8.0, 1.0 / 2.0, 1.0 / 4.0, 0.0}};
        prototype = new RungeKuttaStepInterpolator(coefficients[coefficients.length - 1]);
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, coefficients[0], coefficients, coefficients[coefficients.length - 1], prototype, 0.001, 0.01, 1e-12, 1e-12) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Precision.safeMax(Precision.safeMax(FastMath.abs(yDotK[0][i] - yDotK[1][i]), FastMath.abs(y1[i] - y0[i])));
                }
                return error;
            }
        };
        equations = new ExpandableStatefulODE("testODE") {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computePrimaryDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * y[0];
            }

            @Override
            public void computeSecondaryDerivatives(double t, double[] y, double[][] yDoublePrime) {
                yDoublePrime[0][0] = 2 * y[0];
            }
        };
    }

    @Test
    public void testGetOrder() {
        assertEquals(4, integrator.getOrder());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidMinStep() {
        new EmbeddedRungeKuttaIntegrator("RK4", false, coefficients[0], coefficients, coefficients[coefficients.length - 1], prototype, -0.001, 0.01, 1e-12, 1e-12);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidMaxStep() {
        new EmbeddedRungeKuttaIntegrator("RK4", false, coefficients[0], coefficients, coefficients[coefficients.length - 1], prototype, 0.001, -0.01, 1e-12, 1e-12);
    }

    @Test
    public void testInitializeStep() {
        double[] y0 = VectorUtils.createDoubleArray(0.1);
        double h = integrator.initializeStep(true, integrator.getOrder(), null, 0.1, y0, null, null, 0.1);
        assertTrue(h > 0);
    }

    @Test
    public void testIntegrate() {
        double t0 = 0.1;
        double t1 = 0.4;
        double t2 = 1.0;
        integrator.integrate(equations, t1);
        double[] y1 = equations.getCompleteState();
        integrator.integrate(equations, t2);
        double[] y2 = equations.getCompleteState();
        assertArrayEquals(y1, new double[]{Math.exp(1), Math.exp(2)});
        assertArrayEquals(y2, new double[]{Math.exp(1) * Math.exp(1), Math.exp(1) * Math.exp(2)});
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateWithInvalidEquations() {
        ExpandableStatefulODE invalidEquations = new ExpandableStatefulODE("invalidODE") {
            @Override
            public int getDimension() {
                throw new UnsupportedOperationException();
            }
        };
        integrator.integrate(invalidEquations, 0.1);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithNegativeTimeStep() {
        integrator.integrate(equations, -0.1);
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), 0.01);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.5);
        assertEquals(0.5, integrator.getSafety(), 0.01);
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), 0.01);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.5);
        assertEquals(0.5, integrator.getMinReduction(), 0.01);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), 0.01);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.01);
    }

}