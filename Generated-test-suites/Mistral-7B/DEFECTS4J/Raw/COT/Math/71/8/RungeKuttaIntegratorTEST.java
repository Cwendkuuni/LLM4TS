import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultCombinedEventManager;
import org.apache.commons.math3.ode.sampling.DummyStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] y0 = new double[] {0.0, 0.0};
    private double[] y = new double[] {0.0, 0.0};
    private double[] yDotK = new double[5][];
    private double step = 0.1;

    @Before
    public void setup() {
        double[] c = {0.25, 0.5, 0.5, 0.5, 1.0};
        double[][] a = {{0.0, 0.0}, {0.25, 0.0}, {0.0, 0.5}, {0.0, 0.5}, {0.0, 1.0}};
        double[] b = {0.25, 0.5, 0.5, 0.5, 1.0};
        integrator = new RungeKuttaIntegrator("RK4", c, a, b, new RungeKuttaStepInterpolator(), step) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yDot) {
                System.arraycopy(y, 0, yDot, 0, 2);
            }
        };
        equations = new MockDifferentialEquations();
    }

    @Test
    public void testIntegrate() throws Exception {
        double t = 1.0;
        double result = integrator.integrate(equations, 0.0, y0, t, y);
        Assert.assertEquals(t, result, 1e-9);
        Assert.assertEquals(y[0], 0.6666666874336217, 1e-6);
        Assert.assertEquals(y[1], 0.6666666874336217, 1e-6);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        double[] yDot = new double[2];
        equations = new DerivativeExceptionDifferentialEquations();
        integrator.integrate(equations, 0.0, y0, 0.1, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        equations = new IntegratorExceptionDifferentialEquations();
        integrator.integrate(equations, 0.0, y0, 0.1, y);
    }

    @Test
    public void testStepHandlers() throws Exception {
        DefaultCombinedEventManager manager = new DefaultCombinedEventManager();
        manager.addEvent(0.5, new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int step) {
                Assert.assertEquals(0.5, interpolator.getTime(), 1e-9);
                y[0] = 1.0;
                y[1] = 1.0;
            }
        });
        integrator = new RungeKuttaIntegrator("RK4", c, a, b, new RungeKuttaStepInterpolator(), step) {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                System.arraycopy(y, 0, yDot, 0, 2);
            }

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int step) {
                super.handleStep(interpolator, step);
                System.out.println("Handle step " + step);
            }
        };

        integrator.integrate(equations, 0.0, y0, 1.0, y);
        Assert.assertEquals(1.0, y[0], 1e-6);
        Assert.assertEquals(1.0, y[1], 1e-6);
    }

    @Test
    public void testDenseOutput() throws Exception {
        RungeKuttaStepInterpolator rki = new RungeKuttaStepInterpolator();
        integrator = new RungeKuttaIntegrator("RK4", c, a, b, rki, step) {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                System.arraycopy(y, 0, yDot, 0, 2);
            }

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int step) {
                super.handleStep(interpolator, step);
                yDotK = interpolator.getDenseOutput(yDot);
            }
        };

        integrator.integrate(equations, 0.0, y0, 1.0, y);
        Assert.assertArrayEquals(new double[][] {{0.0, 0.0}, {0.3333333333333333, 0.3333333333333333},
                {0.6666666666666667, 0.6666666666666667}, {1.0, 1.0}, {1.0, 1.0}}, yDotK, 0.01);
    }

    private static class MockDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            Arrays.fill(yDot, 0.0);
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }

    private static class DerivativeExceptionDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            throw new DerivativeException("Derivative not provided");
        }
    }

    private static class IntegratorExceptionDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            throw new IntegratorException("Integration exception");
        }
    }
}