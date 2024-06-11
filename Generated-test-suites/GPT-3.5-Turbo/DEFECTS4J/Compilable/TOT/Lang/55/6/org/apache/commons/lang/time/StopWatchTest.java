package org.apache.commons.lang.time;
import static org.junit.Assert.*;
import org.junit.*;

public class StopWatchTest {

  private StopWatch stopWatch;

  @Before
  public void setUp() {
    stopWatch = new StopWatch();
  }

  // Test case for start(), getTime() and stop()
  @Test
  public void testStartAndGetTimeAndStop() {
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.stop();
    long time = stopWatch.getTime();
    assertTrue(time > 0 && time < 1500);
  }

  // Test case for split(), getSplitTime() and unsplit()
  @Test
  public void testSplitAndGetSplitTimeAndUnsplit() {
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.split();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.unsplit();
    long splitTime = stopWatch.getSplitTime();
    assertTrue(splitTime > 0 && splitTime < 2500);
  }

  // Test case for suspend(), getTime() and resume()
  @Test
  public void testSuspendAndGetTimeAndResume() {
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.suspend();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.resume();
    long time = stopWatch.getTime();
    assertTrue(time > 1000 && time < 2500);
  }

  // Test case for reset() and getTime()
  @Test
  public void testResetAndGetTime() {
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.reset();
    long time = stopWatch.getTime();
    assertEquals(time, 0);
  }

  // Test case for illegal state exception when calling stop() without start()
  @Test(expected = IllegalStateException.class)
  public void testStopWithoutStart() {
    stopWatch.stop();
  }

  // Test case for illegal state exception when calling resume() without suspend()
  @Test(expected = IllegalStateException.class)
  public void testResumeWithoutSuspend() {
    stopWatch.resume();
  }

  // Test case for illegal state exception when calling unsplit() without split()
  @Test(expected = IllegalStateException.class)
  public void testUnsplitWithoutSplit() {
    stopWatch.unsplit();
  }

  // Test case for illegal state exception when calling getSplitTime() without split()
  @Test(expected = IllegalStateException.class)
  public void testGetSplitTimeWithoutSplit() {
    stopWatch.getSplitTime();
  }

  // Test case for illegal state exception when calling suspend() without start()
  @Test(expected = IllegalStateException.class)
  public void testSuspendWithoutStart() {
    stopWatch.suspend();
  }

  // Test case for illegal state exception when calling resume() without suspend()
  @Test(expected = IllegalStateException.class)
  public void testResumeWithoutSuspend2() {
    stopWatch.resume();
  }

  // Test case for illegal state exception when calling getTime() without start or stop
  @Test(expected = RuntimeException.class)
  public void testGetTimeWithoutStartOrStop() {
    stopWatch.getTime();
  }

  // Test case for illegal state exception when calling getSplitTime() without split()
  @Test(expected = IllegalStateException.class)
  public void testGetSplitTimeWithoutSplit2() {
    stopWatch.getSplitTime();
  }

  // Test case for toString() method
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
  }

  // Test case for toSplitString() method
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
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.unsplit();
    String splitTimeString = stopWatch.toSplitString();
    assertNotNull(splitTimeString);
  }

}