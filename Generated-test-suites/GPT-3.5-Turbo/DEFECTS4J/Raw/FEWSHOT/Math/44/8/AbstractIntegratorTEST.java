import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {
    private AbstractIntegrator integrator;

    @Before
    public void setup() {
        integrator = new AbstractIntegratorImpl();
    }

    @Test
    public void testIntegrate() {
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double t = 1.0;
        double[] y = new double[y0.length];

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 3;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + y[1];
                yDot[1] = y[1] + y[2];
                yDot[2] = y[2] + y[0];
            }
        };

        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            Assert.assertEquals(1.0, result, 1e-10);
            Assert.assertArrayEquals(new double[] {2.0, 4.0, 6.0}, y, 1e-10);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}