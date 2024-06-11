
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws DerivativeException, IntegratorException {

        EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();

        FirstOrderDifferentialEquations equations = new TestDifferentialEquations();

        double t0 = 0.0;
        double[] y0 = {0.0, 0.0};
        double t = 1.0;
        double[] y = new double[2];

        double stopTime = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(t, stopTime, 0.0);
        assertEquals(1.0, y[0], 0.0);
        assertEquals(1.0, y[1], 0.0);
    }

    /**
    * Test class implementing FirstOrderDifferentialEquations
    */
    private class TestDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[0];
            yDot[1] = y[1];
        }
    }

    /**
    * Test class extending EmbeddedRungeKuttaIntegrator
    */
    private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestEmbeddedRungeKuttaIntegrator() {
            super("TestEmbeddedMethod", true, new double[]{0.0, 1.0}, new double[][]{{0.0}, {0.5, 0.5}}, new double[]{1.0, 1.0}, null, 0.0, 0.0, 0.0, 0.0);
        }

        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 0.0;
        }
    }
}
