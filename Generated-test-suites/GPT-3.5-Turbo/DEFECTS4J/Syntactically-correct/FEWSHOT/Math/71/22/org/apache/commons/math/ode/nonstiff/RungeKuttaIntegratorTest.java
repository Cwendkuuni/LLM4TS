import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.junit.BeforeClass;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

  private static class DummyEquations implements FirstOrderDifferentialEquations {
    public int getDimension() {
      return 0;
    }
    public void computeDerivatives(double t, double[] y, double[] yDot) {
    }
  }

  private static class DummyInterpolator extends DummyStepInterpolator {
    public DummyInterpolator() {
      super(null, null, true);
    }
    @Override
    public RungeKuttaStepInterpolator copy() {
      return new DummyInterpolator();
    }
  }

  private static class DummyRungeKuttaIntegrator extends RungeKuttaIntegrator {
    protected DummyRungeKuttaIntegrator(final String name,
                                        final double[] c, final double[][] a, final double[] b,
                                        final double step) {
      super(name, c, a, b, new DummyInterpolator(), step);
    }
    public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0,
                            double t, double[] y) {
      return 0;
    }
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @Test
  public void testIntegrate() throws Exception {

    double[] c = { 0.0, 0.5, 0.5, 1.0 };
    double[][] a = { { 0.0, 0.0, 0.0, 0.0 },
                     { 0.5, 0.0, 0.0, 0.0 },
                     { 0.0, 0.5, 0.0, 0.0 },
                     { 0.0, 0.0, 1.0, 0.0 } };
    double[] b = { 1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0 };
    double step = 0.1;

    DummyRungeKuttaIntegrator integrator = new DummyRungeKuttaIntegrator("dummy", c, a, b, step);
    DummyEquations equations = new DummyEquations();
    double t0 = 0.0;
    double[] y0 = {};
    double t = 1.0;
    double[] y = {};

    assertEquals(0.0, integrator.integrate(equations, t0, y0, t, y), 1.0e-10);

  }

}