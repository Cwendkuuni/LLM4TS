import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {
  
  private StopWatch stopWatch;
  
  @Before
  public void setUp() {
    stopWatch = new StopWatch();
  }
  
  @Test
  public void testStart() {
    stopWatch.start();
    
    assertTrue(stopWatch.getTime() >= 0);
    assertFalse(stopWatch.isStopped());
    assertFalse(stopWatch.isSuspended());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testStartAlreadyRunning() {
    stopWatch.start();
    stopWatch.start();
  }
  
  @Test
  public void testStop() {
    stopWatch.start();
    stopWatch.stop();
    
    assertTrue(stopWatch.getTime() >= 0);
    assertTrue(stopWatch.isStopped());
    assertFalse(stopWatch.isSuspended());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testStopNotRunning() {
    stopWatch.stop();
  }
  
  @Test
  public void testReset() {
    stopWatch.start();
    stopWatch.reset();
    
    assertEquals(0, stopWatch.getTime());
    assertFalse(stopWatch.isStopped());
    assertFalse(stopWatch.isSuspended());
  }
  
  @Test
  public void testSplit() {
    stopWatch.start();
    stopWatch.split();
    
    assertFalse(stopWatch.isStopped());
    assertFalse(stopWatch.isSuspended());
    assertTrue(stopWatch.getSplitTime() >= 0);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSplitNotRunning() {
    stopWatch.split();
  }
  
  @Test
  public void testUnsplit() {
    stopWatch.start();
    stopWatch.split();
    stopWatch.unsplit();
    
    assertFalse(stopWatch.isStopped());
    assertFalse(stopWatch.isSuspended());
    assertFalse(stopWatch.hasSplit());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testUnsplitNotSplit() {
    stopWatch.unsplit();
  }
  
  @Test
  public void testSuspend() {
    stopWatch.start();
    stopWatch.suspend();
    
    assertTrue(stopWatch.getTime() >= 0);
    assertFalse(stopWatch.isStopped());
    assertTrue(stopWatch.isSuspended());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSuspendNotRunning() {
    stopWatch.suspend();
  }
  
  @Test
  public void testResume() {
    stopWatch.start();
    stopWatch.suspend();
    stopWatch.resume();
    
    assertTrue(stopWatch.getTime() >= 0);
    assertFalse(stopWatch.isStopped());
    assertFalse(stopWatch.isSuspended());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testResumeNotSuspended() {
    stopWatch.resume();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetSplitTimeNotSplit() {
    stopWatch.getSplitTime();
  }
  
  @Test
  public void testToString() {
    stopWatch.start();
    stopWatch.stop();
    
    String timeString = stopWatch.toString();
    assertEquals(timeString, DurationFormatUtils.formatDurationHMS(stopWatch.getTime()));
  }
  
  @Test
  public void testToSplitString() {
    stopWatch.start();
    stopWatch.split();
    
    String splitTimeString = stopWatch.toSplitString();
    assertEquals(splitTimeString, DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()));
  }
}