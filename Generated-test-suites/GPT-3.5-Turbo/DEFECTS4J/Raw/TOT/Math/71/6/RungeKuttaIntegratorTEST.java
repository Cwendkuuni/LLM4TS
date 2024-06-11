import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private static final double EPSILON = 1e-10;

    @Before
    public void setUp() {
        integrator = new RungeKuttaIntegrator("Test", new double[]{0, 0.5, 0.5, 1.0}, 
                        new double[][]{{0}, {0.5}, {0, 0.5}, {1.0, -1.0, 2.0}}, 
                        new double[]{0, 0.5, 0.5, 1.0}, 
                        new RungeKuttaStepInterpolator(), 0.1);
    }
    
    // Test case for integrate() method
    @Test
    public void testIntegrate() {
        // Test typical use case
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }
            public void computeDerivatives(double t, double[] y, double[] yDot)
                throws DerivativeException {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };
        double[] y0 = new double[]{1.0, 0.0};
        double t = 10.0;
        double[] y = new double[2];
        try {
            integrator.integrate(equations, 0.0, y0, t, y);
        } catch (DerivativeException e) {
            fail("DerivativeException should not be thrown");
        } catch (IntegratorException e) {
            fail("IntegratorException should not be thrown");
        }

        assertEquals(-1.1626705053546665, y[0], EPSILON);
        assertEquals(0.556885668364945, y[1], EPSILON);

        // Test edge case
        equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }
            public void computeDerivatives(double t, double[] y, double[] yDot)
                throws DerivativeException {
                yDot[0] = y[1];
                yDot[1] = -y[0];
            }
        };
        y0 = new double[]{0.0, 1.0};
        t = 0.0;
        y = new double[2];
        try {
            integrator.integrate(equations, 0.0, y0, t, y);
        } catch (DerivativeException e) {
            fail("DerivativeException should not be thrown");
        } catch (IntegratorException e) {
            fail("IntegratorException should not be thrown");
        }

        assertEquals(0.0, y[0], EPSILON);
        assertEquals(1.0, y[1], EPSILON);

        // Test error scenario
        equations = new FirstOrderDifferentialEquations() {
            public int getDimension() {
                return 2;
            }
            public void computeDerivatives(double t, double[] y, double[] yDot)
                throws DerivativeException {
                throw new DerivativeException();
            }
        };
        y0 = new double[]{1.0, 0.0};
        t = 10.0;
        y = new double[2];
        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("DerivativeException should be thrown");
        } catch (DerivativeException e) {
            // success
        } catch (IntegratorException e) {
            fail("IntegratorException should not be thrown");
        }
    }
}