import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {
  
  private EventState eventState;
  
  @Before
  public void setup() {
    eventState = new EventState(new EventHandler() {
      public double g(double t, double[] y) {
        // Dummy implementation
        return 0.0;
      }
      
      public int eventOccurred(double t, double[] y, boolean increasing) {
        // Dummy implementation
        return EventHandler.CONTINUE;
      }
      
      public void resetState(double t, double[] y) {
        // Dummy implementation
      }
    }, 0.01, 0.01, 100);
  }
  
  @Test
  public void testGetEventHandler() {
    assertNotNull(eventState.getEventHandler());
  }
  
  @Test
  public void testGetMaxCheckInterval() {
    assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
  }
  
  @Test
  public void testGetConvergence() {
    assertEquals(0.01, eventState.getConvergence(), 0.0);
  }
  
  @Test
  public void testGetMaxIterationCount() {
    assertEquals(100, eventState.getMaxIterationCount());
  }

  @Test
  public void testReinitializeBegin() {
    double tStart = 0.0;
    double[] yStart = new double[] {1.0, 2.0, 3.0};
    try {
        eventState.reinitializeBegin(tStart, yStart);
    } catch (EventException e) {
        fail("EventException occurred");
    }
    assertEquals(tStart, eventState.t0, 0.0);
    assertEquals(handler.g(tStart, yStart), eventState.g0, 0.0);
    assertEquals(true, eventState.g0Positive);
  }
  
  @Test
  public void testEvaluateStep() {
    StepInterpolator interpolator = new StepInterpolator() {
      public boolean isForward() {
        return true;
      }
      
      public void setInterpolatedTime(double t) throws DerivativeException {
        // Dummy implementation
      }
      
      public double getCurrentTime() {
        return 0.0;
      }
      
      public double[] getInterpolatedState() throws DerivativeException {
        return new double[] {1.0, 2.0, 3.0};
      }
      
      public double[] getInterpolatedDerivatives() throws DerivativeException {
        return new double[] {1.0, 1.0, 1.0};
      }
      
      public double[] getInterpolatedSecondaryState(int index) throws DerivativeException {
        return new double[] {1.0, 1.0, 1.0};
      }
      
      public double[] getInterpolatedSecondaryDerivatives(int index) throws DerivativeException {
        return new double[] {1.0, 1.0, 1.0};
      }
    };
    try {
        assertFalse(eventState.evaluateStep(interpolator));
    } catch (DerivativeException e) {
        fail("DerivativeException occurred");
    } catch (EventException e) {
        fail("EventException occurred");
    } catch (ConvergenceException e) {
        fail("ConvergenceException occurred");
    }
  }
  
  @Test
  public void testGetEventTime() {
    assertEquals(Double.NaN, eventState.getEventTime(), 0.0);
  }
  
  @Test
  public void testStepAccepted() {
    double t = 0.0;
    double[] y = new double[] {1.0, 2.0, 3.0};
    try {
        eventState.stepAccepted(t, y);
    } catch (EventException e) {
        fail("EventException occurred");
    }
    assertEquals(t, eventState.t0, 0.0);
    assertEquals(handler.g(t, y), eventState.g0, 0.0);
    assertEquals(true, eventState.g0Positive);
    assertEquals(EventHandler.CONTINUE, eventState.nextAction);
  }
  
  @Test
  public void testStop() {
    assertFalse(eventState.stop());
  }
  
  @Test
  public void testReset() {
    double t = 0.0;
    double[] y = new double[] {1.0, 2.0, 3.0};
    try {
        assertFalse(eventState.reset(t, y));
    } catch (EventException e) {
        fail("EventException occurred");
    }
    assertEquals(false, eventState.pendingEvent);
    assertEquals(Double.NaN, eventState.pendingEventTime, 0.0);
  }
  
}