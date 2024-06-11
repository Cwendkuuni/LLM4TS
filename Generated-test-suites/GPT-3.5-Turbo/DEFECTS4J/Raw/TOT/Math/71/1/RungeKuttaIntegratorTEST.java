import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    // Test objects
    private RungeKuttaIntegrator integrator;
    private double stepSize;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        double[] c = {0.0, 0.5, 0.5, 1.0};
        double[][] a = {
            {0.0, 0.0, 0.0, 0.0},
            {0.5, 0.0, 0.0, 0.0},
            {0.0, 0.5, 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0}
        };
        double[] b = {1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        stepSize = 0.01;
        integrator = new RungeKuttaIntegrator("RK4", c, a, b, prototype, stepSize);
        equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }
            public void computeDerivatives(double t, double[] y, double[] dot) {
                dot[0] = y[1];
                dot[1] = -y[0];
            }
        };
    }

    @Test
    public void testIntegrate() {
        double t0 = 0.0;
        double t = 1.0;
        double[] y0 = {0.0, 1.0};
        double[] y = new double[2];
        try {
            integrator.integrate(equations, t0, y0, t, y);
            assertEquals(Math.cos(t), y[0], 1e-6);
            assertEquals(-Math.sin(t), y[1], 1e-6);
        } catch (Exception e) {
            fail("Integration failed with exception: " + e.getMessage());
        }
    }

}