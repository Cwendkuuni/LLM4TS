import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class EmbeddedRungeKuttaIntegratorTest {

    private final double[] c;
    private final double[][] a;
    private final double[] b;
    private final double minStep;
    private final double maxStep;
    private final double scalAbsoluteTolerance;
    private final double scalRelativeTolerance;

    public EmbeddedRungeKuttaIntegratorTest(double[] c, double[][] a, double[] b,
                                           double minStep, double maxStep,
                                           double scalAbsoluteTolerance,
                                           double scalRelativeTolerance) {
        this.c = c;
        this.a = a;
        this.b = b;
        this.minStep = minStep;
        this.maxStep = maxStep;
        this.scalAbsoluteTolerance = scalAbsoluteTolerance;
        this.scalRelativeTolerance = scalRelativeTolerance;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new double[]{0.3, 0.4, 0.1}, new double[][]{{0.25, 0.6, 0.25}, {0.25, 0.4, 0.35, 0.25}},
                        new double[]{0.25, 0.4, 0.4, 0.25}, 0.01, 0.05, 1e-6, 1e-6},
                {new double[]{0.4, 0.2, 0.3, 0.1}, new double[][]{{0.33333, 0.33333, 0.33333},
                        {0.25, 0.25, 0.5}}, new double[]{0.25, 0.25, 0.25, 0.25}, 0.01, 0.05, 1e-6, 1e-6},
                {new double[]{0.3, 0.4}, new double[][]{{0.25, 0.6}, {0.25, 0.4, 0.3}},
                        new double[]{0.25, 0.4}, 0.01, 0.05, 1e-6, 1e-6},
                {new double[]{0.3, 0.4}, new double[][]{{0.25, 0.6}, {0.25, 0.4, 0.3}},
                        new double[]{0.25, 0.4}, 0.01, 0.05, null, null},
                {new double[]{0.3, 0.4}, new double[][]{{0.25, 0.6}, {0.25, 0.4, 0.3}},
                        new double[]{0.25, 0.4}, 0.01, 0.05, new double[]{1e-8, 1e-8}, new double[]{1e-8, 1e-8}}
        });
    }

    @Test
    public void testIntegration() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK23", false, c, a, b,
                new RungeKuttaStepInterpolator(1), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.max(Math.abs(y1[0] - y0[0]), Math.abs(y1[1] - y0[1])) / h;
            }
        };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = y[2];
            }
        };

        double[] y0 = {0.1, 0.1};
        double tEnd = 1.0;

        double result = integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        double[] expected = {0.2482113343456782, 0.4964226686913565};
        assertEquals(expected[0], y0[0], 1e-6);
        assertEquals(expected[1], y0[1], 1e-6);
        assertEquals(result, tEnd, 1e-6);

        // test with fsal method
        integrator = new EmbeddedRungeKuttaIntegrator("RKF23", true, c, a, b,
                new RungeKuttaStepInterpolator(1), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return Math.max(Math.abs(y1[0] - y0[0]), Math.abs(y1[1] - y0[1])) / h;
            }
        };

        result = integrator.integrate(equations, 0.0, y0, tEnd, new double[y0.length]);
        assertEquals(result, tEnd, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK23", false, null, a, b,
                new RungeKuttaStepInterpolator(1), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters2() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK23", false, c, null, b,
                new RungeKuttaStepInterpolator(1), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters3() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK23", false, c, a, null,
                new RungeKuttaStepInterpolator(1), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters4() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK23", false, c, a, b,
                null, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters5() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK23", false, c, a, b,
                new RungeKuttaStepInterpolator(1), -1.0, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters6() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK23", false, c, a, b,
                new RungeKuttaStepInterpolator(1), minStep, -1.0, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters7() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK23", false, c, a, b,
                new RungeKuttaStepInterpolator(1), minStep, maxStep, -1.0, scalRelativeTolerance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters8() throws Exception {
        new EmbeddedRungeKuttaIntegrator("RK23", false, c, a, b,
                new RungeKuttaStepInterpolator(1), minStep, maxStep, scalAbsoluteTolerance, -1.0);
    }
}