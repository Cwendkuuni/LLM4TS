package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.Impulse;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

  @Test
  public void testRungeKuttaIntegratorInstance() {
    RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", new double[]{}, new double[]{}, new double[]{});
    assertNotNull(integrator);
  }

  @Test(expected = MathIllegalArgumentException.class)
  public void testInvalidStepSize() {
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestIntegrator", new double[]{}, new double[]{}, new double[]{}, null, 0);
    integrator.integrate(mockDifferentialEquations(), 0.0, new double[0], 0.0, new double[0]);
  }

  @Test
  public void testIntegrationWithNoEvents() {
    RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", new double[]{}, new double[]{}, new double[]{});

    double[] y0 = {0.0, 1.0};
    double t0 = 0.0;
    double t = 2.0;
    double[] y = new double[y0.length];
    System.arraycopy(y0, 0, y, 0, y0.length);

    double result = integrator.integrate(mockDifferentialEquations(), t0, y0, t, y);

    assertEquals(t, result, 0.0);
  }

  @Test
  public void testIntegrationWithEvents() throws EventException {
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("TestIntegrator", new double[]{}, new double[]{}, new double[]{});
    CombinedEventsManager manager = new CombinedEventsManager();
    manager.addEvent(new Impulse(0.5, 1.0, 1.0));

    double[] y0 = {0.0, 1.0};
    double t0 = 0.0;
    double t = 1.5;
    double[] y = new double[y0.length];
    System.arraycopy(y0, 0, y, 0, y0.length);

    StepHandler stepHandler = new StepHandler() {
      @Override
      public void handleStep(StepHandler event, int last) {
        System.out.println("Event detected at time: " + event.getTime());
      }
    };

    integrator.setEventsManager(manager);
    integrator.addStepHandler(stepHandler);

    double result = integrator.integrate(mockDifferentialEquations(), t0, y0, t, y);

    assertEquals(1.5, result, 0.0);
    assertTrue(manager.size() > 0);
  }

  @Test
  public void testIntegrationWithStepHandlers() {
    RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", new double[]{}, new double[]{}, new double[]{});

    double[] y0 = {0.0, 1.0};
    double t0 = 0.0;
    double t = 1.0;
    double[] y = new double[y0.length];
    System.arraycopy(y0, 0, y, 0, y0.length);

    StepHandler stepHandler = new StepHandler() {
      @Override
      public void handleStep(StepHandler event, int last) {
        System.out.println("Step detected at time: " + event.getTime());
      }
    };

    integrator.addStepHandler(stepHandler);

    double result = integrator.integrate(mockDifferentialEquations(), t0, y0, t, y);

    assertEquals(1.0, result, 0.0);
    assertTrue(stepHandler.size() > 0);
  }

  @Test
  public void testIntegrationWithStepInterpolator() {
    RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", new double[]{}, new double[]{}, new double[]{});

    double[] y0 = {0.0, 1.0};
    double t0 = 0.0;
    double t = 1.0;
    double[] y = new double[y0.length];
    System.arraycopy(y0, 0, y, 0, y0.length);

    double result = integrator.integrate(mockDifferentialEquations(), t0, y0, t, y);

    assertEquals(1.0, result, 0.0);

    double[] yDot = new double[y0.length];
    integrator.getEnginedValues(t, y, yDot);

    assertEquals(1.0, yDot[0], 0.0);
    assertEquals(1.0, yDot[1], 0.0);
  }

  @Test
  public void testIntegrationWithEventsAndStepHandlers() throws EventException {
    RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", new double[]{}, new double[]{}, new double[]{});
    CombinedEventsManager manager = new CombinedEventsManager();
    manager.addEvent(new Impulse(0.5, 1.0, 1.0));

    double[] y0 = {0.0, 1.0};
    double t0 = 0.0;
    double t = 1.5;
    double[] y = new double[y0.length];
    System.arraycopy(y0, 0, y, 0, y0.length);

    StepHandler stepHandler = new StepHandler() {
      @Override
      public void handleStep(StepHandler event, int last) {
        System.out.println("Event detected at time: " + event.getTime());
      }
    };

    integrator.setEventsManager(manager);
    integrator.addStepHandler(stepHandler);

    double result = integrator.integrate(mockDifferentialEquations(), t0, y0, t, y);

    assertEquals(1.5, result, 0.0);
    assertTrue(manager.size() > 0);
    assertTrue(stepHandler.size() > 0);
  }

  @Test(expected = DerivativeException.class)
  public void testDerivativeExceptionThrown() {
    RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", new double[]{}, new double[]{}, new double[]{});
    FirstOrderDifferentialEquations equations = mockDifferentialEquationsThrowingException();

    double[] y0 = {0.0, 1.0};
    double t0 = 0.0;
    double[] y = new double[y0.length];
    System.arraycopy(y0, 0, y, 0, y0.length);

    integrator.integrate(equations, t0, y0, 0.0, y);
  }

  @Test(expected = IntegratorException.class)
  public void testIntegratorExceptionThrown() {
    RungeKuttaIntegrator integrator = new EulerIntegrator("Euler", new double[]{}, new double[]{}, new double[]{});
    FirstOrderDifferentialEquations equations = mockDifferentialEquationsThrowingIntegratorException();

    double[] y0 = {0.0, 1.0};
    double t0 = 0.0;
    double[] y = new double[y0.length];
    System.arraycopy(y0, 0, y, 0, y0.length);

    try {
      integrator.integrate(equations, t0, y0, 0.0, y);
    } catch (DerivativeException e) {
      throw new AssertionError("DerivativeException should not be thrown", e);
    }

    integrator.integrate(equations, t0, y0, 0.0, y);
  }

  private FirstOrderDifferentialEquations mockDifferentialEquations() {
    return new FirstOrderDifferentialEquations() {
      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = -y[0];
      }
    };
  }

  private FirstOrderDifferentialEquations mockDifferentialEquationsThrowingException() {
    return new FirstOrderDifferentialEquations() {
      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new DerivativeException("Exception while computing derivatives");
      }
    };
  }

  private FirstOrderDifferentialEquations mockDifferentialEquationsThrowingIntegratorException() {
    return new FirstOrderDifferentialEquations() {
      @Override
      public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new IntegratorException("Exception while integrating equations");
      }
    };
  }
}