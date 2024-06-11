package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.ExpandableStatefulODETestUtils;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.noark42.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.noark42.Noak42Integrator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test integration of a simple ODE.
     */
    @Test
    public void testIntegration() {
        final ExpandableStatefulODE equations = new TestODE();

        final double[] initialState = {1.0, 0.0};
        final double step = 0.1;
        final double initialTime = 0.0;
        final double finalTime = 2.0;

        // Test with default tolerances
        EmbeddedRungeKuttaIntegrator integrator = new Noak42Integrator();
        integrator.integrate(equations, initialTime, initialState, finalTime);
        ExpandableStatefulODETestUtils.assertSolution(equations, initialState, step, finalTime);

        // Test with custom tolerances
        double[] vecAbsoluteTolerance = new double[] {1e-12, 1e-12};
        double[] vecRelativeTolerance = new double[] {1e-14, 1e-14};
        integrator = new Noak42Integrator(null, false, null, null, null, null,
                                          null, initialTime, finalTime,
                                          vecAbsoluteTolerance, vecRelativeTolerance);
        integrator.integrate(equations, initialTime, initialState, finalTime);
        ExpandableStatefulODETestUtils.assertSolution(equations, initialState, step, finalTime, 1e-14);

        // Test with invalid tolerances
        assertThrows(MathIllegalArgumentException.class, () -> {
            new Noak42Integrator(null, false, null, null, null, null,
                                null, initialTime, finalTime, new double[] {1.0}, null);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new Noak42Integrator(null, false, null, null, null, null,
                                null, initialTime, finalTime, null, new double[] {1.0});
        });

        // Test with fsal flag
        integrator = new Noak42Integrator(true, null, null, null, null, null,
                                          null, initialTime, finalTime,
                                          vecAbsoluteTolerance, vecRelativeTolerance);
        integrator.integrate(equations, initialTime, initialState, finalTime);
        ExpandableStatefulODETestUtils.assertSolution(equations, initialState, step, finalTime, 1e-14);
        assertEquals(initialState[0], initialState[1]); // fsal method, last state equals first state

    }

    /**
     * Test the error estimation.
     */
    @Test
    public void testErrorEstimation() {
        final ExpandableStatefulODE equations = new TestODE();
        double[] initialState = {1.0, 0.0};
        double step = 0.1;
        double time = 0.0;

        // Test with default tolerances
        EmbeddedRungeKuttaIntegrator integrator = new Noak42Integrator();
        integrator.integrate(equations, time, initialState, time + step);
        double error = integrator.estimateError(equations, initialState, time, step);
        assertTrue(error > 0.0 && error <= Precision.SAFE_MIN + 1.0);

        // Test with custom tolerances
        double[] vecAbsoluteTolerance = new double[] {1e-12, 1e-12};
        double[] vecRelativeTolerance = new double[] {1e-14, 1e-14};
        integrator = new Noak42Integrator(null, false, null, null, null, null,
                                          null, 0.0, 1.0,
                                          vecAbsoluteTolerance, vecRelativeTolerance);
        integrator.integrate(equations, time, initialState, time + step);
        error = integrator.estimateError(equations, initialState, time, step);
        assertTrue(error > 0.0 && error <= 1e-14);

        // Test with invalid tolerances
        assertThrows(MathIllegalArgumentException.class, () -> {
            new Noak42Integrator(null, false, null, null, null, null,
                                null, 0.0, 1.0, new double[] {1.0}, null);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new Noak42Integrator(null, false, null, null, null, null,
                                null, 0.0, 1.0, null, new double[] {1.0});
        });

        // Test with fsal flag
        integrator = new Noak42Integrator(true, null, null, null, null, null,
                                          null, 0.0, 1.0, vecAbsoluteTolerance, vecRelativeTolerance);
        integrator.integrate(equations, time, initialState, time + step);
        error = integrator.estimateError(equations, initialState, time, step);
        assertTrue(error > 0.0 && error <= Precision.SAFE_MIN + 1.0);
        assertEquals(initialState[0], initialState[1]); // fsal method, last state equals first state

    }

    /**
     * Test the interpolator.
     */
    @Test
    public void testInterpolator() {
        final ExpandableStatefulODE equations = new TestODE();

        double[] initialState = {1.0, 0.0};
        double step = 0.1;
        double time = 0.0;

        EmbeddedRungeKuttaIntegrator integrator = new Noak42Integrator();
        integrator.integrate(equations, time, initialState, time + step);

        RungeKuttaStepInterpolator interpolator = integrator.getInterpolator();
        assertNotNull(interpolator);

        // Test interpolation at the last step
        double[] interpolatedState = new double[initialState.length];
        interpolator.interpolate(time + step, interpolatedState);
        assertArrayEquals(initialState, interpolatedState);

        // Test interpolation at a previous step (fsal method)
        if (integrator.isFsal()) {
            double[] lastState = initialState.clone();
            integrator.integrate(equations, time, lastState, time + step);
            interpolator.interpolate(time + step / 2.0, interpolatedState);
            assertArrayEquals(lastState, interpolatedState);
        }

        // Test interpolation at a previous step (non-fsal method)
        if (!integrator.isFsal()) {
            double[] lastState = initialState.clone();
            integrator.integrate(equations, time, lastState, time + step);
            interpolator.interpolate(time + step / 2.0, interpolatedState);
            assertArrayEquals(lastState, interpolatedState);
            assertFalse(Arrays.equals(initialState, interpolatedState));
        }

    }

    /**
     * Custom ODE for testing.
     */
    static class TestODE extends ExpandableStatefulODE {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -0.1 * y[0];
        }

    }

}