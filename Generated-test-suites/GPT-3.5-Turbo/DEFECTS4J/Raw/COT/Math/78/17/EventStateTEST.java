import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.events.EventException;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {
  private EventState eventState;

  @Before
  public void setUp() {
    eventState = new EventState(new EventHandler() {
      @Override
      public double g(double t, double[] y) throws DerivativeException, EventException {
        return 0;
      }

      @Override
      public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
        return 0;
      }

      @Override
      public void resetState(double t, double[] y) throws EventException {
      }
    }, 1.0, 0.01, 100);
  }

  @Test
  public void testReinitializeBegin() {
    double tStart = 0.0;
    double[] yStart = new double[]{1.0, 2.0, 3.0};
    try {
      eventState.reinitializeBegin(tStart, yStart);
    } catch (EventException e) {
      Assert.fail("Unexpected EventException in reinitializeBegin: " + e.getMessage());
    }
  }

  @Test
  public void testEvaluateStep() {
    StepInterpolator interpolator = new StepInterpolator() {
      @Override
      public void setInterpolatedTime(double time) throws IllegalArgumentException {
      }

      @Override
      public double getPreviousTime() {
        return 0;
      }

      @Override
      public double getCurrentTime() {
        return 1;
      }

      @Override
      public void writeExternal(ObjectOutput out) throws IOException {
      }

      @Override
      public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
      }

      @Override
      public boolean isForward() {
        return true;
      }

      @Override
      public void setSoftBounds(double t0, double t1) throws NumberIsTooLargeException {
      }

      @Override
      public void storeTime(double t) {
      }

      @Override
      public double getInterpolatedTime() {
        return 0;
      }

      @Override
      public double getGlobalPreviousTime() {
        return 0;
      }

      @Override
      public double getGlobalCurrentTime() {
        return 0;
      }

      @Override
      public double getGlobalInterpolatedTime() {
        return 0;
      }

      @Override
      public double[] getInterpolatedState() throws DerivativeException {
        return new double[]{1.0, 2.0, 3.0};
      }

      @Override
      public double[] getInterpolatedDerivatives() throws DerivativeException {
        return new double[]{4.0, 5.0, 6.0};
      }

      @Override
      public double[] getInterpolatedSecondaryState(int index) throws DerivativeException {
        return new double[0];
      }

      @Override
      public double[] getInterpolatedSecondaryDerivatives(int index) throws DerivativeException {
        return new double[0];
      }
    };
    try {
      Assert.assertFalse(eventState.evaluateStep(interpolator));
    } catch (DerivativeException | EventException | ConvergenceException e) {
      Assert.fail("Unexpected exception in evaluateStep: " + e.getMessage());
    }
  }

  @Test
  public void testGetEventTime() {
    Assert.assertEquals(Double.NaN, eventState.getEventTime(), 0.0);
  }

  @Test
  public void testStepAccepted() {
    double t = 0.0;
    double[] y = new double[]{1.0, 2.0, 3.0};
    try {
      eventState.stepAccepted(t, y);
    } catch (EventException e) {
      Assert.fail("Unexpected EventException in stepAccepted: " + e.getMessage());
    }
  }

  @Test
  public void testStop() {
    Assert.assertFalse(eventState.stop());
  }

  @Test
  public void testReset() {
    double t = 0.0;
    double[] y = new double[]{1.0, 2.0, 3.0};
    try {
      Assert.assertFalse(eventState.reset(t, y));
    } catch (EventException e) {
      Assert.fail("Unexpected EventException in reset: " + e.getMessage());
    }
  }
}