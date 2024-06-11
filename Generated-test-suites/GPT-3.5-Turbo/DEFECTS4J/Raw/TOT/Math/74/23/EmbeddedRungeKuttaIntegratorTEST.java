import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {
        // Test case for integrate method
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", true, 
                                                                                  new double[]{0.0, 0.0, 0.0, 0.0}, 
                                                                                  new double[][]{{},{},{}},
                                                                                  new double[]{0.0, 0.0, 0.0}, 
                                                                                  new RungeKuttaStepInterpolator(),
                                                                                  0.0, 0.0, 
                                                                                  new double[]{0.0}, 
                                                                                  new double[]{0.0}) {
            public int getOrder() {
                return 4;
            }
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0.0;
            }
        };

        double t0 = 0.0;
        double[] y0 = {1.0, 0.0};
        double t = 1.0;
        double[] y = new double[2];

        try {
            integrator.integrate(equations, t0, y0, t, y);
        } catch (DerivativeException | IntegratorException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        double[] expected = {0.54030230586814, 0.8414709848079};
        for (int i = 0; i < y.length; i++) {
            assertEquals(expected[i], y[i], 1e-15);
        }
    }

    // Additional test cases can be added here

}