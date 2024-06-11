import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;

public class EmbeddedRungeKuttaIntegratorTest {
  
  private EmbeddedRungeKuttaIntegrator integrator;

  @Before
  public void setUp() {
    // Set up the EmbeddedRungeKuttaIntegrator instance
    double[] c = {0.2, 0.3, 0.4, 1.0};
    double[][] a = {{0.2, 0.3, 0.4}, {0.075, 0.225, 0.3}, {0.3, -0.9, 1.2, 0.6}};
    double[] b = {0.3333333333333333, 0.0, 1.0, 0.5};
    RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    double minStep = 0.0;
    double maxStep = 0.1;
    double scalAbsoluteTolerance = 1e-6;
    double scalRelativeTolerance = 1e-6;
    
    integrator = new EmbeddedRungeKuttaIntegrator("Test Integrator", false, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
  }

  @Test
  public void testGetSafety() {
    // Test that getSafety returns the correct value
    assertEquals(0.9, integrator.getSafety(), 0.0);
  }

  @Test
  public void testSetSafety() {
    // Test that setSafety correctly sets the safety value
    integrator.setSafety(0.8);
    assertEquals(0.8, integrator.getSafety(), 0.0);
  }

  @Test
  public void testGetMinReduction() {
    // Test that getMinReduction returns the correct value
    assertEquals(0.2, integrator.getMinReduction(), 0.0);
  }

  @Test
  public void testSetMinReduction() {
    // Test that setMinReduction correctly sets the minReduction value
    integrator.setMinReduction(0.1);
    assertEquals(0.1, integrator.getMinReduction(), 0.0);
  }

  @Test
  public void testGetMaxGrowth() {
    // Test that getMaxGrowth returns the correct value
    assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
  }

  @Test
  public void testSetMaxGrowth() {
    // Test that setMaxGrowth correctly sets the maxGrowth value
    integrator.setMaxGrowth(20.0);
    assertEquals(20.0, integrator.getMaxGrowth(), 0.0);
  }
  
  @Test
  public void testEstimateError() {
    // Test that estimateError returns the correct error ratio
    double[][] yDotK = {{0.1, 0.2, 0.3}, {0.05, 0.15, 0.25}, {0.2, -0.6, 0.8, 0.4}};
    double[] y0 = {1.0, 2.0, 3.0};
    double[] y1 = {1.1, 2.4, 3.9};
    double h = 0.1;
    
    double error = integrator.estimateError(yDotK, y0, y1, h);
    
    assertEquals(0.32544336897703884, error, 0.0);
  }
}