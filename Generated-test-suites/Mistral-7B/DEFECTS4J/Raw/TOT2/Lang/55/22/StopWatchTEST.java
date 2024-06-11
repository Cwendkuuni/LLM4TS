import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 @Test
 public void testStartAndStop() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.stop();
 long actual = watch.getTime();
 long expected = 0; // time between start and stop should be zero
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSplitAndUnsplit() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.split();
 watch.stop();
 long splitTime = watch.getSplitTime();
 long totalTime = watch.getTime();
 long expectedSplitTime = 0; // time between split and stop
 long expectedTotalTime = splitTime;
 Assert.assertEquals(expectedSplitTime, splitTime);
 Assert.assertEquals(expectedTotalTime, totalTime);
 watch.unsplit();
 long actualTotalTime = watch.getTime();
 Assert.assertEquals(expectedTotalTime, actualTotalTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitAfterStop() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.stop();
 watch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsplitWithoutSplit() {
 StopWatch watch = new StopWatch();
 watch.unsplit();
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendAfterStart() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.suspend();
 }

 @Test(expected = IllegalStateException.class)
 public void testResumeWithoutSuspend() {
 StopWatch watch = new StopWatch();
 watch.resume();
 }

 @Test
 public void testSuspendAndResume() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.suspend();
 watch.resume();
 long actual = watch.getTime();
 long expected = 0; // time between suspend and resume should be zero
 Assert.assertEquals(expected, actual);
 }

}