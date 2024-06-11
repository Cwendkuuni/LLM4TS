import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

  private EmbeddedRungeKuttaIntegrator integrator;
  private double[] c;
  private double[][] a;
  private double[] b;

  @Before
  public void setUp() {
    c = new double[] {1.0/2, 3.0/4, 5.0/6, 1.0/2};
    a = new double[][] {
      {1}, 
      {2.0/3, 1.0/3},
      {1.0/6, 1.0/6, 4.0/6},
      {1.0/2, 3.0/2, -4.0,    6.0 }
    };
    b = new double[] {0.125, 0.375, 0.375, 0.125};
    integrator = new EmbeddedRungeKuttaIntegratorImpl("Test Integrator", false, c, a, b);
  }

  @Test
  public void testGetSafety() {
    double expected = 0.9;
    double actual = integrator.getSafety();
    assertEquals(expected, actual, 1e-10);
  }

  @Test
  public void testSetSafety() {
    double expected = 0.5;
    integrator.setSafety(expected);
    double actual = integrator.getSafety();
    assertEquals(expected, actual, 1e-10);
  }

  @Test
  public void testGetMinReduction() {
    double expected = 0.2;
    double actual = integrator.getMinReduction();
    assertEquals(expected, actual, 1e-10);
  }

  @Test
  public void testSetMinReduction() {
    double expected = 0.1;
    integrator.setMinReduction(expected);
    double actual = integrator.getMinReduction();
    assertEquals(expected, actual, 1e-10);
  }

  @Test
  public void testGetMaxGrowth() {
    double expected = 10.0;
    double actual = integrator.getMaxGrowth();
    assertEquals(expected, actual, 1e-10);
  }

  @Test
  public void testSetMaxGrowth() {
    double expected = 5.0;
    integrator.setMaxGrowth(expected);
    double actual = integrator.getMaxGrowth();
    assertEquals(expected, actual, 1e-10);
  }

  @Test
  public void testEstimateError() {
    double[][] yDotK = new double[][] {
      {1.0, 2.0, 3.0},
      {4.0, 5.0, 6.0},
      {7.0, 8.0, 9.0},
      {10.0, 11.0, 12.0}
    };
    double[] y0 = new double[] {1.0, 2.0, 3.0};
    double[] y1 = new double[] {4.0, 5.0, 6.0};
    double h = 0.1;
    double expected = 2.4;
    double actual = integrator.estimateError(yDotK, y0, y1, h);
    assertEquals(expected, actual, 1e-10);
  }
}