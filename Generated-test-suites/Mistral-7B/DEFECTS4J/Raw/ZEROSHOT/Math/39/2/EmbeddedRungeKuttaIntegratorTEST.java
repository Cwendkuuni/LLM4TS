package org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.samples.RickerODE;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RuntimeConfiguration;

import java.util.Random;

public class EmbeddedRungeKuttaIntegratorTest {

    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Test with default settings
        EmbeddedRungeKuttaIntegrator rk45 = new EmbeddedRungeKuttaIntegrator(
                "Embedded RK45 integrator", false,
                new double[]{0.5, 0.5, 0.5, 0.5, 1.0},
                new double[][]{{0.25, 0.25, 0.25, 0.25, 1.0},
                        {0.25, 0.45, 0.45, 0.45, 1.0},
                        {0.25, 0.3,  0.6,  0.6,  1.0},
                        {0.25, 0.0,  0.9,  0.9,  1.0}},
                new double[]{0.25, 0.25, 0.25, 0.25, 1.0},
                new RungeKuttaStepInterpolator(4, RuntimeConfiguration.doubleMantissaDigits()),
                0.001, 0.01,
                1.0e-6, 1.0e-6);

        ExpandableStatefulODE equations = new RickerODE();
        double tEnd = 10.0;
        double[] y0 = {2.0, 4.0};

        // Test single step
        double[] y = rk45.integrate(equations, tEnd, y0)[0];
        double yRef = new double[]{BesselFunctions.i0(2.5 * tEnd), BesselFunctions.i0(5.0 * tEnd)};
        for (int i = 0; i < y.length; i++) {
            assertEquals(String.format("y[%d] expected: %f, actual: %f", i, yRef[i], y[i]), yRef[i], y[i], 1e-5);
        }

        // Test multiple steps
        Random random = new Random(1234);
        for (int i = 0; i < 10; i++) {
            double tStart = random.nextGaussian() * 2.0 + 1.0;
            double[] yStart = new double[]{random.nextGaussian() * 2.0 + 1.0, random.nextGaussian() * 2.0 + 1.0};
            rk45.reset();
            rk45.integrate(equations, tEnd, yStart);
            y = rk45.getLastState();
            for (int j = 0; j < y.length; j++) {
                assertEquals(String.format("y[%d] expected: %f, actual: %f", j, yRef[j], y[j]), yRef[j], y[j], 1e-5);
            }
        }

        // Test step size control
        rk45 = new EmbeddedRungeKuttaIntegrator(
                "Embedded RK45 integrator", false,
                new double[]{0.5, 0.5, 0.5, 0.5, 1.0},
                new double[][]{{0.25, 0.25, 0.25, 0.25, 1.0},
                        {0.25, 0.45, 0.45, 0.45, 1.0},
                        {0.25, 0.3,  0.6,  0.6,  1.0},
                        {0.25, 0.0,  0.9,  0.9,  1.0}},
                new double[]{0.25, 0.25, 0.25, 0.25, 1.0},
                new RungeKuttaStepInterpolator(4, RuntimeConfiguration.doubleMantissaDigits()),
                0.001, 0.01,
                1.0e-9, 1.0e-9);

        rk45.setSafety(0.5);
        rk45.setMinReduction(0.1);
        rk45.setMaxGrowth(1.1);
        rk45.integrate(equations, tEnd, y0);
        y = rk45.getLastState();
        for (int i = 0; i < y.length; i++) {
            double error = Math.abs(y[i] - yRef[i]);
            assertTrue(String.format("y[%d] error: %f, expected to be smaller than: %f", i, error, 1e-8), error < 1e-8);
        }
    }

    @org.junit.Test
    public void testEmbeddedRungeKuttaIntegratorFsal() {
        // Test with fsal method
        EmbeddedRungeKuttaIntegrator rk45 = new EmbeddedRungeKuttaIntegrator(
                "Embedded RK45 integrator (fsal)", true,
                new double[]{0.5, 0.5, 1.0},
                new double[][]{{0.25, 0.5, 1.0},
                        {0.25, 0.5, 1.0}},
                new double[]{0.25, 0.5, 1.0},
                new RungeKuttaStepInterpolator(2, RuntimeConfiguration.doubleMantissaDigits()),
                0.001, 0.01,
                1.0e-6, 1.0e-6);

        ExpandableStatefulODE equations = new RickerODE();
        double tEnd = 10.0;
        double[] y0 = {2.0, 4.0};

        // Test single step
        rk45.reset();
        rk45.integrate(equations, tEnd, y0);
        double[] y = rk45.getLastState();
        double yRef = BesselFunctions.i0(2.5 * tEnd);
        assertEquals(String.format("y[0] expected: %f, actual: %f", yRef, y[0]), yRef, y[0], 1e-5);
        yRef = BesselFunctions.i0(5.0 * tEnd);
        assertEquals(String.format("y[1] expected: %f, actual: %f", yRef, y[1]), yRef, y[1], 1e-5);

        // Test multiple steps
        Random random = new Random(1234);
        for (int i = 0; i < 10; i++) {
            double tStart = random.nextGaussian() * 2.0 + 1.0;
            double[] yStart = new double[]{random.nextGaussian() * 2.0 + 1.0, random.nextGaussian() * 2.0 + 1.0};
            rk45.reset();
            rk45.integrate(equations, tEnd, yStart);
            y = rk45.getLastState();
            double yRef = BesselFunctions.i0(2.5 * tEnd);
            assertEquals(String.format("y[0] expected: %f, actual: %f", yRef, y[0]), yRef, y[0], 1e-5);
            yRef = BesselFunctions.i0(5.0 * tEnd);
            assertEquals(String.format("y[1] expected: %f, actual: %f", yRef, y[1]), yRef, y[1], 1e-5);
        }

        // Test step size control
        rk45 = new EmbeddedRungeKuttaIntegrator(
                "Embedded RK45 integrator (fsal)", true,
                new double[]{0.5, 1.0},
                new double[][]{{0.5, 1.0}},
                new double[]{0.5, 1.0},
                new RungeKuttaStepInterpolator(2, RuntimeConfiguration.doubleMantissaDigits()),
                0.001, 0.01,
                1.0e-9, 1.0e-9);

        rk45.setSafety(0.5);
        rk45.setMinReduction(0.1);
        rk45.setMaxGrowth(1.1);
        rk45.integrate(equations, tEnd, y0);
        y = rk45.getLastState();
        double error = Precision.abs(y[0] - yRef);
        assertTrue(String.format("y[0] error: %f, expected to be smaller than: %f", error, 1e-8), error < 1e-8);
        error = Precision.abs(y[1] - yRef);
        assertTrue(String.format("y[1] error: %f, expected to be smaller than: %f", error, 1e-8), error < 1e-8);
    }

}