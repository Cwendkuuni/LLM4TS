import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

  @Test
  public void testGetOrder() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    assertEquals(4, integrator.getOrder());
  }

  @Test
  public void testGetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    integrator.setSafety(0.85);
    assertEquals(0.85, integrator.getSafety(), 0.0);
  }

  @Test
  public void testSetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    integrator.setSafety(0.75);
    assertEquals(0.75, integrator.getSafety(), 0.0);
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
    integrator.setMaxGrowth(15.0);
    assertEquals(15.0, integrator.getMaxGrowth(), 0.0);
  }

  private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
  
    public TestEmbeddedRungeKuttaIntegrator() {
        super("Test", false, new double[]{0.2}, new double[][]{{0.3}}, new double[]{0.4},
                new RungeKuttaStepInterpolator(), 0.1, 1.0, 1.0, 1.0);
    }

    @Override
    public int getOrder() {
        return 4;
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