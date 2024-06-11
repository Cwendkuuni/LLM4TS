package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethod1;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        equations = new SimpleDifferentialEquations();
        integrator = new EmbeddedRungeKuttaMethod1(
                "Runge-Kutta Method 1",
                true,
                new double[]{-0.3, 0.3333333333333333, 0.8333333333333333, 1.0},
                new double[]{-0.3, 0.3, 0.8, 1.0},
                new double[]{0.3, 0.3, 0.3, 0.3},
                new RungeKuttaStepInterpolator(),
                0.001,
                1.0,
                1.0E-8,
                1.0E-8);
    }

    @Test
    public void testIntegratorConstructor() {
        Assert.assertNotNull(integrator);
    }

    @Test
    public void testGetOrder() {
        assertEquals(4, integrator.getOrder());
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

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMinStep() {
        integrator = new EmbeddedRungeKuttaMethod1(
                "Runge-Kutta Method 1",
                true,
                new double[]{-0.3, 0.3333333333333333, 0.8333333333333333, 1.0},
                new double[]{-0.3, 0.3, 0.8, 1.0},
                new double[]{0.3, 0.3, 0.3, 0.3},
                new RungeKuttaStepInterpolator(),
                -0.001,
                1.0,
                1.0E-8,
                1.0E-8);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testZeroMinStep() {
        integrator = new EmbeddedRungeKuttaMethod1(
                "Runge-Kutta Method 1",
                true,
                new double[]{-0.3, 0.3333333333333333, 0.8333333333333333, 1.0},
                new double[]{-0.3, 0.3, 0.8, 1.0},
                new double[]{0.3, 0.3, 0.3, 0.3},
                new RungeKuttaStepInterpolator(),
                0.0,
                1.0,
                1.0E-8,
                1.0E-8);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidToleranceValues() {
        integrator = new EmbeddedRungeKuttaMethod1(
                "Runge-Kutta Method 1",
                true,
                new double[]{-0.3, 0.3333333333333333, 0.8333333333333333, 1.0},
                new double[]{-0.3, 0.3, 0.8, 1.0},
                new double[]{0.3, 0.3, 0.3, 0.3},
                new RungeKuttaStepInterpolator(),
                0.001,
                null,
                1.0E-8,
                1.0E-8);
    }

    @Test
    public void testIntegrationWithSimpleDifferentialEquations() {
        double initialTime = 0.0;
        double[] initialValues = {1.0, 0.0};
        double finalTime = 2.0;

        double[] result = integrator.integrate(equations, initialTime, initialValues, finalTime, new double[initialValues.length]);

        // You should provide the expected result here
        double[] expected = {2.718281828459045, 1.718281828459045};

        assertArrayEquals(expected, result, 0.001);
    }

}