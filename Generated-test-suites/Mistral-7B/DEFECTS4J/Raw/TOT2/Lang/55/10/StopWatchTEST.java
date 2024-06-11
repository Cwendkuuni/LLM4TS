import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 private StopWatch stopWatch;

 @Test
 public void testStartAndStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 // some code execution here
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 long expectedTime = 42L; // expected time for some code execution
 Assert.assertEquals("Incorrect time measurement", expectedTime, actualTime);
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
 // some code execution here
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 long expectedSplitTime = 21L; // expected time for some code execution
 Assert.assertEquals("Incorrect split time measurement", expectedSplitTime, splitTime);
 stopWatch.unsplit();
 long actualTime = stopWatch.getTime();
 long expectedTime = 42L; // expected time for some code execution + split time
 Assert.assertEquals("Incorrect time measurement after unsplit", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitBeforeStart() {
 stopWatch = new StopWatch();
 stopWatch.split();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch = new StopWatch();
 stopWatch.start();
 // some code execution here to reach suspend state
 stopWatch.suspend();
 // some time elapsed here
 stopWatch.resume();
 long actualTime = stopWatch.getTime();
 long expectedTime = 42L; // expected time for some code execution before suspend
 Assert.assertEquals("Incorrect time measurement", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendBeforeRun() {
 stopWatch = new StopWatch();
 stopWatch.suspend();
 }

 @Test
 public void testReset() {
 stopWatch = new StopWatch();
 stopWatch.start();
 // some code execution here
 stopWatch.reset();
 long actualTime = stopWatch.getTime();
 Assert.assertEquals("Reset should return zero", 0, actualTime);
 }

 @Test
 public void testMultipleTests() {
 stopWatch = new StopWatch();
 testStartAndStop();
 testSplitAndUnsplit();
 testSuspendAndResume();
 testReset();
 }

}