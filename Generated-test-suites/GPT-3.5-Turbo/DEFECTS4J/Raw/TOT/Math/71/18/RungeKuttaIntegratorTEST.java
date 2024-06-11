import org.junit.Assert;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {
        // Test case for typical use case
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };

        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 0.0};
        double t = 1.0;
        double[] y = new double[2];

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test Integrator", new double[]{0.2, 0.5, 0.8}, new double[][]{{0, 0, 0}, {0.2, 0, 0}, {0.075, 0.225, 0}}, new double[]{0.2, 0.3, 0.4}, new RungeKuttaStepInterpolator(), 0.1);

        try {
            double stopTime = integrator.integrate(equations, t0, y0, t, y);
            Assert.assertEquals(0.5403023058681398, y[0], 1e-9);
            Assert.assertEquals(0.8414709848078965, y[1], 1e-9);
            Assert.assertEquals(t, stopTime, 1e-9);
        } catch (DerivativeException | IntegratorException e) {
            Assert.fail();
        }
    }

    @Test
    public void testIntegrateWithEdgeCases() {
        // Test case for edge cases
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 1;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
            }
        };

        double t0 = 0.0;
        double[] y0 = new double[]{3.0};
        double t = 2.0;
        double[] y = new double[1];

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test Integrator", new double[]{0.5}, new double[][]{{0}}, new double[]{1}, new RungeKuttaStepInterpolator(), 0.1);

        try {
            double stopTime = integrator.integrate(equations, t0, y0, t, y);
            Assert.assertEquals(2.0, y[0], 1e-9);
            Assert.assertEquals(t, stopTime, 1e-9);
        } catch (DerivativeException | IntegratorException e) {
            Assert.fail();
        }
    }

    @Test
    public void testIntegrateWithErrorScenarios() {
        // Test case for error scenarios
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = t;
                yDot[1] = y[0] / 0;
            }
        };

        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 0.0};
        double t = 1.0;
        double[] y = new double[2];

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test Integrator", new double[]{0.5}, new double[][]{{0}}, new double[]{1}, new RungeKuttaStepInterpolator(), 0.1);

        try {
            integrator.integrate(equations, t0, y0, t, y);
            Assert.fail();
        } catch (DerivativeException | IntegratorException e) {
            Assert.assertEquals("Division by zero", e.getMessage());
        }
    }
}