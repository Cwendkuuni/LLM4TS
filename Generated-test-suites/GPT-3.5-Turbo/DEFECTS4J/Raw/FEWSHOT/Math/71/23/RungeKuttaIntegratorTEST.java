import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.IntegratorException;
import org.junit.Test;

import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-10;

    private class DummyODE implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            yDot[0] = y[1];
            yDot[1] = -y[0];
        }
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Runge-Kutta", null, null, null, null, 0.01) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException, IntegratorException {
                // Define computeDerivatives
            }
        };

        double t0 = 0.0;
        double[] y0 = {0.0, 1.0};
        double t = 1.0;
        double[] y = new double[2];

        integrator.integrate(new DummyODE(), t0, y0, t, y);

        assertEquals(Math.cos(t), y[0], EPSILON);
        assertEquals(-Math.sin(t), y[1], EPSILON);
    }
}