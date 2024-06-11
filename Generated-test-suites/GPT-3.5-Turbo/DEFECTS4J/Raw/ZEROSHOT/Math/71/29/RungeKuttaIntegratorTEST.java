import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new RungeKuttaIntegrator("Test Integrator",
                new double[]{0.1, 0.2, 0.3},
                new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}},
                new double[]{0.11, 0.12, 0.13},
                new RungeKuttaStepInterpolator(),
                0.01);
    }

    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];
        try {
            double stopTime = integrator.integrate(equations, t0, y0, t, y);
            assertEquals(t, stopTime, 0.01);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIntegrateException() {
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y0 = new double[2];
        double t = 1.0;
        double[] y = new double[1];
        try {
            integrator.integrate(equations, t0, y0, t, y);
            fail("Exception not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof DerivativeException);
        }
    }

    private class TestEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            if (y.length != 2 || yDot.length != 2) {
                throw new DerivativeException("Invalid array length");
            }
            yDot[0] = 2 * y[0];
            yDot[1] = 3 * y[1];
        }
    }
}