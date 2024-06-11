package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

  private EmbeddedRungeKuttaIntegrator integrator;
  private ExpandableStatefulODE equations;

  @Before
  public void setUp() {
    double[] coefficients = {0, 1.0/4.0, 1.0/3.0, 1.0/2.0};
    double[] internalWeights = {{1.0/6.0, 1.0/3.0}, {1.0/2.0, 1.0/2.0}, {0.0, 1.0}};
    double[] externalWeights = {1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0};
    RungeKuttaStepInterpolator prototype = Mockito.mock(RungeKuttaStepInterpolator.class);
    integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta 4", true, coefficients, internalWeights, externalWeights, prototype, 0.01, 0.05, null, null) {
      @Override
      public int getOrder() {
        return 4;
      }

      @Override
      protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double error = 0;
        for (int i = 0; i < y1.length; i++) {
          error += Precision.safeSum(Precision.safeAbs(yDotK[0][i] - yDotK[1][i]), Precision.safeAbs(y1[i] - y0[i]));
        }
        return error;
      }
    };
    equations = Mockito.mock(ExpandableStatefulODE.class);
  }

  @Test
  public void testIntegrateForward() throws MathIllegalArgumentException, MathIllegalStateException {
    when(equations.getCompleteState()).thenReturn(new double[] {0.1, 0.1});
    double initialTime = 0.0;
    double finalTime = 0.4;
    integrator.integrate(equations, initialTime);
    verify(equations).setTime(eq(initialTime + integrator.getStepSize()));
    verify(equations).setCompleteState(anyArray());

    integrator.integrate(equations, finalTime);
    verify(equations).setTime(eq(finalTime));
    verify(equations).setCompleteState(anyArray());
  }

  @Test(expected = MathIllegalArgumentException.class)
  public void testIntegrateBackwardWithNegativeInitialStep() throws MathIllegalArgumentException, MathIllegalStateException {
    when(equations.getCompleteState()).thenReturn(new double[] {0.1, 0.1});
    double initialTime = 0.4;
    double finalTime = 0.0;
    integrator.integrate(equations, initialTime);
  }

  @Test(expected = MathIllegalStateException.class)
  public void testIntegrateBackwardWithNonSuccessfulStep() throws MathIllegalArgumentException, MathIllegalStateException {
    when(equations.getCompleteState()).thenReturn(new double[] {0.1, 0.1});
    double initialTime = 0.0;
    double finalTime = 0.2;
    integrator.integratorState = IntegratorState.REJECTED;
    integrator.integrate(equations, initialTime);
  }

  @Test
  public void testIntegrateBackward() throws MathIllegalArgumentException, MathIllegalStateException {
    when(equations.getCompleteState()).thenReturn(new double[] {0.1, 0.1});
    double initialTime = 0.4;
    double finalTime = 0.0;
    integrator.integrate(equations, initialTime);
    verify(equations).setTime(eq(initialTime));
    verify(equations).setCompleteState(anyArray());

    integrator.integrate(equations, finalTime);
    verify(equations).setTime(eq(finalTime));
    verify(equations).setCompleteState(anyArray());
  }

  @Test
  public void testStepSizeControl() throws MathIllegalArgumentException, MathIllegalStateException {
    when(equations.getCompleteState()).thenReturn(new double[] {0.1, 0.1});
    double initialTime = 0.0;
    double finalTime = 0.4;

    integrator.integrate(equations, initialTime);

    double currentStepSize = integrator.getStepSize();
    assertEquals(0.05, currentStepSize, 1e-7);

    integrator.integrate(equations, finalTime);

    currentStepSize = integrator.getStepSize();
    assertEquals(0.01, currentStepSize, 1e-7);
  }
}