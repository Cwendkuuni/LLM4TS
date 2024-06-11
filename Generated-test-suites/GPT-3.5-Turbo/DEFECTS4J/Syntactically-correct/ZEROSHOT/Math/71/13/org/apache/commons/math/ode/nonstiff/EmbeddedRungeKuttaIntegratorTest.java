import org.junit.Test;
import org.junit.Assert;

public class EmbeddedRungeKuttaIntegratorTest {
  
  @Test
  public void testIntegrate() throws Exception {
    // Create instance of the class to be tested
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("testMethod", true, new double[]{}, new double[][]{}, new double[]{}, null, 0.0, 0.0, 0.0, 0.0);
    
    // Test input parameters
    Assert.assertEquals("testMethod", integrator.getName());
    Assert.assertEquals(true, integrator.fsal);
    Assert.assertEquals(0, integrator.c.length);
    Assert.assertEquals(0, integrator.a.length);
    Assert.assertEquals(0, integrator.b.length);
    Assert.assertEquals(null, integrator.prototype);
    Assert.assertEquals(0.0, integrator.minStep, 0.0);
    Assert.assertEquals(0.0, integrator.maxStep, 0.0);
    Assert.assertEquals(0.0, integrator.scalAbsoluteTolerance, 0.0);
    Assert.assertEquals(0.0, integrator.scalRelativeTolerance, 0.0);
    
    // Create the FirstOrderDifferentialEquations implementation
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return 1;
      }
      
      @Override
      public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
        // Do nothing
      }
    };
    
    // Test integrate method
    double t0 = 0.0;
    double[] y0 = new double[]{0.0};
    double t = 1.0;
    double[] y = new double[]{1.0};
    double result = integrator.integrate(equations, t0, y0, t, y);
    
    Assert.assertEquals(t0, result, 0.0);
  }
}