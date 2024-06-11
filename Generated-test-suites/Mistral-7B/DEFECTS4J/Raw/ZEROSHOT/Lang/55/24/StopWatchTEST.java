import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for StopWatch.
 */
public class StopWatchTest {

 @Test
 public void testStartAndStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long time = stopWatch.getTime();
 Assert.assertNotEquals("-1", Long.toString(time));
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
 Assert.assertNotEquals("-1", Long.toString(splitTime));
 stopWatch.unsplit();
 long time = stopWatch.getTime();
 Assert.assertEquals(splitTime, time);
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
 Thread.sleep(100);
 stopWatch.resume();
 long time = stopWatch.getTime();
 Assert.assertNotEquals("-1", Long.toString(time));
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
 stopWatch.stop();
 stopWatch.reset();
 long time = stopWatch.getTime();
 Assert.assertEquals("-1", Long.toString(time));
 }
}