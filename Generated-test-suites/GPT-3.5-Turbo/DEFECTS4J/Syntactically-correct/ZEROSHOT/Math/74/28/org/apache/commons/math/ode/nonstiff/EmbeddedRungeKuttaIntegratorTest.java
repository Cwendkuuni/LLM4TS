import org.junit.*;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

  @Test
  public void testGetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    double safety = integrator.getSafety();
    assertEquals(0.9, safety, 0.001);
  }

  @Test
  public void testSetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    integrator.setSafety(0.8);
    double safety = integrator.getSafety();
    assertEquals(0.8, safety, 0.001);
  }

  @Test
  public void testGetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    double minReduction = integrator.getMinReduction();
    assertEquals(0.2, minReduction, 0.001);
  }

  @Test
  public void testSetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    integrator.setMinReduction(0.1);
    double minReduction = integrator.getMinReduction();
    assertEquals(0.1, minReduction, 0.001);
  }

  @Test
  public void testGetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    double maxGrowth = integrator.getMaxGrowth();
    assertEquals(10.0, maxGrowth, 0.001);;
  }

  @Test
  public void testSetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator();
    integrator.setMaxGrowth(20.0);
    double maxGrowth = integrator.getMaxGrowth();
    assertEquals(20.0, maxGrowth, 0.001);;
  }

  private class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

    public double integrate(FirstOrderDifferentialEquations equations,
        double t0, double[] y0, double t, double[] y) {
      return 0.0;
    }

    public int getOrder() {
      return 0;
    }

    public double estimateError(double[][] yDotK,
        double[] y0, double[] y1,
        double h) {
      return 0.0;
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) {
      
    }

    public TestEmbeddedRungeKuttaIntegrator() {
      super("", false, null, null, null, null, 0.0, 0.0, 0.0, 0.0);
    }

  }
}