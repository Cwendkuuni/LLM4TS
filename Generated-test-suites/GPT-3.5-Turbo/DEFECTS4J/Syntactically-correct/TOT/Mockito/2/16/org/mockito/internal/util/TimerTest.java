import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest {

  @Test
  public void testIsCounting_WhenTimerIsNotStarted_ReturnsFalse() {
    Timer timer = new Timer(1000);
    assertFalse(timer.isCounting());
  }

  @Test
  public void testIsCounting_WhenTimerIsStartedWithinDuration_ReturnsTrue() {
    Timer timer = new Timer(2000);
    timer.start();
    assertTrue(timer.isCounting());
  }

  @Test
  public void testIsCounting_WhenTimerIsStartedAndDurationHasPassed_ReturnsFalse() {
    Timer timer = new Timer(1000);
    timer.start();
    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertFalse(timer.isCounting());
  }

  @Test
  public void testStart_WhenTimerIsStarted_StartTimeIsSet() {
    Timer timer = new Timer(1000);
    timer.start();
    assertNotEquals(-1, timer.getStartTime());
  }

  @Test
  public void testStart_WhenTimerIsStarted_StartTimeIsCurrentTimeMillis() {
    Timer timer = new Timer(1000);
    long currentTime = System.currentTimeMillis();
    timer.start();
    assertEquals(currentTime, timer.getStartTime(), 100);
    // Allow 100 milliseconds time difference due to time elapsed between getting current time and setting start time.
  }
}