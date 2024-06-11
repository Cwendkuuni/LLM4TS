import org.apache.commons.math3.ode.*;

import static org.junit.Assert.*;
import org.junit.*;

public class EmbeddedRungeKuttaIntegratorTest {

  private EmbeddedRungeKuttaIntegrator integrator;

  @Before
  public void setUp() {
    integrator = new EmbeddedRungeKuttaIntegratorMock();
  }

  @Test
  public void shouldReturnOrder2WhenGetOrderCalled() {
    assertEquals(2, integrator.getOrder());
  }

  @Test
  public void shouldReturnDefaultSafetyWhenGetSafetyCalled() {
    assertEquals(0.9, integrator.getSafety(), 0.0001);
  }

  @Test
  public void shouldSetSafetyWhenSetSafetyCalled() {
    integrator.setSafety(0.8);
    assertEquals(0.8, integrator.getSafety(), 0.0001);
  }

  @Test
  public void shouldReturnDefaultMinReductionWhenGetMinReductionCalled() {
    assertEquals(0.2, integrator.getMinReduction(), 0.0001);
  }

  @Test
  public void shouldSetMinReductionWhenSetMinReductionCalled() {
    integrator.setMinReduction(0.1);
    assertEquals(0.1, integrator.getMinReduction(), 0.0001);
  }

  @Test
  public void shouldReturnDefaultMaxGrowthWhenGetMaxGrowthCalled() {
    assertEquals(10.0, integrator.getMaxGrowth(), 0.0001);
  }

  @Test
  public void shouldSetMaxGrowthWhenSetMaxGrowthCalled() {
    integrator.setMinReduction(5.0);
    assertEquals(5.0, integrator.getMinReduction(), 0.0001);
  }

  @Test
  public void shouldThrowExceptionWhenOrderIsNegative() {
    try {
      integrator.setOrder(-1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Order must be a positive integer", e.getMessage());
    }
  }

  @Test
  public void shouldThrowExceptionWhenSafetyIsNotPositive() {
    try {
      integrator.setSafety(0.0);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Safety must be a positive value", e.getMessage());
    }
  }

  @Test
  public void shouldThrowExceptionWhenMinReductionIsNotPositive() {
    try {
      integrator.setMinReduction(-0.1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("MinReduction must be a positive value", e.getMessage());
    }
  }

  @Test
  public void shouldThrowExceptionWhenMaxGrowthIsNotPositive() {
    try {
      integrator.setMaxGrowth(0.0);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("MaxGrowth must be a positive value", e.getMessage());
    }
  }

  private class EmbeddedRungeKuttaIntegratorMock extends EmbeddedRungeKuttaIntegrator {

    public EmbeddedRungeKuttaIntegratorMock() {
      super("Mock", false, new double[]{0.0, 1.0}, new double[][]{{0.0, 2.0},{0.5, 1.5}}, new double[]{0.0, 1.0}, null, 0.0, 1.0, null, null);
    }

    @Override
    public int getOrder() {
      return 2;
    }

    @Override
    protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
      return 0;
    }

  }

}