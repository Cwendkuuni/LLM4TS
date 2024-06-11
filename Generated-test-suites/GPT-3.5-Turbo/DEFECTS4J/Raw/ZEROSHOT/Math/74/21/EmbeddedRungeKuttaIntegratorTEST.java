import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {
        double t0 = 0.0;
        double[] y0 = {1.0, 0.0};
        double t = 1.0;
        double[] y = new double[2];

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, new double[]{0.0, 1.0}, new double[][]{{0.0}, {1.0}}, new double[]{1.0}, new RungeKuttaStepInterpolator(), 0.01, 0.1, 1e-6, 1e-6);

        double stopTime = integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        }, t0, y0, t, y);

        // Assert the result
        Assert.assertEquals(t, stopTime, 0.0);
        Assert.assertEquals(0.5403023058681398, y[0], 1e-6);
        Assert.assertEquals(0.8414709848078965, y[1], 1e-6);
    }

}