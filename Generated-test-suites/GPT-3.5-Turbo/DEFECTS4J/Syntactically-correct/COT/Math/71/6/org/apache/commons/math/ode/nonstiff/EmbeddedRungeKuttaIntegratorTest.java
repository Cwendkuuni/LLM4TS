
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    class TestEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            yDot[0] = y[0] + y[1];
            yDot[1] = y[0] - y[1];
        }
    }

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {
        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
        double[] y0 = new double[]{1.0, 0.0};
        FirstOrderDifferentialEquations equations = new TestEquations();
        double t0 = 0.0;
        double[] y = new double[2];
        double t = 1.0;
        double result = integrator.integrate(equations, t0, y0, t, y);
        double[] expected = new double[]{Math.exp(t), Math.exp(-t)};
        assertEquals(expected[0], y[0], 1e-10);
        assertEquals(expected[1], y[1], 1e-10);
        assertEquals(t, result, 1e-10);
    }
}
