package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.Increment;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaMethods;
import org.junit.Test;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest {

  @Test
  public void testBasicFunctionality() {
    double[][] coefficients = {{0.0, 0.3, 0.6}, {0.0, 0.27, 0.45, 0.84}, {0.0, 0.1, 0.2, 0.2, 0.6}};

    final EmbeddedRungeKuttaIntegrator integrator =
        new EmbeddedRungeKuttaMethods.RK45(coefficients[0], new DummyStepInterpolator(), 0.001, 0.01);

    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return 1;
      }

      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = Math.sin(y[0]);
      }
    };

    double[] initialCondition = {0.0};
    double startTime = 0.0;
    double endTime = 1.0;

    try {
      double result = integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
      assertEquals(endTime, result, 1e-8);
    } catch (DerivativeException | IntegratorException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testEdgeCases() {
    // Add test cases for edge cases, such as:
    // - zero timestep
    // - singular values
    // - large stepsize
    // - small stepsize
    // - events handling
    // - different order integrators
    // - combine different integrators with different orders
  }

  /**
   * Dummy step handler for testing.
   */
  static class DummyStepHandler implements StepHandler {

    @Override
    public void handleStep(StepHandler handler, int step) {
      // No-op
    }

  }

  /**
   * Dummy step interpolator for testing.
   */
  static class DummyStepInterpolator implements AbstractStepInterpolator {

    private final double[] interpolant;
    private final double[] interpolantDerivative;
    private final boolean forward;

    DummyStepInterpolator(double[] interpolant, double[] interpolantDerivative, boolean forward) {
      this.interpolant = interpolant;
      this.interpolantDerivative = interpolantDerivative;
      this.forward = forward;
    }

    @Override
    public void reinitialize(RungeKuttaIntegrator integrator, double[] yTmp,
                             double[][] yDotK, boolean forward) {
      this.forward = forward;
    }

    @Override
    public void storeTime(double t) {
      // No-op
    }

    @Override
    public double interpolate(double t, boolean forward) {
      return interpolant[0];
    }

    @Override
    public double[] interpolateDerivatives(double t, boolean forward) {
      return new double[]{interpolantDerivative[0]};
    }

    @Override
    public void computeInterpolant(double t, double[] y, double[] yDot) {
      // No-op
    }

    @Override
    public void computeInterpolantDerivative(double t, double[] y, double[] yDot) {
      // No-op
    }

    @Override
    public void addEvent(Event event) {
      // No-op
    }

    @Override
    public boolean hasEvents() {
      return false;
    }

    @Override
    public boolean isIntegratorStepFinished(double h) {
      return true;
    }

  }

}