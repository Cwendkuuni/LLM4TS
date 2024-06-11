import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {
  
  private StopWatch stopWatch;
  
  @Before
  public void setup() {
    stopWatch = new StopWatch();
  }
  
  @Test
  public void testStart() {
    stopWatch.start();
    assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
  }
  
  @Test
  public void testStartTwice() {
    stopWatch.start();
    try {
      stopWatch.start();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Stopwatch already started. ", e.getMessage());
    }
  }
  
  @Test
  public void testStartAfterStopped() {
    stopWatch.start();
    stopWatch.stop();
    try {
      stopWatch.start();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Stopwatch must be reset before being restarted. ", e.getMessage());
    }
  }
  
  @Test
  public void testStartAfterSuspended() {
    stopWatch.start();
    stopWatch.suspend();
    try {
      stopWatch.start();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Stopwatch must be reset before being restarted. ", e.getMessage());
    }
  }
  
  @Test
  public void testStop() {
    stopWatch.start();
    stopWatch.stop();
    assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
  }
  
  @Test
  public void testStopWithoutStart() {
    try {
      stopWatch.stop();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Stopwatch is not running. ", e.getMessage());
    }
  }
  
  @Test
  public void testReset() {
    stopWatch.start();
    stopWatch.stop();
    stopWatch.reset();
    assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
    assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    assertEquals(0, stopWatch.getTime());
  }
  
  @Test
  public void testSplit() {
    stopWatch.start();
    stopWatch.split();
    assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
  }
  
  @Test
  public void testSplitWithoutStart() {
    try {
      stopWatch.split();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Stopwatch is not running. ", e.getMessage());
    }
  }
  
  @Test
  public void testUnsplit() {
    stopWatch.start();
    stopWatch.split();
    stopWatch.unsplit();
    assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
  }
  
  @Test
  public void testUnsplitWithoutSplit() {
    try {
      stopWatch.unsplit();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Stopwatch has not been split. ", e.getMessage());
    }
  }
  
  @Test
  public void testSuspend() {
    stopWatch.start();
    stopWatch.suspend();
    assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
  }
  
  @Test
  public void testSuspendWithoutStart() {
    try {
      stopWatch.suspend();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Stopwatch must be running to suspend. ", e.getMessage());
    }
  }
  
  @Test
  public void testResume() {
    stopWatch.start();
    stopWatch.suspend();
    stopWatch.resume();
    assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
  }
  
  @Test
  public void testResumeWithoutSuspend() {
    try {
      stopWatch.resume();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Stopwatch must be suspended to resume. ", e.getMessage());
    }
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
    assertTrue(stopWatch.getTime() >= 1000);
  }
  
  @Test
  public void testGetTimeWhileRunning() {
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertTrue(stopWatch.getTime() >= 1000);
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
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.unsplit();
    assertEquals(1000, stopWatch.getSplitTime());
  }
  
  @Test
  public void testGetSplitTimeWithoutSplit() {
    try {
      stopWatch.getSplitTime();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Stopwatch must be split to get the split time. ", e.getMessage());
    }
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
    assertTrue(stopWatch.toString().matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
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
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.unsplit();
    assertTrue(stopWatch.toSplitString().matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
  }
}