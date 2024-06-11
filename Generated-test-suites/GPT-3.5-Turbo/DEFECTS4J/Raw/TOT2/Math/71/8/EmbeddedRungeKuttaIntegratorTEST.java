import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

  @Test
  public void testGetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    assertEquals(0.9, integrator.getSafety(), 0.0);
  }

  @Test
  public void testSetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    integrator.setSafety(0.8);
    assertEquals(0.8, integrator.getSafety(), 0.0);
  }

  @Test
  public void testGetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    assertEquals(0.2, integrator.getMinReduction(), 0.0);
  }

  @Test
  public void testSetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    integrator.setMinReduction(0.1);
    assertEquals(0.1, integrator.getMinReduction(), 0.0);
  }

  @Test
  public void testGetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
  }

  @Test
  public void testSetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    integrator.setMaxGrowth(12.0);
    assertEquals(12.0, integrator.getMaxGrowth(), 0.0);
  }

  private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
    public TestEmbeddedRungeKuttaIntegrator() {
      super("Test", false, new double[]{0.0}, new double[][]{{}}, new double[]{}, null, 0.01, 1.0, 0.0, 0.0);
    }

    @Override
    public int getOrder() {
      return 0;
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
      return 0.0;
    }

    @Override
    protected void computeDerivatives(double t, double[] y, double[] yDot) {
      // do nothing
    }
  }

}