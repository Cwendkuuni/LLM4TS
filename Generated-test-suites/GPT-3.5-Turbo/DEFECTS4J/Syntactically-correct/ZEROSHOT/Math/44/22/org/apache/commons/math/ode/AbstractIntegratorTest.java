import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public class AbstractIntegratorTest {

    @Test
    public void testIntegrate() {
        // Set up test data
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };
        double t0 = 0.0;
        double t = 1.0;
        double[] y0 = {0.0, 1.0};
        double[] y = new double[2];

        // Create AbstractIntegrator instance
        AbstractIntegrator integrator = new AbstractIntegrator("test") {
            public void integrate(ExpandableStatefulODE equations, double t)
                throws MathIllegalStateException, MathIllegalArgumentException {
                equations.setTime(t);
                equations.setPrimaryState(new double[] {0.0, 1.0});
            }
        };

        // Perform integration
        double finalTime = 0;
        try {
            finalTime = integrator.integrate(equations, t0, y0, t, y);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assert integration result
        assertTrue(finalTime == t);
        assertArrayEquals(y, new double[] {0.8414709848078965, 0.5403023058681397}, 0.00001);
    }
}