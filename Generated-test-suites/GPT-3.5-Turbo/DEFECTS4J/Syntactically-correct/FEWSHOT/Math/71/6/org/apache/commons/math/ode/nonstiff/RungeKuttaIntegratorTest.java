package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.junit.Test;

import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

  @Test
  public void testIntegrate() throws DerivativeException, IntegratorException {
    double[] c = {0.0, 0.2, 0.3, 0.6, 1.0};
    double[][] a = {
            {},
            {0.2},
            {3 / 40.0, 9 / 40.0},
            {0.3, -0.9, 1.2},
            {-11 / 54.0, 2.5, -70 / 27.0, 35 / 27.0}
    };
    double[] b = {16 / 135.0, 0, 6656 / 12825.0, 28561 / 56430.0, -9 / 50.0, 2 / 55.0};
    RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("Test Integrator", c, a, b, prototype, 0.01);

    double[] y = new double[2];
    y[0] = 1.0;
    y[1] = 2.0;
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
      @Override
      public int getDimension() {
        return 2;
      }

      @Override
      public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
        yDot[0] = y[1];
        yDot[1] = -2 * y[0];
      }
    };

    double t0 = 0.0;
    double[] y0 = {0.0, 0.0};

    double t = 1.0;
    double[] yExpected = {0.540302305868, -0.841470984812};
    double[] yActual = new double[2];

    integrator.integrate(equations, t0, y0, t, yActual);

    assertArrayEquals(yExpected, yActual, 1e-6);
  }
}