import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {
  private static final double DELTA = 1e-15;
  StopWatch stopWatch;

  @Test
  public void testStart() {
    stopWatch = new StopWatch();
    stopWatch.start();
    Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
  }

  @Test(expected = IllegalStateException.class)
  public void testStartTwiceWithoutReset() {
    stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.start();
  }

  @Test
  public void testStop() {
    stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.stop();
    Assert.assertEquals(StopWatch.STATE_STOPPED, stopWatch.runningState);
  }

  @Test(expected = IllegalStateException.class)
  public void testStopWithoutStart() {
    stopWatch = new StopWatch();
    stopWatch.stop();
  }

  @Test
  public void testReset() {
    stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.reset();
    Assert.assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.runningState);
    Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
    Assert.assertEquals(-1, stopWatch.startTime);
    Assert.assertEquals(-1, stopWatch.stopTime);
  }

  @Test
  public void testSplit() {
    stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.split();
    Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.splitState);
  }

  @Test(expected = IllegalStateException.class)
  public void testSplitWithoutStart() {
    stopWatch = new StopWatch();
    stopWatch.split();
  }

  @Test
  public void testUnsplit() {
    stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.split();
    stopWatch.unsplit();
    Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.splitState);
  }

  @Test(expected = IllegalStateException.class)
  public void testUnsplitWithoutSplit() {
    stopWatch = new StopWatch();
    stopWatch.unsplit();
  }

  @Test
  public void testSuspend() {
    stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.suspend();
    Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.runningState);
  }

  @Test(expected = IllegalStateException.class)
  public void testSuspendWithoutStart() {
    stopWatch = new StopWatch();
    stopWatch.suspend();
  }

  @Test
  public void testResume() {
    stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.suspend();
    stopWatch.resume();
    Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.runningState);
  }

  @Test(expected = IllegalStateException.class)
  public void testResumeWithoutSuspend() {
    stopWatch = new StopWatch();
    stopWatch.resume();
  }

  @Test
  public void testGetTime() {
    stopWatch = new StopWatch();
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.stop();
    double timeInSeconds = stopWatch.getTime() / 1000.0;
    Assert.assertEquals(1.0, timeInSeconds, DELTA);
  }

  @Test
  public void testGetTimeBeforeStart() {
    stopWatch = new StopWatch();
    double timeInSeconds = stopWatch.getTime() / 1000.0;
    Assert.assertEquals(0.0, timeInSeconds, DELTA);
  }

  @Test
  public void testGetSplitTime() {
    stopWatch = new StopWatch();
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.split();
    double splitTimeInSeconds = stopWatch.getSplitTime() / 1000.0;
    Assert.assertEquals(1.0, splitTimeInSeconds, DELTA);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetSplitTimeWithoutSplit() {
    stopWatch = new StopWatch();
    stopWatch.getSplitTime();
  }

  @Test
  public void testToString() {
    stopWatch = new StopWatch();
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.stop();
    Assert.assertTrue(stopWatch.toString().matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
  }

  @Test
  public void testToStringBeforeStart() {
    stopWatch = new StopWatch();
    Assert.assertEquals("00:00:00.000", stopWatch.toString());
  }

  @Test
  public void testToSplitString() {
    stopWatch = new StopWatch();
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.split();
    Assert.assertTrue(stopWatch.toSplitString().matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
  }

  @Test(expected = IllegalStateException.class)
  public void testToSplitStringWithoutSplit() {
    stopWatch = new StopWatch();
    stopWatch.toSplitString();
  }
}