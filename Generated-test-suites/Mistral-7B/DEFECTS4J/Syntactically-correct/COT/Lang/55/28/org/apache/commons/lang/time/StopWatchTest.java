import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 private StopWatch stopWatch;

 @Before
 public void setUp() {
 stopWatch = new StopWatch();
 }

 @Test
 public void testStartAndStop() {
 stopWatch.start();
 stopWatch.stop();
 long actual = stopWatch.getTime();
 Assert.assertNotEquals(-1, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 Assert.assertNotEquals(-1, splitTime);
 stopWatch.unsplit();
 long actual = stopWatch.getTime();
 Assert.assertEquals(splitTime, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch.start();
 long startTime = System.currentTimeMillis();
 stopWatch.suspend();
 long pauseTime = System.currentTimeMillis() - startTime;
 stopWatch.resume();
 long actual = stopWatch.getTime();
 Assert.assertEquals(pauseTime, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendTwice() {
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.suspend();
 }

 @Test
 public void testReset() {
 stopWatch.start();
 long time = stopWatch.getTime();
 stopWatch.reset();
 long actual = stopWatch.getTime();
 Assert.assertEquals(-1, actual);
 }

 @Test
 public void testGetTimeAndSplitTime() {
 stopWatch.start();
 stopWatch.split();
 long time = stopWatch.getTime();
 long splitTime = stopWatch.getSplitTime();
 Assert.assertEquals(time + splitTime, stopWatch.getTime());
 }

 @Test
 public void testToStringAndtoSplitString() {
 stopWatch.start();
 stopWatch.split();
 String timeString = stopWatch.toString();
 String splitString = stopWatch.toSplitString();
 Assert.assertNotEquals(timeString, splitString);
 }
}