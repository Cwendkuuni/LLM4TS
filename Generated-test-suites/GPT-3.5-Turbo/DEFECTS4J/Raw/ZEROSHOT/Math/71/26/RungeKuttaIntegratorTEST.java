import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private class DummyFirstOrderDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            yDot[0] = Math.sin(t);
        }
    }

    private RungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new RungeKuttaIntegrator("Test Integrator", new double[]{0.5, 1.0}, new double[][]{{0.5}, {0.0, 1.0}}, new double[]{1.0, 2.0}, new RungeKuttaStepInterpolator(), 0.1);
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        FirstOrderDifferentialEquations equations = new DummyFirstOrderDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = new double[]{0.0};
        double t = 1.0;
        double[] y = new double[1];

        double result = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(1.0, result, 1e-10);
        assertEquals(Math.sin(1.0), y[0], 1e-10);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateWithDerivativeException() throws DerivativeException, IntegratorException {
        FirstOrderDifferentialEquations equations = new DummyFirstOrderDifferentialEquations() {
            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                throw new DerivativeException();
            }
        };
        double t0 = 0.0;
        double[] y0 = new double[]{0.0};
        double t = 1.0;
        double[] y = new double[1];

        integrator.integrate(equations, t0, y0, t, y);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithIntegratorException() throws DerivativeException, IntegratorException {
        FirstOrderDifferentialEquations equations = new DummyFirstOrderDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = new double[]{0.0};
        double t = 1.0;
        double[] y = new double[1];

        integrator.integrate(equations, t0, y0, t, y);
        integrator.integrate(equations, t0, y0, t, y);
    }
}