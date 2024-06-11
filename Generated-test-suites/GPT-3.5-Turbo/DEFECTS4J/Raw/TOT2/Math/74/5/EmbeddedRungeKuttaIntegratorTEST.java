import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
  
  @Test
  public void testConstructor1() {
    // Test Constructor with scalar tolerances
    double[] c = {0.0, 1.0};
    double[][] a = {{0.0, 0.0}, {0.5, 0.0}};
    double[] b = {0.5, 0.5};
    RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test1", true, c, a, b, prototype, 1.0, 10.0, 1e-6, 1e-6);
    
    assertNotNull(integrator);
    assertEquals("Test1", integrator.getName());
    assertTrue(integrator.getSafety() == 0.9);
    assertTrue(integrator.getMinReduction() == 0.2);
    assertTrue(integrator.getMaxGrowth() == 10.0);
  }
  
  @Test
  public void testConstructor2() {
    // Test Constructor with vector tolerances
    double[] c = {0.0, 1.0};
    double[][] a = {{0.0, 0.0}, {0.5, 0.0}};
    double[] b = {0.5, 0.5};
    RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    double[] vecAbsoluteTolerance = {1e-6, 1e-6};
    double[] vecRelativeTolerance = {1e-6, 1e-6};
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test2", true, c, a, b, prototype, 1.0, 10.0, vecAbsoluteTolerance, vecRelativeTolerance);
    
    assertNotNull(integrator);
    assertEquals("Test2", integrator.getName());
    assertTrue(integrator.getSafety() == 0.9);
    assertTrue(integrator.getMinReduction() == 0.2);
    assertTrue(integrator.getMaxGrowth() == 10.0);
  }
  
  @Test
  public void testGetOrder() {
    // Test getOrder method
    double[] c = {0.0, 1.0};
    double[][] a = {{0.0, 0.0}, {0.5, 0.0}};
    double[] b = {0.5, 0.5};
    RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test3", true, c, a, b, prototype, 1.0, 10.0, 1e-6, 1e-6);
    
    assertEquals(2, integrator.getOrder());
  }
  
  // Add more test cases for other methods
  
}