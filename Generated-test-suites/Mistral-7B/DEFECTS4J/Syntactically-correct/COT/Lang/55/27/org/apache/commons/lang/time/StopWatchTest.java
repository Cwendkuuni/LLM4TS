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
 long actualTime = stopWatch.getTime();
 Assert.assertNotNull(actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 Assert.assertNotNull(splitTime);
 stopWatch.unsplit();
 long actualTime = stopWatch.getTime();
 Assert.assertEquals(splitTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsplitWithoutSplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.unsplit();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch = new StopWatch();
 stopWatch.start();
 long startTime = System.currentTimeMillis();
 stopWatch.suspend();
 long pauseTime = System.currentTimeMillis() - startTime;
 stopWatch.resume();
 long actualTime = stopWatch.getTime();
 Assert.assertEquals(pauseTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendWithoutRunning() {
 stopWatch = new StopWatch();
 stopWatch.suspend();
 }

 @Test
 public void testReset() {
 stopWatch = new StopWatch();
 stopWatch.start();
 long actualTime = stopWatch.getTime();
 Assert.assertNotNull(actualTime);
 stopWatch.reset();
 Assert.assertEquals(-1, stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testStopWithoutRunning() {
 stopWatch = new StopWatch();
 stopWatch.stop();
 }

 @Test
 public void testGetTimeAfterStartButBeforeStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 long actualTime = stopWatch.getTime();
 Assert.assertNotNull(actualTime);
 }

 @Test
 public void testGetSplitTimeAfterSplitButBeforeStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long actualSplitTime = stopWatch.getSplitTime();
 Assert.assertNotNull(actualSplitTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetSplitTimeBeforeSplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 long actualSplitTime = stopWatch.getSplitTime();
 }

 @Test(expected = IllegalStateException.class)
 public void testToStringBeforeStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 String actualToString = stopWatch.toString();
 }

 @Test
 public void testToStringAfterStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 String actualToString = stopWatch.toString();
 Assert.assertNotNull(actualToString);
 }

}