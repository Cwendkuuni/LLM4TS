import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.SimpleEvent;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.stepper.StepHandler;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

  @Test
  public void testEmbeddedRungeKuttaIntegrator_constructor() {
    // Test the default constructor
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[4],
        new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.25}, {0.25, 0}, {0, 0.25, 0.25, 0.25}},
        new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator());

    // Test the constructor with custom parameters
    EmbeddedRungeKuttaIntegrator integrator2 = new EmbeddedRungeKuttaIntegrator("RKF45", true, new double[5],
        new double[][]{{0.0, 0.0, 0.2, 0.2, 0.5}, {0.2, 0.2, 0.2, 0.2, 0.2}, {0.2, 0, 0.2, 0.2, 0.2},
            {0, 0.2, 0.2, 0.2, 0.2}, {0, 0, 0.2, 0.2, 0.2}}, new double[]{0.2, 0.2, 0.2, 0.2, 0.2},
        new RungeKuttaStepInterpolator(), 1.0e-12, 1.0e-12, new double[]{1.0e-12, 1.0e-12});
  }

  @Test
  public void testGetOrder() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[4],
        new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.25}, {0.25, 0, 0.25, 0.25}, {0, 0.25, 0.25, 0.25}},
        new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator());

    assertEquals(4, integrator.getOrder());
  }

  @Test
  public void testGetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[4],
        new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.25}, {0.25, 0, 0.25, 0.25}, {0, 0.25, 0.25, 0.25}},
        new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator());

    assertEquals(0.9, integrator.getSafety(), 1e-4);
  }

  @Test
  public void testSetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[4],
        new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.25}, {0.25, 0, 0.25, 0.25}, {0, 0.25, 0.25, 0.25}},
        new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator());

    integrator.setSafety(0.8);
    assertEquals(0.8, integrator.getSafety(), 1e-4);
  }

  @Test
  public void testIntegrate_simple() {
    double[] y0 = {1.0};
    double  t0 = 0.0;
    double  t  = 1.0;
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return y0.length;
      }

      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[0] * 0.1;
      }
    };

    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[4],
        new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.25}, {0.25, 0, 0.25, 0.25}, {0, 0.25, 0.25, 0.25}},
        new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator());

    double result = integrator.integrate(equations, t0, y0, t);

    assertEquals(t, result, 1e-4);
  }

  @Test
  public void testIntegrate_event() {
    double[] y0 = {1.0};
    double  t0 = 0.0;
    double  t1 = 1.0;
    double  t2 = 2.0;
    double[] y1 = {2.0};

    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return y0.length;
      }

      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[0] * 0.1;
      }

      @Override
      public Event[] getEvents(double t, double[] y) {
        if (t >= 1.0 && t <= 1.1) {
          return new Event[] {new SimpleEvent(t, new DefaultValueEvent(y[0], 2.0))};
        }
        return new Event[0];
      }
    };

    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[4],
        new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.25}, {0.25, 0, 0.25, 0.25}, {0, 0.25, 0.25, 0.25}},
        new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator());

    StepHandler stepHandler = new StepHandler() {
      @Override
      public void handleStep(RungeKuttaStepInterpolator interpolator, int stepNumber) {
        double[] y = interpolator.getY();
        System.out.println("Step number: " + stepNumber + " - y[" + y.length + "] = " + java.util.Arrays.toString(y));
      }
    };

    integrator.addStepHandler(stepHandler);

    double result = integrator.integrate(equations, t0, y0, t2, new StepHandler[] {new StepHandler() {
      @Override
      public void handleStep(RungeKuttaStepInterpolator interpolator, int stepNumber) {
        if (stepNumber == 1) {
          throw new IllegalStateException("Test exception");
        }
      }
    }});

    assertEquals(t2, result, 1e-4);
  }

  @Test(expected = DeriveException.class)
  public void testIntegrate_derivativeException() {
    double[] y0 = {1.0};
    double  t0 = 0.0;
    double  t  = 1.0;
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return y0.length;
      }

      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new DerivativeException("Derivative exception");
      }
    };

    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[4],
        new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.25}, {0.25, 0, 0.25, 0.25}, {0, 0.25, 0.25, 0.25}},
        new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator());

    integrator.integrate(equations, t0, y0, t);
  }

  @Test(expected = IntegratorException.class)
  public void testIntegrate_integratorException() {
    double[] y0 = {1.0};
    double  t0 = 0.0;
    double  t  = 1.0;
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return y0.length;
      }

      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new IntegratorException("Integrator exception");
      }
    };

    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[4],
        new double[][]{{0.0, 0.0, 0.5, 0.5}, {0.25, 0.25, 0.25, 0.25}, {0.25, 0, 0.25, 0.25}, {0, 0.25, 0.25, 0.25}},
        new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator());

    integrator.integrate(equations, t0, y0, t);
  }
}