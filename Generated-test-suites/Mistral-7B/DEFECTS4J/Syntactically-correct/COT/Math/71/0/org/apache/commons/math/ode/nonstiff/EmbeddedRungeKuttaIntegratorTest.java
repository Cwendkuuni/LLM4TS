import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.ReloadableEventsManager;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

  @Test
  public void testConstructor() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    assertNotNull(integrator);
  }

  @Test
  public void testGetOrder() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    assertEquals(3, integrator.getOrder());
  }

  @Test
  public void testGetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    assertEquals(0.9, integrator.getSafety(), 1e-6);
  }

  @Test
  public void testSetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    integrator.setSafety(0.5);
    assertEquals(0.5, integrator.getSafety(), 1e-6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSafetyNegative() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    integrator.setSafety(-1);
  }

  @Test
  public void testGetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    assertEquals(0.2, integrator.getMinReduction(), 1e-6);
  }

  @Test
  public void testSetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    integrator.setMinReduction(0.5);
    assertEquals(0.5, integrator.getMinReduction(), 1e-6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetMinReductionNegative() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    integrator.setMinReduction(-1);
  }

  @Test
  public void testGetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    assertEquals(10.0, integrator.getMaxGrowth(), 1e-6);
  }

  @Test
  public void testSetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    integrator.setMaxGrowth(2);
    assertEquals(2, integrator.getMaxGrowth(), 1e-6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetMaxGrowthZero() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});
    integrator.setMaxGrowth(0);
  }

  @Test
  public void testIntegrate() throws EventException {
    FirstOrderDifferentialEquations equations = new MyDifferentialEquations();
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});

    double[] y0 = {0.0, 0.0};
    double t0 = 0.0;
    double t1 = 1.0;
    double[] y1 = {1.0, 1.0};

    integrator.integrate(equations, t0, y0, t1, y1);

    double[] expectedY1 = {1.0, 1.0};
    assertArrayEquals("Expected y1 to be: " + Arrays.toString(expectedY1),
        expectedY1, y1, 1e-6);
  }

  @Test(expected = DerivativeException.class)
  public void testIntegrateWithDerivativeException() throws EventException {
    FirstOrderDifferentialEquations equations = new MyDifferentialEquationsWithException();
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});

    double[] y0 = {0.0, 0.0};
    double t0 = 0.0;
    double t1 = 1.0;

    integrator.integrate(equations, t0, y0, t1, null);
  }

  @Test(expected = IntegratorException.class)
  public void testIntegrateWithIntegratorException() throws EventException {
    FirstOrderDifferentialEquations equations = new MyDifferentialEquationsWithException();
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("name", false,
        new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.5}, {0.25, 0.25}, {0.5, 0}},
        new double[]{0.0, 0.1, 0.3}, new RungeKuttaStepInterpolator(2), 0.1, 1.0,
        new double[]{1e-12, 1e-12}, new double[]{1e-12, 1e-12});

    double[] y0 = {0.0, 0.0};
    double t0 = 0.0;
    double t1 = 1.0;

    ReloadableEventsManager eventsManager = new ReloadableEventsManager();
    eventsManager.addEvent(new DefaultValueEvent(1.0));

    integrator.integrate(equations, t0, y0, t1, null, eventsManager);
  }

  private static class MyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
      yPrime[0] = y[1];
      yPrime[1] = y[0];
    }
  }

  private static class MyDifferentialEquationsWithException implements FirstOrderDifferentialEquations {
    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
      if (y[0] < 0) {
        throw new DerivativeException("y[0] should be non-negative");
      }
      yPrime[0] = y[1];
      yPrime[1] = y[0];
    }
  }

}