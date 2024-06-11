import org.junit.Test;

public class RungeKuttaIntegratorTest {
  
  @Test
  public void testIntegrate() {
    // Create an instance of RungeKuttaIntegrator
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator();

    // Create an instance of FirstOrderDifferentialEquations
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations();

    // Define the initial values for t0, y0, t, and y
    double t0 = 0.0;
    double[] y0 = { 0.0 };
    double t = 1.0;
    double[] y = new double[1];

    // Call the integrate method
    double result = integrator.integrate(equations, t0, y0, t, y);

    // Check if the result is correct
    assertEquals(1.0, result, 0.0001);
  }

  @Test
  public void testComputeDerivatives() {
    // Create an instance of RungeKuttaIntegrator
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator();

    // Create an instance of FirstOrderDifferentialEquations
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations();

    // Define the values for t, y, and yDot
    double t = 0.0;
    double[] y = { 1.0 };
    double[] yDot = new double[1];

    // Call the computeDerivatives method
    integrator.computeDerivatives(t, y, yDot);

    // Check if the result is correct
    assertEquals(1.0, yDot[0], 0.0001);
  }
}