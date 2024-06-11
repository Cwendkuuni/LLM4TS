package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegratorBuilder;
import org.apache.commons.math.ode.nonstiff.RungeKuttaStepInterpolator;

/**
 * Test the EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

  private static final double EPSILON = 1e-9;

  private EmbeddedRungeKuttaIntegrator integrator;
  private FirstOrderDifferentialEquations equations;
  private StepInterpolator interpolator;
  private StepHandler stepHandler;

  @Before
  public void setUp() {
    equations = new FirstOrderDifferentialEquations() {
      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
      }
    };

    integrator = new EmbeddedRungeKuttaIntegrator(
        "MyEmbeddedRungeKuttaIntegrator",
        true,
        new double[] { 0.3, 0.4, 0.1, 0.2 },
        new double[][] { { 0.0, 0.5, 0.5, 0.5 }, { 0.25, 0.0, 0.5, 0.5 } },
        new double[] { 0.5, 0.5 },
        new RungeKuttaStepInterpolator() {
          @Override
          public double interpolate(double t, double[] y) {
            return 0.0;
          }
        },
        0.01, 10.0, null, null) {
      @Override
      public int getOrder() {
        return 4;
      }
    };

    interpolator = integrator.getPrototype().copy();
    stepHandler = new TestStepHandler();
  }

  @Test
  public void testIntegrate() throws Exception {
    double[] y0 = new double[] { 1.0, 0.0 };
    double t0 = 0.0;
    double t = Math.PI;
    double[] y = new double[y0.length];

    integrator.integrate(equations, t0, y0, t, y);

    assertEquals(Math.sin(t), y[0], EPSILON);
    assertEquals(Math.cos(t), y[1], EPSILON);
  }

  @Test
  public void testIntegrateWithEvents() throws Exception {
    double[] y0 = new double[] { 1.0, 0.0 };
    double t0 = 0.0;
    double t1 = Math.PI / 2.0;
    double t2 = Math.PI;

    integrator.addStepHandler(stepHandler);

    integrator.integrate(equations, t0, y0, t2, null);

    assertFalse(stepHandler.isHandled());

    integrator = new EmbeddedRungeKuttaIntegratorBuilder(
        integrator)
        .withMinStep(0.01)
        .withMaxStep(0.1)
        .withStepHandlers(stepHandler)
        .build();

    integrator.integrate(equations, t0, y0, t1, null);

    assertTrue(stepHandler.isHandled());
  }

  @Test(expected = DerivativeException.class)
  public void testIntegrateWithDerivativeException() throws Exception {
    double[] y0 = new double[] { 1.0, 0.0 };
    double t0 = 0.0;
    double t = Math.PI;

    equations = new FirstOrderDifferentialEquations() {
      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new DerivativeException("DerivativeException test");
      }
    };

    integrator.integrate(equations, t0, y0, t, null);
  }

  @Test(expected = IntegratorException.class)
  public void testIntegrateWithIntegratorException() throws Exception {
    double[] y0 = new double[] { 1.0, 0.0 };
    double t0 = 0.0;
    double t = Math.PI;

    integrator = new EmbeddedRungeKuttaIntegrator(
        "MyEmbeddedRungeKuttaIntegrator",
        true,
        new double[] { 0.3, 0.4, 0.1, 0.2 },
        new double[][] { { 0.0, 0.5, 0.5, 0.5 }, { 0.25, 0.0, 0.5, 0.5 } },
        new double[] { 0.5, 0.5 },
        new RungeKuttaStepInterpolator() {
          @Override
          public double interpolate(double t, double[] y) {
            throw new IntegratorException("IntegratorException test");
          }
        },
        0.01, 10.0, null, null) {
      @Override
      public int getOrder() {
        return 4;
      }

      @Override
      protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        throw new IntegratorException("IntegratorException test");
      }
    };

    integrator.integrate(equations, t0, y0, t, null);
  }

  @Test
  public void testGetOrder() {
    assertEquals(4, integrator.getOrder());
  }

  @Test
  public void testGetSafety() {
    assertEquals(0.9, integrator.getSafety(), EPSILON);
  }

  @Test
  public void testSetSafety() {
    integrator.setSafety(0.5);
    assertEquals(0.5, integrator.getSafety(), EPSILON);
  }

  @Test
  public void testGetMinReduction() {
    assertEquals(0.2, integrator.getMinReduction(), EPSILON);
  }

  @Test
  public void testSetMinReduction() {
    integrator.setMinReduction(0.3);
    assertEquals(0.3, integrator.getMinReduction(), EPSILON);
  }

  @Test
  public void testGetMaxGrowth() {
    assertEquals(10.0, integrator.getMaxGrowth(), EPSILON);
  }

  @Test
  public void testSetMaxGrowth() {
    integrator.setMaxGrowth(5.0);
    assertEquals(5.0, integrator.getMaxGrowth(), EPSILON);
  }

  @Test
  public void testIntegratorPrototype() {
    StepInterpolator prototypeInterpolator = integrator.getPrototype();
    assertNotNull(prototypeInterpolator);
  }

  private static class TestStepHandler implements StepHandler {

    private boolean handled = false;

    @Override
    public void handleStep(StepInterpolator interpolator, int stepNumber) {
      handled = true;
    }

    public boolean isHandled() {
      return handled;
    }

  }

}