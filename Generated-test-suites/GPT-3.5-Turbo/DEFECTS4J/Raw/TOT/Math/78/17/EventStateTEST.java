import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.events.EventState;

public class EventStateTest {
  
  private EventState eventState;
  
  @Before
  public void setUp() {
    EventHandler handler = new EventHandler() {
      @Override
      public int eventOccurred(double t, double[] y, boolean increasing) {
        return EventHandler.CONTINUE;
      }
      
      @Override
      public double g(double t, double[] y) {
        return y[0] - 1.0;
      }
      
      @Override
      public void resetState(double t, double[] y) {
      
      }
    };
    eventState = new EventState(handler, 0.1, 0.001, 100);
  }
  
  @Test
  public void testGetEventHandler() {
    EventHandler handler = eventState.getEventHandler();
    Assert.assertNotNull(handler);
  }
  
  @Test
  public void testGetMaxCheckInterval() {
    double maxCheckInterval = eventState.getMaxCheckInterval();
    Assert.assertEquals(0.1, maxCheckInterval, 0.001);
  }
  
  @Test
  public void testGetConvergence() {
    double convergence = eventState.getConvergence();
    Assert.assertEquals(0.001, convergence, 0.00001);
  }
  
  @Test
  public void testGetMaxIterationCount() {
    int maxIterationCount = eventState.getMaxIterationCount();
    Assert.assertEquals(100, maxIterationCount);
  }
  
  @Test
  public void testReinitializeBegin() throws EventException {
    eventState.reinitializeBegin(0.0, new double[]{1.0});
    Assert.assertEquals(0.0, eventState.t0, 0.00001);
    Assert.assertEquals(0.0, eventState.g0, 0.00001);
  }
  
  @Test
  public void testEvaluateStep() throws Exception {
    eventState.reinitializeBegin(0.0, new double[]{1.0});
    eventState.forward = true;
    eventState.evaluateStep(new StepInterpolator() {
      @Override
      public void setInterpolatedTime(double time) {
      
      }
      
      @Override
      public void setInterpolator(int i) throws DerivativeException {
      
      }
      
      @Override
      public void rescale(double stepSize) throws DerivativeException {
      
      }
      
      @Override
      public boolean isForward() {
        return true;
      }
      
      @Override
      public double getMinTime() {
        return 0.0;
      }
      
      @Override
      public double getMaxTime() {
        return 1.0;
      }
      
      @Override
      public int getCurrentStateIndex() {
        return 0;
      }
      
      @Override
      public double getCurrentTime() {
        return 1.0;
      }
      
      @Override
      public void finalizeStep() throws DerivativeException {
      
      }
      
      @Override
      public double[] getInterpolatedState() throws DerivativeException {
        return new double[]{1.0};
      }
      
      @Override
      public double getCurrentStepStart() {
        return 0.0;
      }
    });
    
    Assert.assertTrue(eventState.pendingEvent);
    Assert.assertEquals(1.0, eventState.pendingEventTime, 0.00001);
  }
  
  @Test
  public void testGetEventTime() {
    eventState.pendingEventTime = 1.0;
    double eventTime = eventState.getEventTime();
    Assert.assertEquals(1.0, eventTime, 0.00001);
  }
  
  @Test
  public void testStepAccepted() throws EventException {
    eventState.pendingEvent = true;
    eventState.increasing = true;
    eventState.forward = true;
    eventState.stepAccepted(1.0, new double[]{1.0});
    Assert.assertEquals(1.0, eventState.t0, 0.00001);
    Assert.assertEquals(1.0, eventState.g0, 0.00001);
    Assert.assertFalse(eventState.pendingEvent);
    Assert.assertEquals(Double.NaN, eventState.pendingEventTime, 0.00001);
    Assert.assertEquals(EventHandler.CONTINUE, eventState.nextAction);
  }
  
  @Test
  public void testStop() {
    eventState.nextAction = EventHandler.STOP;
    boolean stop = eventState.stop();
    Assert.assertTrue(stop);
    
    eventState.nextAction = EventHandler.CONTINUE;
    stop = eventState.stop();
    Assert.assertFalse(stop);
  }
  
  @Test
  public void testReset() throws EventException {
    eventState.pendingEvent = true;
    eventState.nextAction = EventHandler.RESET_STATE;
    boolean resetState = eventState.reset(1.0, new double[]{1.0});
    Assert.assertTrue(resetState);
    
    eventState.nextAction = EventHandler.RESET_DERIVATIVES;
    resetState = eventState.reset(1.0, new double[]{1.0});
    Assert.assertTrue(resetState);
    
    eventState.nextAction = EventHandler.CONTINUE;
    resetState = eventState.reset(1.0, new double[]{1.0});
    Assert.assertFalse(resetState);
  }
  
}