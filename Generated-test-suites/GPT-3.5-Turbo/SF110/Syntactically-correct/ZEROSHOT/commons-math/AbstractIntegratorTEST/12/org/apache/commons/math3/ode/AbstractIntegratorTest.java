import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;

import org.junit.*;
import java.util.*;

public class AbstractIntegratorTest {

  @Test
  public void testGetName() {
    AbstractIntegrator integrator = new DummyIntegrator();
    String name = integrator.getName();
    Assert.assertEquals("DummyIntegrator", name);
  }

  @Test
  public void testAddStepHandler() {
    AbstractIntegrator integrator = new DummyIntegrator();
    StepHandler handler = new DummyStepHandler();
    integrator.addStepHandler(handler);
    Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
    Assert.assertTrue(stepHandlers.contains(handler));
  }

  @Test
  public void testClearStepHandlers() {
    AbstractIntegrator integrator = new DummyIntegrator();
    StepHandler handler = new DummyStepHandler();
    integrator.addStepHandler(handler);
    integrator.clearStepHandlers();
    Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
    Assert.assertTrue(stepHandlers.isEmpty());
  }

  @Test
  public void testAddEventHandler() {
    AbstractIntegrator integrator = new DummyIntegrator();
    EventHandler handler = new DummyEventHandler();
    double maxCheckInterval = 1.0;
    double convergence = 0.1;
    int maxIterationCount = 5;
    integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
    Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
    Assert.assertTrue(eventHandlers.contains(handler));
  }

  @Test
  public void testClearEventHandlers() {
    AbstractIntegrator integrator = new DummyIntegrator();
    EventHandler handler = new DummyEventHandler();
    double maxCheckInterval = 1.0;
    double convergence = 0.1;
    int maxIterationCount = 5;
    integrator.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount);
    integrator.clearEventHandlers();
    Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
    Assert.assertTrue(eventHandlers.isEmpty());
  }

  @Test
  public void testSetMaxEvaluations() {
    AbstractIntegrator integrator = new DummyIntegrator();
    int maxEvaluations = 1000;
    integrator.setMaxEvaluations(maxEvaluations);
    int actualMaxEvaluations = integrator.getMaxEvaluations();
    Assert.assertEquals(maxEvaluations, actualMaxEvaluations);
  }

  @Test
  public void testGetIntegrations() {
    AbstractIntegrator integrator = new DummyIntegrator();
    int evaluations = integrator.getEvaluations();
    Assert.assertEquals(0, evaluations);
  }

  @Test
  public void testInitIntegration() {
    AbstractIntegrator integrator = new DummyIntegrator();
    double t0 = 0.0;
    double[] y0 = {1.0, 2.0, 3.0};
    double t = 1.0;
    integrator.initIntegration(t0, y0, t);
    int evaluations = integrator.getEvaluations();
    Assert.assertEquals(0, evaluations);
  }

  @Test
  public void testSetEquations() {
    AbstractIntegrator integrator = new DummyIntegrator();
    ExpandableStatefulODE equations = new ExpandableStatefulODE(new DummyEquations());
    integrator.setEquations(equations);
    ExpandableStatefulODE actualEquations = integrator.getExpandable();
    Assert.assertEquals(equations, actualEquations);
  }

  @Test
  public void testIntegrate() throws Exception {
    AbstractIntegrator integrator = new DummyIntegrator();
    FirstOrderDifferentialEquations equations = new DummyEquations();
    double t0 = 0.0;
    double[] y0 = {0.0};
    double t = 1.0;
    double[] y = new double[1];
    double endTime = integrator.integrate(equations, t0, y0, t, y);
    Assert.assertTrue(endTime > t0);
    Assert.assertTrue(endTime <= t);
    Assert.assertEquals(1, y.length);
    Assert.assertEquals(1, integrator.getEvaluations());
  }

  @Test
  public void testComputeDerivatives() throws Exception {
    AbstractIntegrator integrator = new DummyIntegrator();
    double t = 0.0;
    double[] y = {0.0};
    double[] yDot = new double[1];
    integrator.computeDerivatives(t, y, yDot);
    Assert.assertEquals(1, integrator.getEvaluations());
  }

  class DummyIntegrator extends AbstractIntegrator {
    public void  integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {}
  }

  class DummyStepHandler implements StepHandler {
    public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {}
    public void init(double t0, double[] y0, double t) {}
  }

  class DummyEventHandler implements EventHandler {
    public void init(double t0, double[] y0, double t) throws DimensionMismatchException {}
    public Action eventOccurred(double t, double[] y, boolean increasing) throws DimensionMismatchException { return EventHandler.Action.CONTINUE; }
    public double g(double t, double[] y) throws DimensionMismatchException { return 1.0; }
    public void resetState(double t, double[] y) throws DimensionMismatchException {}
  }

  class DummyEquations implements FirstOrderDifferentialEquations {
    public int getDimension() { return 1; }
    public void computeDerivatives(double t, double[] y, double[] yDot) {
      yDot[0] = t;
    }
  }
}