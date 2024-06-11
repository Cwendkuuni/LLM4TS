import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 @Test
 public void testStartAndStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();

 long actual = stopWatch.getTime();
 long expected = 0; // Assume that getTime() returns 0 when not started or stopped
 Assert.assertEquals("Time should be equal to 0", expected, actual);
 }

 @Test
 public void testSplitAndUnsplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 stopWatch.stop();

 long actual = stopWatch.getTime();
 long expected = 0; // Assume that getTime() returns 0 when not started or stopped
 Assert.assertEquals("Time should be equal to 0", expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwiceWithoutReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendResumeTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.suspend();
 stopWatch.resume();
 }

 @Test
 public void testSplitAndGetSplitTime() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();

 long expectedHours = 0;
 long expectedMinutes = 0;
 long expectedSeconds = 1;
 long expectedMilliseconds = 0;
 long expectedTotalMilliseconds = 100; // Assuming the test runs in under 100ms

 String expectedSplitString = String.format("%d:%02d:%02d.%03d",
 expectedHours, expectedMinutes, expectedSeconds, expectedMilliseconds);
 String actualSplitString = DurationFormatUtils.formatDurationHMS(splitTime);

 Assert.assertEquals("Split time format should match expected format", expectedSplitString, actualSplitString);
 }

 @Test
 public void testToStringAndToSplitString() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();

 String expectedToString = "0:0:1.000";
 String expectedToSplitString = "0:0:1.000";
 String actualToString = stopWatch.toString();
 String actualToSplitString = stopWatch.toSplitString();

 Assert.assertEquals("toString() should match expected format", expectedToString, actualToString);
 Assert.assertEquals("toSplitString() should match expected format", expectedToSplitString, actualToSplitString);
 }

}