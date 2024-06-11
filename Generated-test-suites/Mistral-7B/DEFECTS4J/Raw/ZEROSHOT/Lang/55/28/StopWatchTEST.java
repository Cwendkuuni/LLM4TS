import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for StopWatch.java
 */
public class StopWatchTest {

 @Test
 public void testStartAndStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long time = stopWatch.getTime();
 Assert.assertNotEquals(-1, time);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testSplitAndUnsplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 Assert.assertNotEquals(-1, splitTime);
 stopWatch.unsplit();
 long time = stopWatch.getTime();
 Assert.assertEquals(splitTime + stopWatch.getTime(), time);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitBeforeStart() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.split();
 }

 @Test
 public void testSuspendAndResume() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 long startTime = stopWatch.getTime();
 stopWatch.suspend();
 long pauseTime = System.currentTimeMillis() - startTime;
 stopWatch.resume();
 stopWatch.stop();
 long finalTime = stopWatch.getTime();
 Assert.assertEquals(startTime + pauseTime, finalTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendBeforeStart() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.suspend();
 }

 @Test
 public void testReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 long time = stopWatch.getTime();
 Assert.assertNotEquals(-1, time);
 stopWatch.reset();
 Assert.assertEquals(-1, stopWatch.getTime());
 }

 @Test
 public void testToString() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 String toString = stopWatch.toString();
 Assert.assertNotNull(toString);
 }
}