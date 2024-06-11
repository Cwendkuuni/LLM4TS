package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.util.UnmodifiableArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

  private EmbeddedRungeKuttaIntegrator integrator;
  private FirstOrderDifferentialEquations equations;

  @Before
  public void setUp() {
    this.equations = new MyDifferentialEquations();
    this.integrator = new MyEmbeddedRungeKuttaIntegrator(
        "MyEmbeddedRungeKuttaIntegrator", false,
        new double[] {0.5, 0.3, 0.2, 0.1},
        new double[][] {{0.0, 0.5, 0.5, 0.875, 0.4375},
                         {0.0, 0, 0.375, 0.875, 0.4375},
                         {0.0, 0.125, 0.4375, 0.8, 0.5625},
                         {0.0, 0.1875, 0.625, 0.8, 0.6875},
                         {0.0, 0.25, 0.78125, 0.8, 0.78125}},
        new double[] {0.0, 0.2, 0.2, 0.2, 0.2},
        new RungeKuttaStepInterpolator(),
        0.001, 0.01, null, null);
  }

  @Test
  public void testIntegratorConstruction() {
    assertNotNull(integrator);
  }

  @Test
  public void testGetOrder() {
    int order = integrator.getOrder();
    assertEquals(5, order);
  }

  @Test
  public void testIntegrate() throws Exception {
    double[] y0 = {1.0, 1.0};
    double t0 = 0.0;
    double t = 1.0;
    double[] y = new double[y0.length];

    integrator.integrate(equations, t0, y0, t, y);

    double expected[] = {2.718281828, 2.718281828};
    assertArrayEquals(expected, y, 0.001);
  }

  @Test(expected = DerivativeException.class)
  public void testIntegrateWithDerivativeException() throws Exception {
    // Add a test case that produces a DerivativeException
    equations.addEvent(new DefaultValueEvent(1.0));
    double[] y0 = {1.0, 1.0};
    double t0 = 0.0;
    double t = 1.0;
    double[] y = new double[y0.length];

    integrator.integrate(equations, t0, y0, t, y);
  }

  @Test(expected = IntegratorException.class)
  public void testIntegrateWithIntegratorException() throws Exception {
    // Add a test case that produces an IntegratorException
    double[] y0 = {1.0, 1.0};
    double t0 = 0.0;
    double t = Double.POSITIVE_INFINITY;
    double[] y = new double[y0.length];

    integrator.integrate(equations, t0, y0, t, y);
  }

  @Test
  public void testGetters() {
    assertTrue(integrator.isFsal());

    double[] c = integrator.getC();
    assertArrayEquals(new double[] {0.5, 0.3, 0.2, 0.1}, c);

    double[][] a = integrator.getA();
    assertArrayEquals(
        new double[][] {{0.0, 0.5, 0.5, 0.875, 0.4375},
                         {0.0, 0.0, 0.375, 0.875, 0.4375},
                         {0.0, 0.125, 0.4375, 0.8, 0.5625},
                         {0.0, 0.1875, 0.625, 0.8, 0.6875},
                         {0.0, 0.25, 0.78125, 0.8, 0.78125}},
        a);

    double[] b = integrator.getB();
    assertArrayEquals(new double[] {0.0, 0.2, 0.2, 0.2, 0.2}, b);

    RungeKuttaStepInterpolator prototype = integrator.getPrototype();
    assertNotNull(prototype);

    double exp = integrator.getExp();
    assertEquals(-1.0 / 5.0, exp, 0.001);

    double safety = integrator.getSafety();
    assertEquals(0.9, safety, 0.001);

    double minReduction = integrator.getMinReduction();
    assertEquals(0.2, minReduction, 0.001);

    double maxGrowth = integrator.getMaxGrowth();
    assertEquals(10.0, maxGrowth, 0.001);
  }

  static class MyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
      yPrime[0] = y[1];
      yPrime[1] = -y[0];
    }

    @Override
    public int getDimension() {
      return 2;
    }

    @Override
    public void addEvent(Event event) {
      // Do nothing, no events will be added in this test
    }

    @Override
    public void removeEvent(Event event) {
      // Do nothing, no events will be removed in this test
    }

    @Override
    public UnmodifiableArrayList<Event> getEvents() {
      return new UnmodifiableArrayList<>();
    }

    @Override
    public void initialize(double[] yInitial) {
      // Do nothing, the initial condition is already set in setUp method
    }

    @Override
    public void reset() {
      // Do nothing, no need to reset in this test
    }

  }

  static class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

    public MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal,
                                         double[] c, double[][] a, double[] b,
                                         RungeKuttaStepInterpolator prototype,
                                         double minStep, double maxStep,
                                         double[] vecAbsoluteTolerance,
                                         double[] vecRelativeTolerance) {
      super(name, fsal, c, a, b, prototype, minStep, maxStep,
            vecAbsoluteTolerance, vecRelativeTolerance);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1,
                                   double h) {
      // Implement your custom error estimation logic here
    }

  }

}