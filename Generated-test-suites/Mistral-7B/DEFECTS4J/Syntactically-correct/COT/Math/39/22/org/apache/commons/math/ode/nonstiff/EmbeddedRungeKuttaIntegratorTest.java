package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

  /**
   * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
   */
  @org.junit.Test
  public void testEmbeddedRungeKuttaIntegratorConstruction() {
    final String name        = "testEmbeddedRungeKuttaIntegrator";
    final boolean fsal       = false;
    final double[] c         = { 0.0, 0.3, 0.6, 1.0 };
    final double[][] a       = { { 0.0 }, { 0.27, 0.0 }, { 0.4666666666666666, 0.27 }, { 0.8333333333333333, 0.6, 0.27 } };
    final double[] b         = { 0.0, 0.25, 0.4, 0.6 };
    final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    final double minStep      = 0.01;
    final double maxStep      = 0.1;
    final double[] vecAbsoluteTolerance = { 1e-12 };
    final double[] vecRelativeTolerance = { 1e-12 };

    EmbeddedRungeKuttaIntegrator integrator =
        new EmbeddedRungeKuttaIntegrator(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    assertNotNull(integrator);
    assertEquals(name, integrator.getName());
    assertEquals(fsal, integrator.isFSAL());
    assertEquals(c.length + 1, integrator.getStages());
    assertEquals(Integrator.ORDER_4, integrator.getOrder());
  }

  /**
   * Test method for {@link EmbeddedRungeKuttaIntegrator#getOrder()}.
   */
  @org.junit.Test
  public void testEmbeddedRungeKuttaIntegratorOrder() {
    final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, new double[0], new double[0][0], new double[0], null, 0.0, 0.0, null, null);
    assertEquals(Integrator.ORDER_4, integrator.getOrder());
  }

  /**
   * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}.
   */
  @org.junit.Test
  public void testEmbeddedRungeKuttaIntegratorIntegrate() {
    final String name          = "testEmbeddedRungeKuttaIntegrator";
    final boolean fsal         = false;
    final double[] c           = { 0.0, 0.3, 0.6, 1.0 };
    final double[][] a         = { { 0.0 }, { 0.27, 0.0 }, { 0.4666666666666666, 0.27 }, { 0.8333333333333333, 0.6, 0.27 } };
    final double[] b           = { 0.0, 0.25, 0.4, 0.6 };
    final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    final double minStep        = 0.01;
    final double maxStep        = 0.1;
    final double[] vecAbsoluteTolerance = { 1e-12 };
    final double[] vecRelativeTolerance = { 1e-12 };

    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);

    final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
      @Override
      public int getDimension() {
        return 1;
      }

      @Override
      public void initialize(double time, double[] state) {
        state[0] = 0.0;
      }

      @Override
      public void computeDerivatives(double time, double[] state, double[] derivatives) {
        derivatives[0] = 1.0;
      }

      @Override
      public double getTime() {
        return equations.getState()[0];
      }

      @Override
      public void setTime(double time) {
        equations.setState(new double[] { time });
      }

      @Override
      public void setCompleteState(double[] state) {
        equations.setState(state);
      }
    };

    integrator.integrate(equations, 1.0);
    assertEquals(1.1, equations.getState()[0], Precision.SAFE_DOUBLE_EPSILON);

    integrator.resetInternalState();

    integrator.integrate(equations, 0.5);
    assertEquals(0.55, equations.getState()[0], Precision.SAFE_DOUBLE_EPSILON);

    assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(equations, -1.0));
    assertThrows(MathIllegalStateException.class, () -> integrator.integrate(equations, 1.5));
  }

  /**
   * Test method for {@link EmbeddedRungeKuttaIntegrator#integrate(ExpandableStatefulODE, double)}.
   */
  @org.junit.Test
  public void testEmbeddedRungeKuttaIntegratorIntegrate2() {
    final String name          = "testEmbeddedRungeKuttaIntegrator";
    final boolean fsal         = true;
    final double[] c           = { 0.0, 0.3, 0.6, 1.0 };
    final double[][] a         = { { 0.0 }, { 0.27, 0.0 }, { 0.4666666666666666, 0.27 }, { 0.8333333333333333, 0.6, 0.27 } };
    final double[] b           = { 0.0, 0.25, 0.4, 0.6 };
    final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    final double minStep        = 0.01;
    final double maxStep        = 0.1;
    final double[] vecAbsoluteTolerance = { 1e-12 };
    final double[] vecRelativeTolerance = { 1e-12 };

    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);

    final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
      @Override
      public int getDimension() {
        return 1;
      }

      @Override
      public void initialize(double time, double[] state) {
        state[0] = 0.0;
      }

      @Override
      public void computeDerivatives(double time, double[] state, double[] derivatives) {
        derivatives[0] = 1.0;
      }

      @Override
      public double getTime() {
        return equations.getState()[0];
      }

      @Override
      public void setTime(double time) {
        equations.setState(new double[] { time });
      }

      @Override
      public void setCompleteState(double[] state) {
        equations.setState(state);
      }
    };

    integrator.integrate(equations, 1.0);
    assertEquals(1.1, equations.getState()[0], Precision.SAFE_DOUBLE_EPSILON);

    integrator.resetInternalState();

    integrator.integrate(equations, 0.5);
    assertEquals(0.55, equations.getState()[0], Precision.SAFE_DOUBLE_EPSILON);

    integrator.integrate(equations, 1.0);
    assertEquals(1.21, equations.getState()[0], Precision.SAFE_DOUBLE_EPSILON);

    integrator.resetInternalState();

    integrator.integrate(equations, 0.25);
    assertEquals(0.3125, equations.getState()[0], Precision.SAFE_DOUBLE_EPSILON);

    assertThrows(MathIllegalArgumentException.class, () -> integrator.integrate(equations, -1.0));
    assertThrows(MathIllegalStateException.class, () -> integrator.integrate(equations, 1.5));
  }

  /** Test constants. */
  private static final int Integrator_ORDER_4 = 4;

}