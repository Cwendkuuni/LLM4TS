package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.SamplePointsGenerator;
import org.apache.commons.math3.ode.Verifier;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;

/**
 * This class tests the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test the EmbeddedRungeKuttaMethod1 class.
     */
    @Test
    public void testEmbeddedRungeKuttaMethod1() {
        final String name = "EmbeddedRungeKuttaMethod1";
        final double minStep = 0.0001;
        final double maxStep = 1.0;
        final double scalAbsoluteTolerance = 1.0e-9;
        final double scalRelativeTolerance = 1.0e-8;
        final double safety = 0.9;
        final double minReduction = 0.2;
        final double maxGrowth = 10.0;
        final double[] c = { 0.0, 1.0 / 3.0, 1.0 / 2.0, 1.0 };
        final double[][] a = { { 1.0 / 4.0, 0.0, 1.0 / 3.0, 1.0 / 2.0 },
                               { 1.0 / 3.0, 2.0 / 3.0, 1.0, 1.0 },
                               { 1.0 / 2.0, 1.0, 1.0, 1.0 },
                               { 1.0, 0.0, 0.0, 1.0 } };
        final double[] b = { 1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0, 1.0 };
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c.length + 1);

        final EmbeddedRungeKuttaIntegrator integrator =
            new EmbeddedRungeKuttaIntegrator(name, true, c, a, b, prototype, minStep, maxStep,
                                             scalAbsoluteTolerance, scalRelativeTolerance) {

                @Override
                protected double estimateError(double[][] yDotK, double[] y0, double[] y1,
                                               double h) {
                    double error = 0.0;
                    for (int i = 0; i < y0.length; i++) {
                        error += FastMath.pow(FastMath.abs(y1[i] - y0[i]), 2);
                    }
                    return Math.sqrt(error);
                }
            };

        final double[] initialState = { 0.0, 1.0 };
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {

            @Override
            public int getOrder() {
                return c.length + 1;
            }

            @Override
            public int getDimension() {
                return initialState.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y) {
                for (int i = 0; i < y.length; i++) {
                    y[i] = -y[i] + BesselFunctions.besselI(0, t);
                }
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double getPrimaryMapper() {
                return 0.0;
            }

            @Override
            public double[] getSecondaryMappers() {
                return new double[] {};
            }

        };

        equations.setTime(0.0);
        equations.setCompleteState(initialState);

        final SamplePointsGenerator generator = new SamplePointsGenerator(50);
        final double[][] expectedValues = new double[generator.getNumberOfPoints()][initialState.length];
        final Verifier verifier = new Verifier(equations, generator);

        integrator.integrate(equations, 2.0);
        assertArrayEquals(initialState, equations.getCompleteState());

        for (int i = 0; i < generator.getNumberOfPoints(); i++) {
            double t = generator.getPoint(i);
            integrator.integrate(equations, t);
            assertEquals(expectedValues[i], equations.getCompleteState(), 1e-8);
        }

        verifier.verify(expectedValues);

        integrator.setSafety(0.8);
        integrator.setMinReduction(0.3);
        integrator.setMaxGrowth(5.0);
        equations.setTime(0.0);
        equations.setCompleteState(initialState);
        integrator.integrate(equations, 10.0);
        assertEquals(expectedValues[expectedValues.length - 1], equations.getCompleteState(), 1e-8);

        integrator.setSafety(1.1);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(2.0);
        equations.setTime(0.0);
        equations.setCompleteState(initialState);
        try {
            integrator.integrate(equations, -1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

    }

}