package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.noise.RK4NoiseFunction;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for the EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

  

  /**
   * Test constructor with minimum arguments.
   */
  @Test
  public void testEmbeddedRungeKuttaIntegratorMinimumArguments() {
    String name = "testIntegrator";
    boolean fsal = true;
    double[] c = {0.3, 0.4, 0.5};
    double[][] a = {{0.0, 0.2, 0.3}, {0.1, 0.4, 0.5}, {0.2, 0.6, 0.7}};
    double[] b = {0.1, 0.2, 0.3};
    RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    double minStep = 0.01;
    double maxStep = 0.05;
    double scalAbsoluteTolerance = 1e-6;
    double scalRelativeTolerance = 1e-6;

    EmbeddedRungeKuttaIntegrator integrator =
        new EmbeddedRungeKuttaIntegrator(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

    assertNotNull(integrator);
    assertEquals(name, integrator.getName());
    assertEquals(fsal, integrator.isFSAL());
    assertArrayEquals(c, integrator.getC());
    assertArrayEquals(a, integrator.getA());
    assertArrayEquals(b, integrator.getB());
    assertNotNull(integrator.getPrototype());
    assertEquals(minStep, integrator.getMinStep(), 0.0);
    assertEquals(maxStep, integrator.getMaxStep(), 0.0);
    assertEquals(scalAbsoluteTolerance, integrator.getScalAbsoluteTolerance(), 0.0);
    assertEquals(scalRelativeTolerance, integrator.getScalRelativeTolerance(), 0.0);
  }

  /**
   * Test getOrder method.
   */
  @Test
  public void testGetOrder() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorTest_TestOrder();
    assertEquals(3, integrator.getOrder());
  }

  /**
   * Test getSafety method.
   */
  @Test
  public void testGetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorTest_TestSafety();
    assertEquals(0.9, integrator.getSafety(), 0.001);
  }

  /**
   * Test setSafety method.
   */
  @Test
  public void testSetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorTest_TestSafety();
    integrator.setSafety(0.5);
    assertEquals(0.5, integrator.getSafety(), 0.001);
  }

  /**
   * Test getMinReduction method.
   */
  @Test
  public void testGetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorTest_TestMinReduction();
    assertEquals(0.2, integrator.getMinReduction(), 0.001);
  }

  /**
   * Test setMinReduction method.
   */
  @Test
  public void testSetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorTest_TestMinReduction();
    integrator.setMinReduction(0.7);
    assertEquals(0.7, integrator.getMinReduction(), 0.001);
  }

  /**
   * Test getMaxGrowth method.
   */
  @Test
  public void testGetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorTest_TestMaxGrowth();
    assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
  }

  /**
   * Test setMaxGrowth method.
   */
  @Test
  public void testSetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegratorTest_TestMaxGrowth();
    integrator.setMaxGrowth(5.0);
    assertEquals(5.0, integrator.getMaxGrowth(), 0.001);
  }

  /**
   * Test integrate method with a simple function.
   */
  @Test
  public void testIntegrateSimpleFunction() throws Exception {
    String name = "testIntegrator";
    boolean fsal = true;
    double[] c = {0.3, 0.4, 0.5};
    double[][] a = {{0.0, 0.2, 0.3}, {0.1, 0.4, 0.5}, {0.2, 0.6, 0.7}};
    double[] b = {0.1, 0.2, 0.3};
    RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
    double minStep = 0.01;
    double maxStep = 0.05;
    double scalAbsoluteTolerance = 1e-6;
    double scalRelativeTolerance = 1e-6;

    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

    ExpandableStatefulODE equations = new RK4NoiseFunction(0.1, 1.0, 1.0);

    double t = 0.0;
    integrator.integrate(equations, t);
    double[] y = equations.getCompleteState();

    t = 1.0;
    integrator.integrate(equations, t);
    double[] y1 = equations.getCompleteState();

    assertEquals(1.105170833851236, y[0], 1e-6);
    assertEquals(1.211949733489239, y[1], 1e-6);

    assertEquals(1.111022302469135, y1[0], 1e-6);
    assertEquals(1.223521525155345, y1[1], 1e-6);
  }

  

}

class EmbeddedRungeKuttaIntegratorTest_TestOrder extends EmbeddedRungeKuttaIntegrator {
  EmbeddedRungeKuttaIntegratorTest_TestOrder() {
    super("testIntegrator", true, new double[]{0.3, 0.4, 0.5},
          new double[][]{{0.0, 0.2, 0.3}, {0.1, 0.4, 0.5}, {0.2, 0.6, 0.7}},
          new double[]{0.1, 0.2, 0.3}, new RungeKuttaStepInterpolator(),
          0.01, 0.05, null, null);
  }
}

class EmbeddedRungeKuttaIntegratorTest_TestSafety extends EmbeddedRungeKuttaIntegrator {
  EmbeddedRungeKuttaIntegratorTest_TestSafety() {
    super("testIntegrator", true, new double[]{0.3, 0.4, 0.5},
          new double[][]{{0.0, 0.2, 0.3}, {0.1, 0.4, 0.5}, {0.2, 0.6, 0.7}},
          new double[]{0.1, 0.2, 0.3}, new RungeKuttaStepInterpolator(),
          0.01, 0.05, null, null);
  }
}

class EmbeddedRungeKuttaIntegratorTest_TestMinReduction extends EmbeddedRungeKuttaIntegrator {
  EmbeddedRungeKuttaIntegratorTest_TestMinReduction() {
    super("testIntegrator", true, new double[]{0.3, 0.4, 0.5},
          new double[][]{{0.0, 0.2, 0.3}, {0.1, 0.4, 0.5}, {0.2, 0.6, 0.7}},
          new double[]{0.1, 0.2, 0.3}, new RungeKuttaStepInterpolator(),
          0.01, 0.05, null, null);
  }
}

class EmbeddedRungeKuttaIntegratorTest_TestMaxGrowth extends EmbeddedRungeKuttaIntegrator {
  EmbeddedRungeKuttaIntegratorTest_TestMaxGrowth() {
    super("testIntegrator", true, new double[]{0.3, 0.4, 0.5},
          new double[][]{{0.0, 0.2, 0.3}, {0.1, 0.4, 0.5}, {0.2, 0.6, 0.7}},
          new double[]{0.1, 0.2, 0.3}, new RungeKuttaStepInterpolator(),
          0.01, 0.05, null, null);
  }
}