import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + y[1];
                yDot[1] = y[1] - y[0];
            }
        };

        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
                // Do nothing
            }
        };

        double t0 = 0.0;
        double[] y0 = {1.0, 1.0};
        double t = 1.0;
        double[] y = new double[2];

        try {
            double result = integrator.integrate(equations, t0, y0, t, y);
            assertEquals(t, result, 0.0);
            assertEquals(1.0, y[0], 0.0);
            assertEquals(1.0, y[1], 0.0);
        } catch (MathIllegalStateException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (MathIllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testComputeDerivatives() {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }

            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + y[1];
                yDot[1] = y[1] - y[0];
            }
        });

        AbstractIntegrator integrator = new AbstractIntegrator() {
            public void integrate(ExpandableStatefulODE equations, double t) {
                // Do nothing
            }
        };

        double[] y = {1.0, 2.0};
        double[] yDot = new double[2];

        try {
            integrator.computeDerivatives(0.0, y, yDot);

            assertEquals(-3.0, yDot[0], 0.0);
            assertEquals(1.0, yDot[1], 0.0);
        } catch (MaxCountExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}