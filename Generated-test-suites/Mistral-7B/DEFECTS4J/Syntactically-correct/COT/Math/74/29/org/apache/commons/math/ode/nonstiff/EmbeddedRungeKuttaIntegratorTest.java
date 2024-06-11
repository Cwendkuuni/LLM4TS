package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.noisy.NoisyFunction;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

/**
 * Test for EmbeddedRungeKuttaIntegrator class
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Tests given constructor
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorConstruction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        {0.0, 0.0, 0.5, 0.5},
                        {0.25, 0.25, 0.25, 0.5},
                        {0.25, 0.25, 0.5, 0.25},
                        {0.5, 0.5, 0.0, 0.0}},
                new double[] {0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                0.0, null, null) {
            @Override
            int getOrder() {
                return 4;
            }
        };

        assertEquals("RK4", integrator.getName());
        assertEquals(4, integrator.getOrder());
        assertEquals(false, integrator.isFSAL());
        assertEquals(4, integrator.getC().length);
        assertEquals(4, integrator.getA().length);
        assertEquals(5, integrator.getB().length);
    }

    /**
     * Tests given constructor with vecAbsoluteTolerance and vecRelativeTolerance
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorConstructionWithTolerances() {
        double[] vecAbsoluteTolerance = {1.0e-6, 1.0e-6, 1.0e-6, 1.0e-6};
        double[] vecRelativeTolerance = {1.0e-5, 1.0e-5, 1.0e-5, 1.0e-5};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        {0.0, 0.0, 0.5, 0.5},
                        {0.25, 0.25, 0.25, 0.5},
                        {0.25, 0.25, 0.5, 0.25},
                        {0.5, 0.5, 0.0, 0.0}},
                new double[] {0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                vecAbsoluteTolerance, vecRelativeTolerance) {
            @Override
            int getOrder() {
                return 4;
            }
        };

        assertEquals("RK4", integrator.getName());
        assertEquals(4, integrator.getOrder());
        assertEquals(false, integrator.isFSAL());
        assertEquals(4, integrator.getC().length);
        assertEquals(4, integrator.getA().length);
        assertEquals(5, integrator.getB().length);
        assertEquals(vecAbsoluteTolerance, integrator.getVecAbsoluteTolerance());
        assertEquals(vecRelativeTolerance, integrator.getVecRelativeTolerance());
    }

    /**
     * Testing constructor with invalid arguments
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorInvalidConstructorArguments() {
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("RK4", true, null, null, null, null, 0.0, 0.0,
                    0.0, null);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] {0.25, 0.25, 0.5}, null,
                    null, null, 0.0, 0.0, 0.0, null);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] {0.25, 0.25, 0.5},
                    new double[][] { {0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.5},
                            {0.25, 0.25, 0.5, 0.25}, {0.5, 0.5, 0.0, 0.0}}, null, 0.0, 0.0,
                    0.0, null);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] {0.25, 0.25, 0.5},
                    new double[][] { {0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.5},
                            {0.25, 0.25, 0.5, 0.25}, {0.5, 0.5, 0.0, 0.0}}, null, null, 0.0, 0.0,
                    0.0, null);
        });
        assertThrows(MathIllegalArgumentException.class, () -> {
            new EmbeddedRungeKuttaIntegrator("RK4", false, new double[] {0.25, 0.25, 0.5},
                    new double[][] { {0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.5},
                            {0.25, 0.25, 0.5, 0.25}, {0.5, 0.5, 0.0, 0.0}}, null, 0.0, 0.0,
                    new double[] {1.0, 1.0, 1.0, 1.0}, null);
        });
    }

    /**
     * Testing getters and setters
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorGettersSetters() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        {0.0, 0.0, 0.5, 0.5},
                        {0.25, 0.25, 0.25, 0.5},
                        {0.25, 0.25, 0.5, 0.25},
                        {0.5, 0.5, 0.0, 0.0}},
                new double[] {0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                0.0, null, null) {
            @Override
            int getOrder() {
                return 4;
            }
        };

        assertEquals(0.9, integrator.getSafety(), 0.001);
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);

        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);

        integrator.setMinReduction(0.3);
        assertEquals(0.3, integrator.getMinReduction(), 0.001);

        integrator.setMaxGrowth(5.0);
        assertEquals(5.0, integrator.getMaxGrowth(), 0.001);
    }

    /**
     * Testing integration with simple differential equation
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorSimpleIntegration() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        {0.0, 0.0, 0.5, 0.5},
                        {0.25, 0.25, 0.25, 0.5},
                        {0.25, 0.25, 0.5, 0.25},
                        {0.5, 0.5, 0.0, 0.0}},
                new double[] {0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                0.0, null, null) {
            @Override
            int getOrder() {
                return 4;
            }

            @Override
            double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double err = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    err += Math.pow(y1[i] - y0[i], 2);
                }
                err = Math.sqrt(err);
                return err;
            }
        };

        double[] y0 = {0.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;

        FirstOrderDifferentialEquations equations = d -> new double[] {d[0] * (1.0 - d[1]),
                d[1]};

        double result = integrator.integrate(equations, t0, y0, t1, null);

        double[] y1 = {0.7360336454128348, 0.2639663545871652};
        assertEquals(result, t1, 1e-8);
        assertEquals(y1[0], integrator.getY()[0], 1e-8);
        assertEquals(y1[1], integrator.getY()[1], 1e-8);
    }

    /**
     * Testing integration with noisy differential equation
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorNoisyIntegration() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[] {0.25, 0.25, 0.5, 0.5},
                new double[][] {
                        {0.0, 0.0, 0.5, 0.5},
                        {0.25, 0.25, 0.25, 0.5},
                        {0.25, 0.25, 0.5, 0.25},
                        {0.5, 0.5, 0.0, 0.0}},
                new double[] {0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                0.0, null, null) {
            @Override
            int getOrder() {
                return 4;
            }

            @Override
            double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double err = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    err += Math.pow(y1[i] - y0[i], 2);
                }
                err = Math.sqrt(err);
                return err;
            }
        };

        double[] y0 = {0.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;

        FirstOrderDifferentialEquations equations = new NoisyFunction(d -> new double[] {d[0] * (1.0 - d[1]),
                d[1] + 0.1 * Math.random()});

        double result = integrator.integrate(equations, t0, y0, t1, null);

        double[] y1 = {0.7410487366175921, 0.2589512633824079};
        assertEquals(result, t1, 1e-8);
        assertEquals(y1[0], integrator.getY()[0], 1e-8);
        assertEquals(y1[1], integrator.getY()[1], 1e-8);

        // test with stepHandlers
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int index) {
                assertEquals(index % 2 == 0, context.isAccepted());
            }
        };

        integrator.addStepHandler(stepHandler);

        result = integrator.integrate(equations, t0, y0, t1, null);
        assertEquals(result, t1, 1e-8);
        assertEquals(y1[0], integrator.getY()[0], 1e-8);
        assertEquals(y1[1], integrator.getY()[1], 1e-8);
        assertEquals(1, stepHandler.getNumberOfStepsTaken());
    }

}