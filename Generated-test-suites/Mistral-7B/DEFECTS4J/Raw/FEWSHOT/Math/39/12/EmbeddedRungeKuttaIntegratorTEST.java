package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final int DIMENSION = 1;
    private static final double ABS_TOL = 1e-14;
    private static final double REL_TOL = 1e-15;

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;
    private RungeKuttaStepInterpolator interpolator;

    @Before
    public void setUp() {
        final double[] c = {0.0, 0.3, 0.6, 1.0};
        final double[][] a = {{0.0, 0.0, 0.0, 0.0},
                              {0.3, 0.25, 0.64, 0.0},
                              {0.6, 0, 1.0 - 0.6, 0.36},
                              {1.0, 0.0, 1.0 - 1.0, 1.0 - 0.6}};
        final double[] b = {0.25, 0.64, 0.6, 1.0};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(DIMENSION);

        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c, a, b, prototype, 1e-12, 1e12, ABS_TOL, REL_TOL) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Precision.relativelyEqual(y0, y1, REL_TOL)
                        ? Precision.abs(y1[0] - y0[0])
                        : Precision.abs(y1[0] - y0[0]) / (h * getOrder());
            }

            @Override
            public int getOrder() {
                return 4;
            }
        };

        equations = new ExpandableStatefulODE("Equations") {

            @Override
            public int getDimension() {
                return DIMENSION;
            }

            @Override
            public void computePrimaryDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]) + 0.1 * Math.cos(t);
            }

            @Override
            public void computeSecondaryDerivatives(double t, double[] y, double[][] yDot) {
                yDot[0][0] = Math.cos(y[0]);
            }

        };

        interpolator = new RungeKuttaStepInterpolator(DIMENSION) {

            @Override
            public void reinitialize(RungeKuttaIntegrator integrator, double[] y0, double[][] yDot, boolean forward,
                                     EquationsMapper mapper, double[]... secondaryMapper) {
                super.reinitialize(integrator, y0, yDot, forward, mapper, secondaryMapper);
                this.integrator = integrator;
            }
        };
    }

    @Test
    public void testIntegrate() throws MathIllegalStateException, MathIllegalArgumentException {
        double[] y0 = {Math.PI / 2.0};
        double t = 1.0;

        integrator.integrate(equations, t, y0, null);
        double[] yExpected = {Math.sin(Math.PI / 2.0) + 1.0};
        assertArrayEquals(yExpected, y0, 0.0);

        integrator.integrate(equations, t, y0, null);
        double[] y = y0.clone();
        double[] yDot = new double[DIMENSION];
        double[] yDotK = new double[integrator.getNumStages()][DIMENSION];
        assertArrayEquals(yExpected, y, 0.0);
        integrator.getDYdt(t, y, yDot);
        assertArrayEquals(yDot, yDotK[0], 0.0);

        integrator.integrate(equations, t, y, yDot);
        yExpected = new double[]{Math.sin(Math.PI / 2.0 + yDot[0] * 0.01)};
        assertArrayEquals(yExpected, y, 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateInvalidStepSize() throws MathIllegalStateException, MathIllegalArgumentException {
        double[] y0 = {Math.PI / 2.0};
        integrator.setCurrentStepSize(-1.0);
        integrator.integrate(equations, 1.0, y0, null);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testIntegrateInvalidStateSize() throws MathIllegalStateException, MathIllegalArgumentException {
        double[] y0 = new double[DIMENSION * 2];
        integrator.integrate(equations, 1.0, y0, null);
    }

    @Test
    public void testGetSafety() {
        assertEquals(0.9, integrator.getSafety(), 0.0);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.5);
        assertEquals(0.5, integrator.getSafety(), 0.0);
    }

    @Test
    public void testGetMinReduction() {
        assertEquals(0.2, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 0.0);
    }

    @Test
    public void testGetMaxGrowth() {
        assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.0);
    }
}