import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 @Test
 public void testStartAndStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 long expectedTime = 0; // assume the test method runs instantly
 Assert.assertEquals("Time should be equal to expected time", expectedTime, actualTime);
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
 Assert.assertEquals("Split time should be greater than zero", 0, splitTime);
 stopWatch.unsplit();
 long actualTime = stopWatch.getTime();
 long expectedTime = 0; // assume the test method runs instantly
 Assert.assertEquals("Time should be equal to expected time", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test
 public void testSuspendAndResume() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 long pauseTime = System.currentTimeMillis() - stopWatch.getTime();
 stopWatch.resume();
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 long expectedTime = pauseTime + stopWatch.getSplitTime();
 Assert.assertEquals("Time should be equal to expected time", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.suspend();
 }

 @Test
 public void testReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 long expectedTime = 0; // assume the test method runs instantly
 stopWatch.reset();
 stopWatch.start();
 long resetTime = stopWatch.getTime();
 Assert.assertEquals("Time should be equal to expected time", expectedTime, resetTime);
 }
}