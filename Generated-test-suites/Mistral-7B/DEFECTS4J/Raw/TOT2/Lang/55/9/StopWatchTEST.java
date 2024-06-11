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
 long actual = stopWatch.getTime();
 long expected = 0; // no time has passed during test
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 stopWatch.unsplit();
 long actual = stopWatch.getTime();
 long expected = splitTime;
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testStopBeforeStart() {
 stopWatch = new StopWatch();
 stopWatch.stop();
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendBeforeSuspend() {
 stopWatch = new StopWatch();
 stopWatch.suspend();
 stopWatch.suspend();
 }

 @Test(expected = IllegalStateException.class)
 public void testResumeBeforeSuspend() {
 stopWatch = new StopWatch();
 stopWatch.resume();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 long actual = stopWatch.getTime();
 long expected = 0; // no time has passed during test
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testTimeout() {
 stopWatch = new StopWatch();
 stopWatch.start();
 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 // handle exception
 }
 stopWatch.stop();
 long actual = stopWatch.getTime();
 long expected = 1000; // one second has passed during test
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitAfterStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 stopWatch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsplitAfterSplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 stopWatch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testResumeAfterStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 stopWatch.resume();
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitAfterSplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test
 public void testToString() {
 stopWatch = new StopWatch();
 stopWatch.start();
 Thread.sleep(1000);
 stopWatch.stop();
 String actual = stopWatch.toString();
 String expected = "1:00.000"; // one second has passed during test
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testToSplitString() {
 stopWatch = new StopWatch();
 stopWatch.start();
 Thread.sleep(500);
 stopWatch.split();
 Thread.sleep(500);
 stopWatch.unsplit();
 String actual = stopWatch.toSplitString();
 String expected = "0:01.000"; // one and a half seconds have passed during test
 Assert.assertEquals(expected, actual);
 }
}