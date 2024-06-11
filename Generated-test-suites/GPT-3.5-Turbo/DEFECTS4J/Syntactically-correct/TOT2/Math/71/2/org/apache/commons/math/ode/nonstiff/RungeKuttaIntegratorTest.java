import static org.junit.Assert.*;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {
        // Test for normal integration scenario
        // Set up test input
        double[] y0 = {1.0, 2.0};
        double[] y = new double[y0.length];
        double t0 = 0.0;
        double t = 1.0;
        FirstOrderDifferentialEquations equations = new TestEquations();

        // Initialize integrator
        RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();

        // Perform integration
        try {
            double finalTime = integrator.integrate(equations, t0, y0, t, y);

            // Check final time
            assertEquals(t, finalTime, 1e-10);

            // Check final values of y
            assertEquals(2.718281828459045, y[0], 1e-10);
            assertEquals(7.3890560989306495, y[1], 1e-10);

        } catch (DerivativeException | IntegratorException e) {
            fail("Integration failed: " + e.getMessage());
        }
    }

    // Edge case - Testing integration with initial time equal to final time
    @Test
    public void testIntegrateEdgeCase() {
        // Set up test input
        double[] y0 = {1.0, 2.0};
        double[] y = new double[y0.length];
        double t0 = 1.0;
        double t = 1.0;
        FirstOrderDifferentialEquations equations = new TestEquations();

        // Initialize integrator
        RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();

        // Perform integration
        try {
            double finalTime = integrator.integrate(equations, t0, y0, t, y);

            // Check final time
            assertEquals(t, finalTime, 1e-10);

            // Check final values of y remain same as initial
            assertArrayEquals(y0, y, 1e-10);

        } catch (DerivativeException | IntegratorException e) {
            fail("Integration failed: " + e.getMessage());
        }
    }

    // Error scenario - Testing integration with null equations
    @Test(expected = NullPointerException.class)
    public void testIntegrateNullEquations() {
        // Set up test input
        double[] y0 = {1.0, 2.0};
        double[] y = new double[y0.length];
        double t0 = 0.0;
        double t = 1.0;
        FirstOrderDifferentialEquations equations = null;

        // Initialize integrator
        RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();

        // Perform integration (expected to throw NullPointerException)
        integrator.integrate(equations, t0, y0, t, y);
    }

    // Custom class to implement FirstOrderDifferentialEquations for testing
    private static class TestEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0] * y[1];
            yDot[1] = y[0] + y[1];
        }
    }

    // Custom class to extend RungeKuttaIntegrator for testing
    private static class TestRungeKuttaIntegrator extends RungeKuttaIntegrator {
        private static final double[] c = {0.0, 0.5, 0.5, 1.0};
        private static final double[][] a = {{}, {0.5}, {0.0, 0.5}, {0.0, 0.0, 1.0}};
        private static final double[] b = {1.0/6, 1.0/3, 1.0/3, 1.0/6};

        public TestRungeKuttaIntegrator() {
            super("Test Integrator", c, a, b, null, 1.0);
        }
    }
}