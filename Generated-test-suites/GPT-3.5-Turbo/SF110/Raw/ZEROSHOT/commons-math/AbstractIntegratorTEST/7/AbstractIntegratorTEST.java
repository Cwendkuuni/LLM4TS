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
    AbstractIntegrator integrator = new AbstractIntegratorImpl();
    StepHandler handler = new StepHandlerImpl();
    
    integrator.addStepHandler(handler);
    assertTrue(integrator.getStepHandlers().contains(handler));
  }
  
  @Test
  public void testClearStepHandlers() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl();
    StepHandler handler = new StepHandlerImpl();
    
    integrator.addStepHandler(handler);
    integrator.clearStepHandlers();
    assertEquals(0, integrator.getStepHandlers().size());
  }
  
  @Test
  public void testAddEventHandler() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl();
    EventHandler handler = new EventHandlerImpl();
    
    integrator.addEventHandler(handler, 1.0, 0.001, 100);
    assertTrue(integrator.getEventHandlers().contains(handler));
  }
  
  @Test
  public void testClearEventHandlers() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl();
    EventHandler handler = new EventHandlerImpl();
    
    integrator.addEventHandler(handler, 1.0, 0.001, 100);
    integrator.clearEventHandlers();
    assertEquals(0, integrator.getEventHandlers().size());
  }
  
  @Test
  public void testGetMaxEvaluations() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl();
    integrator.setMaxEvaluations(100);
    
    assertEquals(100, integrator.getMaxEvaluations());
  }
  
  @Test
  public void testGetEvaluations() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl();
    Incrementor evaluations = integrator.getEvaluationsCounter();
    evaluations.increment();
    
    assertEquals(1, integrator.getEvaluations());
  }
  
  @Test(expected = DimensionMismatchException.class)
  public void testIntegrateDimensionMismatchException() {
    AbstractIntegrator integrator = new AbstractIntegratorImpl();
    double[] y0 = {1.0, 2.0, 3.0};
    double[] y = new double[2];
    double[] yDot = new double[2];
    
    integrator.integrate(new FirstOrderDifferentialEquationsImpl(), 0.0, y0, 1.0, y);
  }
  
  // Add more test methods here
  
}