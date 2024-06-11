package org.apache.commons.lang.time;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang.time.StopWatch;
import static org.junit.Assert.assertEquals;

public class StopWatchTest {
  
  private StopWatch stopWatch;
  
  @Before
  public void setUp() {
    stopWatch = new StopWatch();
  }
  
  @Test
  public void testStart() {
    stopWatch.start();
    assertEquals(stopWatch.getTime(), 0);
    assertEquals(stopWatch.toString(), "00:00:00.000");
  }
  
  @Test(expected=IllegalStateException.class)
  public void testStartTwice() {
    stopWatch.start();
    stopWatch.start();
  }
  
  @Test(expected=IllegalStateException.class)
  public void testStopBeforeStart() {
    stopWatch.stop();
  }
  
  @Test
  public void testStop() throws InterruptedException {
    stopWatch.start();
    Thread.sleep(1000);
    stopWatch.stop();
    long time = stopWatch.getTime();
    assertEquals(time, 1000, 100);
    assertEquals(stopWatch.toString(), "00:00:01.000");
  }
  
  @Test(expected=IllegalStateException.class)
  public void testStopTwice() throws InterruptedException {
    stopWatch.start();
    Thread.sleep(1000);
    stopWatch.stop();
    stopWatch.stop();
  }
  
  @Test(expected=IllegalStateException.class)
  public void testResetBeforeStart() {
    stopWatch.reset();
  }
  
  @Test
  public void testResetAfterStop() throws InterruptedException {
    stopWatch.start();
    Thread.sleep(1000);
    stopWatch.stop();
    stopWatch.reset();
    assertEquals(stopWatch.getTime(), 0);
    assertEquals(stopWatch.toString(), "00:00:00.000");
  }
  
  @Test
  public void testSplit() throws InterruptedException {
    stopWatch.start();
    Thread.sleep(500);
    stopWatch.split();
    long splitTime = stopWatch.getSplitTime();
    assertEquals(stopWatch.getTime(), 500, 100);
    assertEquals(splitTime, 500, 100);
    assertEquals(stopWatch.toString(), "00:00:00.500");
    assertEquals(stopWatch.toSplitString(), "00:00:00.500");
  }
  
  @Test(expected=IllegalStateException.class)
  public void testUnsplitBeforeSplit() {
    stopWatch.unsplit();
  }
  
  @Test
  public void testUnsplit() throws InterruptedException {
    stopWatch.start();
    Thread.sleep(500);
    stopWatch.split();
    Thread.sleep(500);
    stopWatch.unsplit();
    long time = stopWatch.getTime();
    assertEquals(time, 1000, 100);
    assertEquals(stopWatch.toString(), "00:00:01.000");
  }
  
  @Test(expected=IllegalStateException.class)
  public void testSuspendBeforeStart() {
    stopWatch.suspend();
  }
  
  @Test
  public void testSuspend() throws InterruptedException {
    stopWatch.start();
    Thread.sleep(500);
    stopWatch.suspend();
    Thread.sleep(500);
    stopWatch.resume();
    long time = stopWatch.getTime();
    assertEquals(time, 1000, 100);
    assertEquals(stopWatch.toString(), "00:00:01.000");
  }
  
  @Test(expected=IllegalStateException.class)
  public void testResumeBeforeSuspend() {
    stopWatch.resume();
  }
}