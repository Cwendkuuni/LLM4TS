import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {

        // Define the test case
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = y[0] + t;
                yDot[1] = y[1] - t;
            }
        };

        double t0 = 0.0;
        double[] y0 = {0.0, 0.0};
        double t = 1.0;
        double[] y = {0.0, 0.0};

        // Create the integrator
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator",
                false, new double[]{0.5, 1.0}, new double[][]{{0.5}, {0.0, 1.0}}, new double[]{0.5, 0.5},
                new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-6);

        // Perform the integration
        try {
            double stopTime = integrator.integrate(equations, t0, y0, t, y);

            // Check the result
            assertEquals(1.5, y[0], 1e-6);
            assertEquals(-0.5, y[1], 1e-6);
            assertEquals(1.0, stopTime, 1e-6);
        } catch (DerivativeException | IntegratorException e) {
            fail(e.getMessage());
        }
    }

}