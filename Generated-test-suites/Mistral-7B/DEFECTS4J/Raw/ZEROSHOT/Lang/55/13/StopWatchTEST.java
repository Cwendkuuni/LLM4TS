import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 private StopWatch stopWatch;

 @Test
 public void testStartAndStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 Assert.assertNotEquals(-1, stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testReset() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 stopWatch.reset();
 Assert.assertEquals(-1, stopWatch.getTime());
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 Assert.assertNotEquals(-1, stopWatch.getTime());
 Assert.assertNotEquals(-1, stopWatch.getSplitTime());
 stopWatch.unsplit();
 Assert.assertNotEquals(-1, stopWatch.getTime());
 Assert.assertEquals(-1, stopWatch.getSplitTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitBeforeStart() {
 stopWatch = new StopWatch();
 stopWatch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsplitBeforeSplit() {
 stopWatch = new StopWatch();
 stopWatch.unsplit();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.stop();
 Assert.assertNotEquals(-1, stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendBeforeStart() {
 stopWatch = new StopWatch();
 stopWatch.suspend();
 }

 @Test
 public void testResumeBeforeSuspend() {
 stopWatch = new StopWatch();
 stopWatch.resume();
 }

 @Test
 public void testToString() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 Assert.assertTrue(DurationFormatUtils.isParsable(stopWatch.toString()));
 }
}