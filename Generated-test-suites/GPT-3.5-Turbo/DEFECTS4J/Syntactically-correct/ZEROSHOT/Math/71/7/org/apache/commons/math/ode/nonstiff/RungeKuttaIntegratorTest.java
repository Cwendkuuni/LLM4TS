import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private class TestEquations implements FirstOrderDifferentialEquations {

        public int getDimension() {
            return 2;
        }

        public void computeDerivatives(double t, double[] y, double[] yDot)
                throws DerivativeException {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test Integrator",
                new double[]{0.0, 0.5, 0.5, 1.0},
                new double[][]{{0.0}, {0.5}, {0.0, 0.5}, {0.0, 0.0, 1.0}},
                new double[]{1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0},
                new RungeKuttaStepInterpolator(), 0.1);

        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = {0.0, 1.0};
        double t = 1.0;
        double[] y = new double[2];

        double stopTime = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(t, stopTime, 1e-6);
        assertEquals(Math.cos(t), y[0], 1e-6);
        assertEquals(-Math.sin(t), y[1], 1e-6);
    }

    @Test
    public void testIntegrateBackward() throws DerivativeException, IntegratorException {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test Integrator",
                new double[]{0.0, -0.5, -0.5, -1.0},
                new double[][]{{0.0}, {-0.5}, {0.0, -0.5}, {0.0, 0.0, -1.0}},
                new double[]{1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0},
                new RungeKuttaStepInterpolator(), 0.1);

        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 1.0;
        double[] y0 = {Math.cos(t0), -Math.sin(t0)};
        double t = 0.0;
        double[] y = new double[2];

        double stopTime = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(t, stopTime, 1e-6);
        assertEquals(0.0, y[0], 1e-6);
        assertEquals(1.0, y[1], 1e-6);
    }
}