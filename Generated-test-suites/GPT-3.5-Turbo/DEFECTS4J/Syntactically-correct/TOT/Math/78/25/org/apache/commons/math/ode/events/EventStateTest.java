import static org.junit.Assert.*;
import org.junit.Test;

public class EventStateTest {
  
  // Test case for reinitializeBegin
  @Test
  public void testReinitializeBegin() {
    // dummy values
    double tStart = 0.0;
    double[] yStart = {1.0, 2.0, 3.0};
    
    try {
      EventState eventState = new EventState(new EventHandler(), 0.01, 0.001, 100);
      eventState.reinitializeBegin(tStart, yStart);
      assertEquals(tStart, eventState.t0, 0.0001);
      assertEquals(0.0, eventState.g0, 0.0001);
      assertEquals(true, eventState.g0Positive);
      assertEquals(false, eventState.pendingEvent);
      assertEquals(Double.NaN, eventState.pendingEventTime, 0.0001);
      assertEquals(Double.NaN, eventState.previousEventTime, 0.0001);
      assertEquals(true, eventState.forward);
      assertEquals(true, eventState.increasing);
      assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    } catch (EventException e) {
      fail("EventException was thrown");
    }
  }
  
  // Test case for evaluateStep
  @Test
  public void testEvaluateStep() {
    try {
      EventState eventState = new EventState(new EventHandler(), 0.01, 0.001, 100);
      StepInterpolator interpolator = new StepInterpolator();
      boolean result = eventState.evaluateStep(interpolator);
      assertEquals(false, result);
    } catch (DerivativeException e) {
      fail("DerivativeException was thrown");
    } catch (EventException e) {
      fail("EventException was thrown");
    } catch (ConvergenceException e) {
      fail("ConvergenceException was thrown");
    }
  }
  
  // Test case for getEventTime
  @Test
  public void testGetEventTime() {
    try {
      EventState eventState = new EventState(new EventHandler(), 0.01, 0.001, 100);
      double eventTime = eventState.getEventTime();
      assertEquals(Double.NaN, eventTime, 0.0001);
    } catch (EventException e) {
      fail("EventException was thrown");
    }
  }
  
  // Test case for stepAccepted
  @Test
  public void testStepAccepted() {
    try {
      EventState eventState = new EventState(new EventHandler(), 0.01, 0.001, 100);
      // dummy values
      double t = 0.0;
      double[] y = {1.0, 2.0, 3.0};
      eventState.stepAccepted(t, y);
      assertEquals(t, eventState.t0, 0.0001);
      assertEquals(0.0, eventState.g0, 0.0001);
      assertEquals(true, eventState.g0Positive);
      assertEquals(false, eventState.pendingEvent);
      assertEquals(Double.NaN, eventState.pendingEventTime, 0.0001);
      assertEquals(t, eventState.previousEventTime, 0.0001);
      assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    } catch (EventException e) {
      fail("EventException was thrown");
    }
  }
  
  // Test case for stop
  @Test
  public void testStop() {
    try {
      EventState eventState = new EventState(new EventHandler(), 0.01, 0.001, 100);
      boolean result = eventState.stop();
      assertEquals(false, result);
    } catch (EventException e) {
      fail("EventException was thrown");
    }
  }
  
  // Test case for reset
  @Test
  public void testReset() {
    try {
      EventState eventState = new EventState(new EventHandler(), 0.01, 0.001, 100);
      // dummy values
      double t = 0.0;
      double[] y = {1.0, 2.0, 3.0};
      boolean resetStatus = eventState.reset(t, y);
      assertEquals(false, resetStatus);
      assertEquals(false, eventState.pendingEvent);
      assertEquals(Double.NaN, eventState.pendingEventTime, 0.0001);
    } catch (EventException e) {
      fail("EventException was thrown");
    }
  }
}