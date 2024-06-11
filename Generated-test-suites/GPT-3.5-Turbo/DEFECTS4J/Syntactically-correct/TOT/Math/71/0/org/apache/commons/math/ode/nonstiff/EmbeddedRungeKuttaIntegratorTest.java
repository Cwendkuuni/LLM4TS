import static org.junit.Assert.assertEquals;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws DerivativeException {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 1;
            }
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = 2 * t; // Example derivative function yDot = 2t
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{1.0, 2.0, 3.0}, new double[][]{{0.0, 0.0, 0.0}, {1.0, 0.0, 0.0}, {0.5, 0.0, 0.5}}, new double[]{1.0, 0.0, 0.0}, new RungeKuttaStepInterpolator(), 0.001, 0.1, 1.0e-6, 1.0e-6);

        double t0 = 0.0;
        double[] y0 = new double[]{0.0};
        double t = 2.0;
        double[] y = new double[]{0.0};
        double actual = integrator.integrate(equations, t0, y0, t, y);

        double expected = 4.0; // expected value of the integration
        assertEquals(expected, actual, 0.001);
    }

}