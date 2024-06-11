import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StopWatchTest {
  
  @Test
  public void testStartAndStop() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.stop();
    
    long elapsedTime = stopWatch.getTime();
    assertEquals(1000, elapsedTime, 100);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testStartTwiceWithoutReset() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.start();
  }
  
  @Test
  public void testReset() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.reset();
    
    long elapsedTime = stopWatch.getTime();
    assertEquals(0, elapsedTime);
  }
  
  @Test
  public void testSplitAndUnsplit() {
    StopWatch stopWatch = new StopWatch();
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
    stopWatch.stop();
    
    long elapsedTime = stopWatch.getTime();
    assertEquals(2000, elapsedTime, 100);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testUnsplitBeforeSplit() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.unsplit();
  }
  
  @Test
  public void testSuspendAndResume() {
    StopWatch stopWatch = new StopWatch();
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
    stopWatch.stop();
    
    long elapsedTime = stopWatch.getTime();
    assertEquals(2000, elapsedTime, 100);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testResumeBeforeSuspend() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.resume();
  }
  
  @Test
  public void testGetSplitTime() {
    StopWatch stopWatch = new StopWatch();
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
    stopWatch.stop();
    
    long splitTime = stopWatch.getSplitTime();
    assertEquals(2000, splitTime, 100);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetSplitTimeWithoutSplit() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.getSplitTime();
  }
  
  @Test
  public void testToString() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    stopWatch.stop();
    
    String timeString = stopWatch.toString();
    assertEquals("00:00:01", timeString);
  }
  
  @Test
  public void testToSplitString() {
    StopWatch stopWatch = new StopWatch();
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
    stopWatch.stop();
    
    String splitString = stopWatch.toSplitString();
    assertEquals("00:00:02", splitString);
  }
  
}