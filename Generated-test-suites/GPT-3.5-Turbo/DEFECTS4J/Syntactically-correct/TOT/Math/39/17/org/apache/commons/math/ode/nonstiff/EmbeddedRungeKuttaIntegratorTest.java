import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

  private EmbeddedRungeKuttaIntegrator integrator;

  @Before
  public void setUp() {
    integrator = new TestEmbeddedRungeKuttaIntegrator();
  }

  @Test
  public void testConstructor() {
    assertNotNull(integrator);
  }

  @Test
  public void testGetOrder() {
    assertEquals(4, integrator.getOrder());
  }

  @Test
  public void testGetSafety() {
    assertEquals(0.9, integrator.getSafety(), 0.0);
  }

  @Test
  public void testSetSafety() {
    integrator.setSafety(0.8);
    assertEquals(0.8, integrator.getSafety(), 0.0);
  }

  @Test
  public void testGetMinReduction() {
    assertEquals(0.2, integrator.getMinReduction(), 0.0);
  }

  @Test
  public void testSetMinReduction() {
    integrator.setMinReduction(0.1);
    assertEquals(0.1, integrator.getMinReduction(), 0.0);
  }

  @Test
  public void testGetMaxGrowth() {
    assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
  }

  @Test
  public void testSetMaxGrowth() {
    integrator.setMaxGrowth(20.0);
    assertEquals(20.0, integrator.getMaxGrowth(), 0.0);
  }

  @Test
  public void testEstimateError() {
    double[][] yDotK = new double[4][4];
    double[] y0 = {1.0, 2.0, 3.0, 4.0};
    double[] y1 = {2.0, 3.0, 4.0, 5.0};
    double h = 0.1;
    double error = integrator.estimateError(yDotK, y0, y1, h);
    assertEquals(1.0, error, 0.0);
  }

  @Test
  public void testIntegrate() {
    // TODO: Add test cases for integrate method
  }

  // Custom embedded Runge-Kutta integrator for testing purposes
  private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
  
    public TestEmbeddedRungeKuttaIntegrator() {
      super("Test", true, new double[]{0.0, 0.5, 0.5, 1.0},
            new double[][]{{}, {0.5}, {-1.0, 2.0}, {0.5, 0.0, 0.5}},
            new double[]{1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0},
            null, 0.0, 0.0, 0.0, 0.0);
    }
  
    public int getOrder() {
      return 4;
    }
  
    protected double estimateError(double[][] yDotK, 
                                   double[] y0, double[] y1, double h) {
      return 1.0;
    }
  
    protected void computeDerivatives(double t, double[] y, double[] yDot) {
      // Do nothing
    }
  
  }

}