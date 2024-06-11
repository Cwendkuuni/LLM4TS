import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EmbeddedRungeKuttaIntegratorTest {

  private EmbeddedRungeKuttaIntegrator integrator;
  private FirstOrderDifferentialEquations equations;
  private double[] initialCondition;
  private double stepSize;
  private double startTime;
  private double endTime;
  private EventManager eventManager;

  @Before
  public void setUp() {
    this.integrator = new MyEmbeddedRungeKuttaIntegrator(
        "MyEmbeddedRungeKuttaMethod", true, new double[]{0.3, 0.4},
        new double[][]{{0.237, 0.0}, {0.678, 0.474}, {0.236, 0.0}},
        new double[]{0.237, 0.678}, new MyRungeKuttaStepInterpolator(), 0.01, 0.03,
        null, null);
    this.equations = TestUtils.createTestEquations();
    this.initialCondition = new double[]{1.0, 1.0};
    this.stepSize = 0.01;
    this.startTime = 0.0;
    this.endTime = 1.0;
    this.eventManager = new EventManager();
  }

  @Test
  public void testIntegrateBasic() {
    double result = integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    Assert.assertEquals(endTime, result, 1e-8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIntegrateNullEquations() {
    integrator.integrate(null, startTime, initialCondition, endTime, new double[initialCondition.length]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIntegrateNullInitialCondition() {
    integrator.integrate(equations, startTime, null, endTime, new double[initialCondition.length]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIntegrateNullY() {
    integrator.integrate(equations, startTime, initialCondition, endTime, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIntegrateNegativeStepSize() {
    integrator.setStepSize(-stepSize);
    try {
      integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
    } finally {
      integrator.setStepSize(stepSize);
    }
  }

  @Test
  public void testIntegrateWithEvents() {
    double[] y1 = new double[]{1.0, 1.1};
    double[] y2 = new double[]{2.0, 2.1};

    StepHandler stepHandler = new MyStepHandler();
    eventManager.addEvent(new When(1.5, new MyEvent(), stepHandler));
    integrator.addStepHandler(stepHandler);

    integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

    Assert.assertEquals(stepHandler.getCallCount(), 1);
    Assert.assertArrayEquals(y1, stepHandler.getY(), 1e-8);
    Assert.assertArrayEquals(y2, stepHandler.getYDot(), 1e-8);

    stepHandler.reset();

    integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

    Assert.assertEquals(stepHandler.getCallCount(), 2);
    Assert.assertArrayEquals(y1, stepHandler.getY(), 1e-8);
    Assert.assertArrayEquals(y2, stepHandler.getYDot(), 1e-8);
  }

  @Test
  public void testIntegrateWithEventsAndNonDefaultTolerances() {
    double[] y1 = new double[]{1.0, 1.1};
    double[] y2 = new double[]{2.0, 2.1};

    double absTol = 0.001;
    double relTol = 0.001;

    StepHandler stepHandler = new MyStepHandler();
    eventManager.addEvent(new When(1.5, new MyEvent(), stepHandler));
    integrator.addStepHandler(stepHandler);
    integrator.setAbsoluteTolerance(absTol);
    integrator.setRelativeTolerance(relTol);

    integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

    Assert.assertEquals(stepHandler.getCallCount(), 1);
    Assert.assertArrayEquals(y1, stepHandler.getY(), 1e-8 + absTol);
    Assert.assertArrayEquals(y2, stepHandler.getYDot(), 1e-8 + absTol);

    stepHandler.reset();

    integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);

    Assert.assertEquals(stepHandler.getCallCount(), 2);
    Assert.assertArrayEquals(y1, stepHandler.getY(), 1e-8 + absTol);
    Assert.assertArrayEquals(y2, stepHandler.getYDot(), 1e-8 + absTol);
  }

  @Test(expected = IllegalStateException.class)
  public void testIntegrateWithNullStepHandler() {
    integrator.addStepHandler(null);
    integrator.integrate(equations, startTime, initialCondition, endTime, new double[initialCondition.length]);
  }

  @Test
  public void testGetOrder() {
    int order = integrator.getOrder();
    Assert.assertEquals(3, order);
  }

  @Test
  public void testGetSafety() {
    double safety = integrator.getSafety();
    Assert.assertEquals(0.9, safety, 1e-8);
  }

  @Test
  public void testSetSafety() {
    integrator.setSafety(0.8);
    double safety = integrator.getSafety();
    Assert.assertEquals(0.8, safety, 1e-8);
  }

  @Test
  public void testGetMinReduction() {
    double minReduction = integrator.getMinReduction();
    Assert.assertEquals(0.2, minReduction, 1e-8);
  }

  @Test
  public void testSetMinReduction() {
    integrator.setMinReduction(0.3);
    double minReduction = integrator.getMinReduction();
    Assert.assertEquals(0.3, minReduction, 1e-8);
  }

  @Test
  public void testGetMaxGrowth() {
    double maxGrowth = integrator.getMaxGrowth();
    Assert.assertEquals(10.0, maxGrowth, 1e-8);
  }

  @Test
  public void testSetMaxGrowth() {
    integrator.setMaxGrowth(5.0);
    double maxGrowth = integrator.getMaxGrowth();
    Assert.assertEquals(5.0, maxGrowth, 1e-8);
  }

  static class MyEvent extends Event {
    @Override
    public boolean isRegularEvent() {
      return false;
    }

    @Override
    public double value(double t, double[] y) {
      return 0.0;
    }
  }

  static class MyStepHandler implements StepHandler {
    private int callCount = 0;
    private double[] y;
    private double[] yDot;

    @Override
    public int handleStep(double t, double[] y, double[] yDot) {
      this.y = y;
      this.yDot = yDot;
      callCount++;
      return Event.CONTINUE;
    }

    public int getCallCount() {
      return callCount;
    }

    public double[] getY() {
      return y;
    }

    public double[] getYDot() {
      return yDot;
    }

    public void reset() {
      callCount = 0;
    }
  }

  static class MyRungeKuttaStepInterpolator extends RungeKuttaStepInterpolator {
    @Override
    public double interpolate(double t, double[] y) {
      return 0.0;
    }

    @Override
    public void reinitialize(RungeKuttaIntegrator integrator, double[] yTmp, double[][] yDotK, boolean forward) {

    }
  }
}