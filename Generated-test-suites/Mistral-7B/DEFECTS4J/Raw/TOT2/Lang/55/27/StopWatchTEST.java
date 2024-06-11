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
 Assert.assertEquals(0, stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwiceWithoutReset() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 Assert.assertEquals(0, stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitWithoutStart() {
 stopWatch = new StopWatch();
 stopWatch.split();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.stop();
 Assert.assertEquals(0, stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendWithoutRun() {
 stopWatch = new StopWatch();
 stopWatch.suspend();
 }

 @Test
 public void testGetTime() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 Assert.assertNotEquals(0, stopWatch.getTime());
 }

 @Test
 public void testGetSplitTime() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 Assert.assertNotEquals(0, stopWatch.getSplitTime());
 }

 @Test
 public void testToStringAndToSplitString() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 String time = stopWatch.toString();
 String splitTime = stopWatch.toSplitString();
 Assert.assertNotEquals("", time);
 Assert.assertNotEquals("", splitTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetTimeWithoutStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 Assert.assertThrows(IllegalStateException.class, () -> stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetSplitTimeWithoutSplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 Assert.assertThrows(IllegalStateException.class, () -> stopWatch.getSplitTime());
 }
}