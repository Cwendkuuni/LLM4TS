import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double ABSOLUTE_TOLERANCE = 1.0e-6;
    private static final double RELATIVE_TOLERANCE = 1.0e-6;

    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegrator("test", false, new double[]{0.3, 0.4},
                new double[][]{{0.237, 0.0, 0.678}, {0.0, 0.857, 0.059}}, new double[]{0.2, 0.5},
                new RungeKuttaStepInterpolator(), 0.01, 0.05, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y.length; i++) {
                    error += Math.pow(Math.abs(yDotK[1][i] - y1[i]), 2);
                }
                return Math.sqrt(error);
            }
        };
    }

    @Test
    public void testIntegrateWithFunctionalArguments() throws Exception {
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.integrate(new MyDifferentialEquations(), t0, y0, t, y);

        Assert.assertArrayEquals(y0, y, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNullEquations() throws Exception {
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[y0.length];

        integrator.integrate(null, t0, y0, t, y);
    }

    @Test(expected = IllegalStateException.class)
    public void testIntegrateWithNonCompatibleMinAndMaxStep() throws Exception {
        integrator.setMinStep(Double.POSITIVE_INFINITY);
        integrator.setMaxStep(Double.NEGATIVE_INFINITY);

        integrator.integrate(new MyDifferentialEquations(), 0.0, new double[]{1.0, 1.0}, 0.0, new double[2]);
    }

    @Test
    public void testIntegrateWithMinimalStep() throws Exception {
        integrator.setMinStep(0.0);

        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double t = 0.0;
        double[] y = new double[y0.length];

        integrator.integrate(new MyDifferentialEquations(), t0, y0, t, y);

        Assert.assertArrayEquals(y0, y, 0.0);
    }

    @Test
    public void testIntegrateWithMaximalStep() throws Exception {
        integrator.setMaxStep(Double.POSITIVE_INFINITY);

        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double t = 1000.0;
        double[] y = new double[y0.length];

        integrator.integrate(new MyDifferentialEquations(), t0, y0, t, y);

        Assert.assertArrayEquals(y0, y, 0.0);
    }

    @Test
    public void testIntegrateWithFunctionalTolerances() throws Exception {
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double t = 1.0;
        double[] vecAbsoluteTolerance = new double[]{1.0e-6, 1.0e-6};
        double[] vecRelativeTolerance = new double[]{1.0e-6, 1.0e-6};
        double[] y = new double[y0.length];

        integrator.integrate(new MyDifferentialEquations(), t0, y0, t, y, vecAbsoluteTolerance, vecRelativeTolerance);

        Assert.assertArrayEquals(y0, y, 0.0);
    }

    @Test
    public void testIntegrateWithAbsolutetoleranceSmallerThanStep() throws Exception {
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double t = 1.0;
        double[] vecAbsoluteTolerance = new double[]{0.5, 0.5};
        double[] y = new double[y0.length];

        integrator.integrate(new MyDifferentialEquations(), t0, y0, t, y, vecAbsoluteTolerance, vecRelativeTolerance);

        Assert.assertArrayEquals(y0, y, 1.0);
    }

    @Test
    public void testIntegrateWithRelativetoleranceSmallerThanAbsolutetolerance() throws Exception {
        double[] y0 = {1.0, 2.0};
        double t0 = 0.0;
        double t = 1.0;
        double[] vecAbsoluteTolerance = new double[]{1.0e-6, 1.0e-6};
        double[] vecRelativeTolerance = new double[]{-1.0e-6, -1.0e-6};
        double[] y = new double[y0.length];

        integrator.integrate(new MyDifferentialEquations(), t0, y0, t, y, vecAbsoluteTolerance, vecRelativeTolerance);

        Assert.assertArrayEquals(y0, y, 1.0);
    }

    private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getNumberOfEquations() {
            return y.length;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            for (int i = 0; i < y.length; i++) {
                yPrime[i] = y[i] * 2.0;
            }
        }

        private double[] y;
    }

}