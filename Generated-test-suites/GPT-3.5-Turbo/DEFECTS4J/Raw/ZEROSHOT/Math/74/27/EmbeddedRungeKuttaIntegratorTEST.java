import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {
        // Create test class instance
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquationsImpl();
        final double t0 = 0;
        final double[] y0 = new double[] {1.0, 1.0};
        final double t = 1;
        final double[] y = new double[y0.length];
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorImpl();

        try {
            // Invoke method to be tested
            integrator.integrate(equations, t0, y0, t, y);

            // Assert test results
            Assert.assertEquals(0, Double.compare(1.0, y[0]));
            Assert.assertEquals(0, Double.compare(1.0, y[1]));

        } catch (Exception e) {
            Assert.fail("Exception was thrown: " + e.getMessage());
        }
    }

    private class FirstOrderDifferentialEquationsImpl implements FirstOrderDifferentialEquations {
        public void computeDerivatives(double var1, double[] var2, double[] var3) throws DerivativeException {

        }

        public int getDimension() {
            return 0;
        }
    }

    private class EmbeddedRungeKuttaIntegratorImpl extends EmbeddedRungeKuttaIntegrator {
        public EmbeddedRungeKuttaIntegratorImpl() {
            super("name", false, new double[] {0.0, 0.5, 1.0}, new double[][] {{0.5}, {0.0, 0.5}}, new double[] {0.5, 0.5}, null, 0, 0, 0, 0);
        }

        public int getOrder() {
            return 0;
        }

        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0;
        }
    }
}