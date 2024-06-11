import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

  private StopWatch stopWatch;

  @Before
  public void setUp() {
    stopWatch = new StopWatch();
  }

  @Test
  public void testStart() {
    stopWatch.start();
    assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
  }

  @Test(expected = IllegalStateException.class)
  public void testStartTwiceWithoutReset() {
    stopWatch.start();
    stopWatch.start();
  }

  @Test
  public void testStop() {
    stopWatch.start();
    stopWatch.stop();
    assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
  }

  @Test(expected = IllegalStateException.class)
  public void testStopWithoutStart() {
    stopWatch.stop();
  }

  @Test
  public void testReset() {
    stopWatch.start();
    stopWatch.stop();
    stopWatch.reset();
    assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
  }

  @Test
  public void testSplit() {
    stopWatch.start();
    stopWatch.split();
    assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
  }

  @Test(expected = IllegalStateException.class)
  public void testSplitWithoutStart() {
    stopWatch.split();
  }

  @Test
  public void testUnsplit() {
    stopWatch.start();
    stopWatch.split();
    stopWatch.unsplit();
    assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
  }

  @Test(expected = IllegalStateException.class)
  public void testUnsplitWithoutSplit() {
    stopWatch.unsplit();
  }

  @Test
  public void testSuspend() {
    stopWatch.start();
    stopWatch.suspend();
    assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
  }

  @Test(expected = IllegalStateException.class)
  public void testSuspendWithoutStart() {
    stopWatch.suspend();
  }

  @Test
  public void testResume() {
    stopWatch.start();
    stopWatch.suspend();
    stopWatch.resume();
    assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
  }

  @Test(expected = IllegalStateException.class)
  public void testResumeWithoutSuspend() {
    stopWatch.resume();
  }

  @Test
  public void testGetTime() {
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.stop();
    assertTrue(stopWatch.getTime() > 0);
  }

  @Test(expected = RuntimeException.class)
  public void testGetTimeWhileRunning() {
    stopWatch.start();
    stopWatch.getTime();
  }

  @Test
  public void testGetSplitTime() {
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.split();
    assertTrue(stopWatch.getSplitTime() > 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetSplitTimeWithoutSplit() {
    stopWatch.getSplitTime();
  }

  @Test
  public void testToString() {
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.stop();
    String timeString = stopWatch.toString();
    assertNotNull(timeString);
    assertFalse(timeString.isEmpty());
  }

  @Test
  public void testToSplitString() {
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.split();
    String splitTimeString = stopWatch.toSplitString();
    assertNotNull(splitTimeString);
    assertFalse(splitTimeString.isEmpty());
  }
  
}