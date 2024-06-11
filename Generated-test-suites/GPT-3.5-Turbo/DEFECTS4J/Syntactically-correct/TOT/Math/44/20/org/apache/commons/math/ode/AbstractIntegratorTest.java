import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testIntegrate() {
        // test a typical use case
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };

        AbstractIntegrator integrator = new ConcreteIntegrator();

        double t0 = 0.0;
        double[] y0 = {0.0, 1.0};
        double t = 10.0;
        double[] y = new double[2];

        double result = integrator.integrate(equations, t0, y0, t, y);

        assertEquals(result, t, 0.0);
        assertEquals(y[0], Math.cos(t), 0.01);
        assertEquals(y[1], -Math.sin(t), 0.01);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateDimensionMismatchException() {
        // test exception when the dimension of the initial state does not match the dimension of the equations
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // do nothing
            }
        };

        AbstractIntegrator integrator = new ConcreteIntegrator();

        double t0 = 0.0;
        double[] y0 = {0.0}; // dimension does not match
        double t = 10.0;
        double[] y = new double[2];

        integrator.integrate(equations, t0, y0, t, y);
    }

}