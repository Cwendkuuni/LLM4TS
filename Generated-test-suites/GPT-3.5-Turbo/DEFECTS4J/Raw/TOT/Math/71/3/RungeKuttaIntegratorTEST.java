import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new RungeKuttaIntegrator() {
          public void computeDerivatives(double t, double[] y, double[] yDot) {
            
          }
        };

        equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + t;
                yDot[1] = y[1] - t;
            }
        };
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];

        double result = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(1.0, result, 1e-10);
        assertEquals(3.0, y[0], 1e-10);
        assertEquals(1.0, y[1], 1e-10);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateWithDerivativeException() throws DerivativeException, IntegratorException {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];

        integrator = new RungeKuttaIntegrator() {
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                throw new DerivativeException();
            }
        };

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithIntegratorException() throws DerivativeException, IntegratorException {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];

        integrator = new RungeKuttaIntegrator() {
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                throw new DerivativeException();
            }

            public boolean requiresDenseOutput() {
                return true;
            }
        };

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test
    public void testIntegrateWithZeroStep() throws DerivativeException, IntegratorException {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 0.0;
        double[] y = new double[2];

        double result = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(0.0, result, 1e-10);
        assertArrayEquals(y0, y, 1e-10);
    }

    @Test
    public void testIntegrateBackwards() throws DerivativeException, IntegratorException {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = -1.0;
        double[] y = new double[2];

        double result = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(-1.0, result, 1e-10);
        assertEquals(3.0, y[0], 1e-10);
        assertEquals(-1.0, y[1], 1e-10);
    }
}