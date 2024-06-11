import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {
  
  @Test
  public void testGetName() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    assertEquals("test", integrator.getName());
  }
  
  @Test
  public void testAddStepHandler() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    StepHandler handler = new StepHandlerImpl();
    integrator.addStepHandler(handler);
    assertTrue(integrator.getStepHandlers().contains(handler));
  }
  
  @Test
  public void testClearStepHandlers() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    StepHandler handler = new StepHandlerImpl();
    integrator.addStepHandler(handler);
    integrator.clearStepHandlers();
    assertTrue(integrator.getStepHandlers().isEmpty());
  }
  
  @Test
  public void testAddEventHandler() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    EventHandler handler = new EventHandlerImpl();
    integrator.addEventHandler(handler, 0.1, 0.01, 10);
    assertTrue(integrator.getEventHandlers().contains(handler));
  }
  
  @Test
  public void testClearEventHandlers() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    EventHandler handler = new EventHandlerImpl();
    integrator.addEventHandler(handler, 0.1, 0.01, 10);
    integrator.clearEventHandlers();
    assertTrue(integrator.getEventHandlers().isEmpty());
  }
  
  @Test
  public void testGetCurrentStepStart() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
  }
  
  @Test
  public void testGetCurrentSignedStepsize() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0.0);
  }
  
  @Test
  public void testSetMaxEvaluations() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    integrator.setMaxEvaluations(100);
    assertEquals(100, integrator.getMaxEvaluations());
  }
  
  @Test
  public void testGetMaxEvaluations() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    assertEquals(Integer.MAX_VALUE, integrator.getMaxEvaluations());
  }
  
  @Test
  public void testGetEvaluations() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    assertEquals(0, integrator.getEvaluations());
  }
  
  @Test
  public void testIntegrate() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    double[] y0 = {0.0, 0.0};
    double[] y = new double[y0.length];
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquationsImpl();
    double t = 1.0;
    try {
      integrator.integrate(equations, 0.0, y0, t, y);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testIntegrateThrowsDimensionMismatchException() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    double[] y0 = {0.0};
    double[] y = new double[y0.length];
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquationsImpl();
    double t = 1.0;
    try {
      integrator.integrate(equations, 0.0, y0, t, y);
      fail("DimensionMismatchException not thrown");
    } catch (DimensionMismatchException e) {
      // expected
    } catch (Exception e) {
      fail("Wrong exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testIntegrateThrowsNumberIsTooSmallException() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    double[] y0 = {0.0, 0.0};
    double[] y = new double[y0.length];
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquationsImpl();
    double t = 0.0;
    try {
      integrator.integrate(equations, 0.0, y0, t, y);
      fail("NumberIsTooSmallException not thrown");
    } catch (NumberIsTooSmallException e) {
      // expected
    } catch (Exception e) {
      fail("Wrong exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testComputeDerivatives() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    double t = 0.0;
    double[] y = {0.0, 0.0};
    double[] yDot = new double[y.length];
    try {
      integrator.computeDerivatives(t, y, yDot);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testSetStateInitialized() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    integrator.setStateInitialized(true);
    assertTrue(integrator.isStateInitialized());
  }
  
  @Test
  public void testAcceptStep() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    AbstractStepInterpolator interpolator = new AbstractStepInterpolatorImpl();
    double[] y = {0.0, 0.0};
    double[] yDot = new double[y.length];
    double tEnd = 1.0;
    try {
      double result = integrator.acceptStep(interpolator, y, yDot, tEnd);
      assertEquals(interpolator.getGlobalCurrentTime(), result, 0.0);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testSanityChecks() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl("test");
    ExpandableStatefulODE equations = new ExpandableStatefulODE();
    double t = 1.0;
    try {
      integrator.sanityChecks(equations, t);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  private class AbstractIntegratorImpl extends AbstractIntegrator {
    public AbstractIntegratorImpl(String name) {
      super(name);
    }
    
    public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
      // implementation here
    }
  }
  
  private class StepHandlerImpl implements StepHandler {
    public void init(double t0, double[] y0, double t) {
      // implementation here
    }
    
    public void handleStep(AbstractStepInterpolator interpolator, boolean isLast) {
      // implementation here
    }
  }
  
  private class EventHandlerImpl implements EventHandler {
    public void init(double t0, double[] y0, double t) {
      // implementation here
    }
    
    public double g(double t, double[] y) {
      // implementation here
    }
    
    public Action eventOccurred(double t, double[] y, boolean increasing) {
      // implementation here
    }
    
    public void resetState(double t, double[] y) {
      // implementation here
    }
  }
  
  private class FirstOrderDifferentialEquationsImpl implements FirstOrderDifferentialEquations {
    public int getDimension() {
      return 2;
    }
    
    public void computeDerivatives(double t, double[] y, double[] yDot) {
      // implementation here
    }
  }
  
  private class AbstractStepInterpolatorImpl extends AbstractStepInterpolator {
    public AbstractStepInterpolatorImpl() {
      super(true);
    }
    
    protected StepInterpolator doCopy() {
      return null;
    }
    
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
      // implementation here
    }
  }
  
  private class ExpandableStatefulODE implements FirstOrderDifferentialEquations {
    public int getDimension() {
      return 2;
    }
    
    public void computeDerivatives(double t, double[] y, double[] yDot) {
      // implementation here
    }
  }
}