import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    

    @Test
    public void testEmbeddedRungeKuttaIntegratorConstruction() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbedgedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.5},
                new double[][]{{0, 0, 0.25, 0.25}, {0.25, 0.25, 0.5, 0.5}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                new double[]{1e-10, 1e-10});
        assertEquals("RK4", integrator.getName());
        assertFalse(integrator.isFsal());
        assertArrayEquals(new double[]{0.25, 0.25, 0.5, 0.5}, integrator.getC(), 0);
        assertArrayEquals(new double[][]{{0, 0, 0.25, 0.25}, {0.25, 0.25, 0.5, 0.5}}, integrator.getA(), 0);
        assertArrayEquals(new double[]{0.25, 0.25, 0.25, 0.25}, integrator.getB(), 0);
        assertEquals(RungeKuttaStepInterpolator.class, integrator.getPrototype().getClass());
        assertEquals(0.001, integrator.getMinStep(), 0);
        assertEquals(0.01, integrator.getMaxStep(), 0);
        assertArrayEquals(new double[]{1e-10, 1e-10}, integrator.getAbsoluteTolerance(), 0);
        assertArrayEquals(new double[]{1e-10, 1e-10}, integrator.getRelativeTolerance(), 0);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbedgedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5, 0.5},
                new double[][]{{0, 0, 0.25, 0.25}, {0.25, 0.25, 0.5, 0.5}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                new double[]{1e-10, 1e-10});
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorFsal() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF45", true,
                new double[]{0, 0.25, 0.25, 0.5, 0.5, 0.5, 0.5, 0.5},
                new double[][]{{0, 0, 0.25, 0.25, 0.325, 0.46875, 0.6125, 0.675},
                        {0.25, 0.25, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
                        {0.325, 0.46875, 0.6125, 0.675, 0.675, 0.675, 0.675, 0.675}},
                new double[]{0.325, 0.46875, 0.5625, 0.6275, 0.6875, 0.7295, 0.75625, 0.79375},
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                new double[]{1e-10, 1e-10});
        assertTrue(integrator.isFsal());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmbeddedRungeKuttaIntegratorInvalidCoefficients() {
        new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5},
                new double[][]{{0, 0, 0.25, 0.25}, {0.25, 0.25, 0.5, 0.5}},
                new double[]{0.25, 0.25, 0.25}, // missing coefficients
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                new double[]{1e-10, 1e-10});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmbeddedRungeKuttaIntegratorInvalidCoefficients2() {
        new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5},
                new double[][]{{0, 0, 0.25, 0.25}, {0.25, 0.25, 0.5, 0.5}},
                new double[]{0.25, 0.25, -1}, // negative coefficients
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                new double[]{1e-10, 1e-10});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmbeddedRungeKuttaIntegratorInvalidCoefficients3() {
        new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.25, 0.25, 0.5},
                new double[][]{{0, 0, 0.25, 0.25}, {0.25, 0.25, 0.5, 0.5}},
                new double[]{0.25, 0.25, 0.25, 2}, // too many coefficients
                new RungeKuttaStepInterpolator(), 0.001, 0.01,
                new double[]{1e-10, 1e-10});
    }

    

}