import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

  private RungeKuttaIntegrator integrator;
  private FirstOrderDifferentialEquations equations;
  private double[] y0;

  @Before
  public void setUp() {
    double[] c = { 0.0, 0.2, 0.3, 0.6, 1.0 };
    double[][] a = { { 0.0 }, { 0.2 }, { 0.3, 0.6 }, { 0.4, -0.8, 0.2 }, { -8.0/9.0, 2.0, -3544.0/2565 , 1859.0/4104.0 } };
    double[] b = { 1.0/9.0, 0.0, 0.0, 5048.0/14641.0, 3785.0/102672.0, -42.0/62.0 };
    RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    double step = 1.0;

    // Initialize the integrator
    integrator = new RungeKuttaIntegrator("TestIntegrator", c, a, b, prototype, step);

    // Initialize the differential equations
    equations = new FirstOrderDifferentialEquations() {
      public int getDimension() {
        return 2;
      }
      public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
        yDot[0] = y[1];
        yDot[1] = -y[0];
      }
    };

    // Initialize the initial state vector
    y0 = new double[] { 0.0, 1.0 };
  }

  @Test
  public void testIntegrate() throws DerivativeException, IntegratorException {
    double t0 = 0.0;
    double t = 10.0;
    double[] y = new double[2];

    // Integrate the differential equations
    double stopTime = integrator.integrate(equations, t0, y0, t, y);

    // Check the integration result
    assertEquals(t, stopTime, 1e-10);
    assertEquals(0.0, y[0], 1e-10);
    assertEquals(1.0, y[1], 1e-10);
  }

  @Test(expected = DerivativeException.class)
  public void testComputeDerivativesException() throws DerivativeException {
    double t = 0.0;
    double[] y = new double[2];
    double[] yDot = new double[2];

    // Compute the derivatives with an invalid state vector size
    equations.computeDerivatives(t, y, yDot);
  }

}