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
 long expectedTime = 0; // assuming no time has passed during test setup and teardown
 Assert.assertEquals("Time should be zero when watch is started and stopped immediately.", expectedTime, actualTime);
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 long actualSplitTime = stopWatch.getSplitTime();
 long expectedSplitTime = 0; // assuming no time has passed during split and unsplit
 Assert.assertEquals("Split time should be zero when watch is split and unsplit immediately.", expectedSplitTime, actualSplitTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsplitWhenNotSplit() {
 stopWatch = new StopWatch();
 stopWatch.unsplit();
 }

 @Test
 public void testResumeAndSuspend() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 long expectedTime = 0; // assuming no time has passed during test setup and teardown
 Assert.assertEquals("Time should be zero when watch is started, suspended, and resumed immediately.", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testResumeWhenNotSuspended() {
 stopWatch = new StopWatch();
 stopWatch.resume();
 }

 @Test
 public void testStartTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testStartAfterStop() {
 stopWatch = new StopWatch();
 stopWatch.stop();
 stopWatch.start();
 }

 @Test
 public void testLongRunningTest() {
 stopWatch = new StopWatch();
 stopWatch.start();
 try {
 Thread.sleep(1000); // simulate long running test
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 long expectedTime = 1000; // assuming 1 second has passed during test
 Assert.assertEquals("Time should match expected value for long running test.", expectedTime, actualTime);
 }
}