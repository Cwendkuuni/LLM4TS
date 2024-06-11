import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.IntegratorException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {
    private RungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new RungeKuttaIntegrator("TestIntegrator", new double[]{1.0, 2.0}, new double[][]{{0.5, 0.5}, {0.25, 0.25}}, new double[]{0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.1);
    }

    @After
    public void tearDown() {
        integrator = null;
    }

    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
                yDot[0] = -10 * y[0];
                yDot[1] = 10 * y[0];
            }

            public int getDimension() {
                return 2;
            }
        };

        double t0 = 0.0;
        double[] y0 = new double[]{1.0, 0.0};
        double t = 1.0;
        double[] y = new double[2];

        try {
            double stopTime = integrator.integrate(equations, t0, y0, t, y);
            assertEquals(t, stopTime, 1e-12);
            assertEquals(-9.049547903890123e-05, y[0], 1e-12);
            assertEquals(9.049547903890123e-05, y[1], 1e-12);
        } catch (DerivativeException e) {
            fail("DerivativeException occurred");
        } catch (IntegratorException e) {
            fail("IntegratorException occurred");
        }
    }

}