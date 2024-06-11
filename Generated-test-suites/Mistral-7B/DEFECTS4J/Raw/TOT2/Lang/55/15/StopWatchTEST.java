import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

 // Test for constructor
 @Test
 public void testConstructor() {
 StopWatch stopWatch = new StopWatch();
 assertNotNull(stopWatch);
 }

 // Test for start() method
 @Test
 public void testStart() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 assertFalse(stopWatch.isRunning());
 stopWatch.stop();
 assertTrue(stopWatch.isRunning());
 }

 // Test for stop() method
 @Test
 public void testStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 long initialTime = stopWatch.getTime();
 stopWatch.stop();
 long finalTime = stopWatch.getTime();
 assertTrue(initialTime < finalTime);
 }

 // Test for reset() method
 @Test
 public void testReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.reset();
 assertEquals(0, stopWatch.getTime());
 }

 // Test for split() method
 @Test
 public void testSplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long initialSplitTime = stopWatch.getSplitTime();
 stopWatch.stop();
 long finalTime = stopWatch.getTime();
 assertTrue(initialSplitTime < finalTime);
 assertTrue(finalTime - initialSplitTime > 0);
 }

 // Test for unsplit() method
 @Test
 public void testUnsplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 assertEquals(0, stopWatch.getSplitTime());
 }

 // Test for suspend() method
 @Test
 public void testSuspend() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 long initialTime = stopWatch.getTime();
 stopWatch.suspend();
 long pausedTime = stopWatch.getTime();
 stopWatch.resume();
 long finalTime = stopWatch.getTime();
 assertTrue(initialTime < pausedTime);
 assertTrue(finalTime > pausedTime);
 }

 // Test for illegal state exceptions
 @Test(expected = IllegalStateException.class)
 public void testIllegalStateException1() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.stop();
 stopWatch.stop();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateException2() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.split();
 stopWatch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateException3() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 stopWatch.unsplit();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateException4() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.reset();
 stopWatch.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateException5() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.unsplit();
 }

 // Test for negative time
 @Test
 public void testNegativeTime() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 Thread.yield();
 Thread.yield();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 stopWatch.stop();
 long totalTime = stopWatch.getTime();
 long expectedTotalTime = splitTime + totalTime;
 long actualTotalTime = stopWatch.getTime() + splitTime;
 assertEquals(expectedTotalTime, actualTotalTime);
 }

}