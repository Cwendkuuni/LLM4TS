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
 long actualTime = stopWatch.getTime();
 long expectedTime = 0; // assuming the test case runs instantly
 Assert.assertEquals("Incorrect time elapsed", expectedTime, actualTime);
 }

 @Test
 public void testSplitAndUnsplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 Assert.assertNotEquals("Split time should be greater than zero", 0, splitTime);
 stopWatch.unsplit();
 long actualTime = stopWatch.getTime();
 long expectedTime = splitTime;
 Assert.assertEquals("Incorrect time elapsed after unsplit", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwiceWithoutReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testSuspendAndResume() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 long pauseTime = System.currentTimeMillis() - stopWatch.getTime();
 stopWatch.resume();
 long actualTime = stopWatch.getTime();
 long expectedTime = stopWatch.getTime() + pauseTime;
 Assert.assertEquals("Incorrect time elapsed after resume", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendWithoutRunning() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.suspend();
 }

 @Test
 public void testToString() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 String actualString = stopWatch.toString();
 String expectedString = "0s"; // assuming the test case runs instantly
 Assert.assertNotEquals("Incorrect string representation", expectedString, actualString);
 stopWatch.stop();
 actualString = stopWatch.toString();
 int indexOfDot = actualString.lastIndexOf('.');
 long actualMillis = Long.parseLong(actualString.substring(indexOfDot));
 expectedMillis = 0; // assuming the test case runs instantly
 Assert.assertEquals("Incorrect string representation after stop", DurationFormatUtils.formatDuration(expectedMillis, "s"), actualString.substring(0, indexOfDot));
 }

 @Test
 public void testToSplitString() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 String actualString = stopWatch.toSplitString();
 String expectedString = "0s"; // assuming the test case runs instantly
 Assert.assertNotEquals("Incorrect string representation", expectedString, actualString);
 stopWatch.stop();
 actualString = stopWatch.toSplitString();
 int indexOfDot = actualString.lastIndexOf('.');
 long actualMillis = Long.parseLong(actualString.substring(indexOfDot));
 expectedMillis = 0; // assuming the test case runs instantly
 Assert.assertEquals("Incorrect string representation after stop", DurationFormatUtils.formatDuration(expectedMillis, "s"), actualString.substring(0, indexOfDot));
 }
}