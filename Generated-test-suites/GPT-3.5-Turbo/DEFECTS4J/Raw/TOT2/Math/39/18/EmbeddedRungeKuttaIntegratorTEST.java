import static org.junit.Assert.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

  // Method to test: getOrder()
  @Test
  public void testGetOrder() {
    EmbeddedRungeKuttaIntegrator integrator = new SomeEmbeddedRungeKuttaIntegrator();
    assertEquals(4, integrator.getOrder());
  }

  // Method to test: getSafety()
  @Test
  public void testGetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new SomeEmbeddedRungeKuttaIntegrator();
    assertEquals(0.9, integrator.getSafety(), 0.0);
  }

  // Method to test: setSafety()
  @Test
  public void testSetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new SomeEmbeddedRungeKuttaIntegrator();
    integrator.setSafety(0.8);
    assertEquals(0.8, integrator.getSafety(), 0.0);
  }

  // Method to test: getMinReduction()
  @Test
  public void testGetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new SomeEmbeddedRungeKuttaIntegrator();
    assertEquals(0.2, integrator.getMinReduction(), 0.0);
  }

  // Method to test: setMinReduction()
  @Test
  public void testSetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new SomeEmbeddedRungeKuttaIntegrator();
    integrator.setMinReduction(0.1);
    assertEquals(0.1, integrator.getMinReduction(), 0.0);
  }

  // Method to test: getMaxGrowth()
  @Test
  public void testGetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new SomeEmbeddedRungeKuttaIntegrator();
    assertEquals(10.0, integrator.getMaxGrowth(), 0.0);
  }

  // Method to test: setMaxGrowth()
  @Test
  public void testSetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new SomeEmbeddedRungeKuttaIntegrator();
    integrator.setMaxGrowth(15.0);
    assertEquals(15.0, integrator.getMaxGrowth(), 0.0);
  }

}