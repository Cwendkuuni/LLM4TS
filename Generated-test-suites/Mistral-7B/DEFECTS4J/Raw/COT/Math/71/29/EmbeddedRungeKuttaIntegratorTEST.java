import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

  private EmbeddedRungeKuttaIntegrator integrator;

  @Before
  public void setUp() {
    integrator = EmbeddedRungeKuttaIntegratorTestUtil.createEmbeddedRungeKuttaIntegrator("RK23", false,
                                                                                        new double[]{0.25, 0.5},
                                                                                        new double[][]{{0.25, 0.5}, {0.5, 0.25}},
                                                                                        new double[]{0.25, 0.5},
                                                                                        new RungeKuttaStepInterpolator(),
                                                                                        0.001, 0.01,
                                                                                        new double[]{1e-9, 1e-9},
                                                                                        new double[]{1e-9, 1e-9});
  }

  @Test
  public void testIntegrateSimple() throws Exception {
    double[] y0 = {1.0, 0.0};
    double tEnd = 2.0;
    double[] yEnd = {2.0, 1.0};

    double t = integrator.integrate(new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return y0.length;
      }

      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
      }
    }, 0.0, y0, tEnd, new double[y0.length]);

    assertEquals(tEnd, t, 1e-10);
    assertEquals(yEnd[0], y[0], 1e-10);
    assertEquals(yEnd[1], y[1], 1e-10);
  }

  @Test(expected = IntegratorException.class)
  public void testIntegrateException() throws Exception {
    double[] y0 = {1.0, 0.0};
    double tEnd = 3.0;
    double[] yEnd = {2.0, 1.0};

    integrator.setMinStep(0.0);

    integrator.integrate(new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return y0.length;
      }

      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
      }
    }, 0.0, y0, tEnd, new double[y0.length]);

    assertThrows(IntegratorException.class, () -> integrator.integrate(
        new FirstOrderDifferentialEquations() {
          @Override
          public int getDimension() {
            return y0.length;
          }

          @Override
          public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0];
          }
        }, 0.0, y0, 0.0, new double[y0.length]));
  }

  @Test
  public void testIntegrateEvent() throws Exception {
    double[] y0 = {1.0, 0.0};
    double tEnd = 2.0;
    double[] yEnd = {2.0, 1.0};

    StepHandler stepHandler = new StepHandler() {
      @Override
      public void handleStep(StepHandlerContext context, int stepNumber) {
        assertEquals(context.getTime(), 1.0, 1e-10);
      }
    };

    integrator.addStepHandler(stepHandler);

    integrator.integrate(new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return y0.length;
      }

      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
      }
    }, 0.0, y0, tEnd, new double[y0.length]);

    integrator.removeStepHandler(stepHandler);

    Event event = new When(1.0);
    integrator.addEventHandler(event);

    double t = integrator.integrate(new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return y0.length;
      }

      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
      }
    }, 0.0, y0, tEnd, new double[y0.length]);

    assertEquals(tEnd, t, 1e-10);
    assertEquals(yEnd[0], y[0], 1e-10);
    assertEquals(yEnd[1], y[1], 1e-10);

    assertEquals(1, event.getIndex());
  }

}