import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

 //Test case for constructor
 @Test
 public void testStopWatchConstructor() {
 StopWatch stopWatch = new StopWatch();
 assertNull(stopWatch.getTime());
 }

 //Test case for start method
 @Test
 public void testStartMethod() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 assertFalse(stopWatch.isRunning());
 stopWatch.stop();
 long time = stopWatch.getTime();
 assertTrue(stopWatch.isRunning());
 assertEquals(time, 0);
 }

 //Test case for split method
 @Test
 public void testSplitMethod() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 assertEquals(splitTime, 0);
 stopWatch.stop();
 }

 //Test case for unsplit method
 @Test
 public void testUnsplitMethod() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 long time = stopWatch.getTime();
 assertEquals(time, 0);
 }

 //Test case for suspend and resume methods
 @Test
 public void testSuspendAndResumeMethods() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 long startTime = stopWatch.getTime();
 stopWatch.suspend();
 stopWatch.resume();
 long endTime = stopWatch.getTime();
 long difference = endTime - startTime;
 assertEquals(difference, 0);
 }

 //Test case for illegal state exceptions
 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionStartTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionSplitTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionUnsplitNoSplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.unsplit();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionResumeNoSuspend() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.resume();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionStopRunning() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.stop();
 stopWatch.stop();
 }
}