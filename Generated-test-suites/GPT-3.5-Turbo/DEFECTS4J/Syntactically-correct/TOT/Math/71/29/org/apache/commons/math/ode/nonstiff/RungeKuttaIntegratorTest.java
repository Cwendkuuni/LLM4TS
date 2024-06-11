import org.junit.Assert;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

  @Test
  public void testIntegrate() {

    // Test variables
    double t0 = 0.0;
    double[] y0 = {1.0, 1.0};
    double t = 1.0;
    double[] y = new double[2];

    // Create instance of RungeKuttaIntegrator
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator();

    // Perform integration
    integrator.integrate(new FirstOrderDifferentialEquationsImpl(), t0, y0, t, y);

    // Verify the integration result
    double[] expectedY = {2.0, 2.0};
    Assert.assertArrayEquals(expectedY, y, 1e-3);
  }
  
  @Test
  public void testIntegrate_edgeCase() {

    // Test variables
    double t0 = 0.0;
    double[] y0 = {1.0, 1.0};
    double t = 1.0;
    double[] y = new double[2];

    // Create instance of RungeKuttaIntegrator
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator();

    // Perform integration
    integrator.integrate(new FirstOrderDifferentialEquationsImpl(), t0, y0, t, y);

    // Verify the integration result
    double[] expectedY = {2.0, 2.0};
    Assert.assertArrayEquals(expectedY, y, 1e-3);
  }

  @Test
  public void testIntegrate_errorScenario() {

    // Test variables
    double t0 = 0.0;
    double[] y0 = {1.0, 1.0};
    double t = 1.0;
    double[] y = new double[2];

    // Create instance of RungeKuttaIntegrator
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator();

    // Perform integration
    try {
      integrator.integrate(null, t0, y0, t, y);
      Assert.fail("Expected DerivativeException");
    } catch (DerivativeException e) {
      // Exception is expected
    }

  }

}