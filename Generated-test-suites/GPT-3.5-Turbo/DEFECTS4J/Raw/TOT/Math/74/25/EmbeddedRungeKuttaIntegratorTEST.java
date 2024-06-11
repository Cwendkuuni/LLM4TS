import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {
        // Create the test class instance
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();

        // Define the test inputs
        FirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        // Perform the integration
        double stopTime = 0;
        try {
            stopTime = integrator.integrate(equations, t0, y0, t, y);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check the results
        assertEquals(t, stopTime, 1e-6);
        assertEquals(2.0, y[0], 1e-6);
    }

    @Test(expected = Exception.class)
    public void testInvalidIntegration() throws Exception {
        // Create the test class instance
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();

        // Define the test inputs
        FirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = {1.0};
        double t = -1.0;
        double[] y = new double[y0.length];

        // Perform the integration (should throw an exception)
        double stopTime = integrator.integrate(equations, t0, y0, t, y);
    }
}