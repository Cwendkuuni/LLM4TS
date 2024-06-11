package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.SingularEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for EmbeddedRungeKuttaIntegrator.
 */
public class EmbeddedRungeKuttaIntegratorTest {

  private static final double DELTA = 1e-10;
  private static final double EPSILON = 1e-13;

  private EmbeddedRungeKuttaIntegrator integrator;
  private FirstOrderDifferentialEquations equations;
  private double[] initialValues;

  @Before
  public void setUp() {
    integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
        new double[] {0.25, 0.25, 0.5, 0.25},
        new double[][] {{0, 0.25, 0.25, 0.5, 0.25}, {0.25, 0.25, 0.5, 0.25, 0.25}},
        new double[] {0.25, 0.25},
        new RungeKuttaStepInterpolator(), 0.001, 10.0,
        null, null) {
      @Override
      public int getOrder() {
        return 4;
      }

      @Override
      public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double error = 0;
        for (int i = 0; i < y0.length; ++i) {
          error += Math.pow(Math.abs(yDotK[yDotK.length - 1][i] - y1[i]), 2);
        }
        return Math.sqrt(error);
      }
    };

    equations = (firstOrderDifferentialEquations) (args -> new double[] {args[0] * 2.0});
    initialValues = new double[] {1.0};
  }

  @Test
  public void testIntegratorBasicFunctionality() {
    double tStart = 0.0;
    double tEnd = 2.0;

    integrator.initialize(equations, tStart, initialValues, tEnd);
    double[] result = integrator.integrate(tStart, initialValues);

    Assert.assertArrayEquals(new double[] {2.0}, result, EPSILON);
  }

  @Test(expected = MathIllegalArgumentException.class)
  public void testIntegratorThrowsExceptionWithNegativeStepSize() {
    integrator.setMaxStep(-1.0);
    integrator.initialize(equations, 0.0, initialValues, 1.0);
    integrator.integrate(0.0, initialValues);
  }

  @Test(expected = MathIllegalArgumentException.class)
  public void testIntegratorThrowsExceptionWithNegativeMinStep() {
    integrator.setMinStep(-1.0);
    integrator.initialize(equations, 0.0, initialValues, 1.0);
    integrator.integrate(0.0, initialValues);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIntegratorThrowsExceptionWithNullEventsHandlersManager() {
    integrator.setEventsHandlersManager(null);

    integrator.initialize(equations, 0.0, initialValues, 1.0);
    integrator.integrate(0.0, initialValues);
  }

  @Test
  public void testIntegratorWithSingularEvent() {
    double[] initialValues = new double[] {0.0, 1.0};
    integrator.setEventsHandlersManager(new CombinedEventsManager());
    integrator.addEndTimeChecker(1.0);
    integrator.addEvent(1.0, new SingularEvent() {
      @Override
      public boolean occurs(double t, double[] y) {
        return Math.abs(y[1]) < 1e-5;
      }

      @Override
      public void evaluateEvent(double t, double[] y, double[] values) {
        values[0] = Math.log(Math.abs(y[1]));
      }
    });

    integrator.initialize(equations, 0.0, initialValues, 1.0);
    double[] result = integrator.integrate(0.0, initialValues);

    Assert.assertEquals(Math.log(1.0), result[0], EPSILON);
  }

  @Test
  public void testIntegratorWithValueEvent() {
    integrator.setEventsHandlersManager(new CombinedEventsManager());
    integrator.addEndTimeChecker(2.0);
    integrator.addEvent(1.0, new DefaultValueEvent() {
      @Override
      public double value(double t, double[] y) {
        return 1.0;
      }
    });

    integrator.initialize(equations, 0.0, initialValues, 2.0);
    double[] result = integrator.integrate(0.0, initialValues);

    Assert.assertArrayEquals(new double[] {1.0, 2.0}, result, DELTA);
  }

  @Test
  public void testIntegratorWithStepHandler() {
    integrator.setEventsHandlersManager(new CombinedEventsManager());
    integrator.addStepHandler(new StepHandler() {
      @Override
      public void handleStep(StepHandler event, int stepNumber) {
        System.out.println("Step number = " + stepNumber);
      }
    });

    integrator.initialize(equations, 0.0, initialValues, 1.0);
    integrator.integrate(0.0, initialValues);
  }
}