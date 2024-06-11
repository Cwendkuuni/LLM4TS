import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {
  
  @Test
  public void testIntegrate() {
    // Create the test object
    TestRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    
    // Create the test function
    TestFirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
    
    // Set initial values and time range
    double t0 = 0.0;
    double[] y0 = {1.0, 2.0, 3.0};
    double t = 1.0;
    double[] y = {0.0, 0.0, 0.0};
    
    try {
      // Execute the integration
      double stopTime = integrator.integrate(equations, t0, y0, t, y);
      
      // Verify the integration result
      assertEquals(1.0, stopTime, 0.0001);
      assertEquals(2.7183, y[0], 0.0001);
      assertEquals(7.3891, y[1], 0.0001);
      assertEquals(20.0855, y[2], 0.0001);
      
    } catch (DerivativeException | IntegratorException e) {
      fail("Exception thrown during integration: " + e.getMessage());
    }
  }
  
  @Test
  public void testIntegrateWithInvalidTimeRange() {
    // Create the test object
    TestRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    
    // Create the test function
    TestFirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
    
    // Set initial values and invalid time range
    double t0 = 0.0;
    double[] y0 = {1.0, 2.0, 3.0};
    double t = 0.0;
    double[] y = {0.0, 0.0, 0.0};
    
    try {
      // Execute the integration
      double stopTime = integrator.integrate(equations, t0, y0, t, y);
      
      // Verify the integration result
      assertEquals(0.0, stopTime, 0.0001);
      assertEquals(1.0, y[0], 0.0001);
      assertEquals(2.0, y[1], 0.0001);
      assertEquals(3.0, y[2], 0.0001);
      
      fail("No exception thrown for invalid time range: t < t0");
      
    } catch (DerivativeException | IntegratorException e) {
      // Exception expected
    }
    
    // Set initial values and invalid time range
    t0 = 1.0;
    y0 = new double[]{1.0, 2.0, 3.0};
    t = 0.0;
    y = new double[]{0.0, 0.0, 0.0};
    
    try {
      // Execute the integration
      double stopTime = integrator.integrate(equations, t0, y0, t, y);
      
      // Verify the integration result
      assertEquals(1.0, stopTime, 0.0001);
      assertEquals(2.7183, y[0], 0.0001);
      assertEquals(7.3891, y[1], 0.0001);
      assertEquals(20.0855, y[2], 0.0001);
      
      fail("No exception thrown for invalid time range: t < t0");
      
    } catch (DerivativeException | IntegratorException e) {
      // Exception expected
    }
  }
  
  @Test
  public void testIntegrateWithErrorsInDerivatives() {
    // Create the test object
    TestRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    
    // Create the test function
    TestFirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
    
    // Set initial values and time range
    double t0 = 0.0;
    double[] y0 = {1.0, 2.0, 3.0};
    double t = 1.0;
    double[] y = {0.0, 0.0, 0.0};
    
    try {
      // Set an invalid derivative value
      equations.setErrorInDerivatives(1);
      
      // Execute the integration
      double stopTime = integrator.integrate(equations, t0, y0, t, y);
      
      // Verify the integration result
      assertEquals(1.0, stopTime, 0.0001);
      
      fail("No exception thrown for errors in derivative computation");
      
    } catch (DerivativeException | IntegratorException e) {
      // Exception expected
    }
    
    // Reset the error in the derivatives
    equations.setErrorInDerivatives(0);
  }
  
  private class TestRungeKuttaIntegrator extends RungeKuttaIntegrator {
    
    // Implement abstract methods and constructor
    
    @Override
    protected void computeDerivatives(double t, double[] y, double[] yDot)
        throws DerivativeException {
      // Compute the derivatives using a test function
      for (int i = 0; i < y.length; i++) {
        yDot[i] = 1.0 / (1.0 + Math.exp(-y[i]));
      }
    }
    
    @Override
    protected boolean requiresDenseOutput() {
      return false;
    }
  }
  
  private class TestFirstOrderDifferentialEquations implements FirstOrderDifferentialEquations {
    
    private int errorInDerivatives = 0;
    
    public void setErrorInDerivatives(int errorInDerivatives) {
      this.errorInDerivatives = errorInDerivatives;
    }
    
    @Override
    public int getDimension() {
      return 3;
    }
    
    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot)
        throws DerivativeException {
      if (errorInDerivatives == 1) {
        throw new DerivativeException("Error in derivative computation");
      }
      
      // Compute the derivatives using a test function
      for (int i = 0; i < y.length; i++) {
        yDot[i] = 1.0 / (1.0 + Math.exp(-y[i]));
      }
    }
  }
}