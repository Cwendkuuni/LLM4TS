import org.apache.commons.lang.time.StopWatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
 assertTrue(stopWatch.getTime() > 0);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 stopWatch.start();
 stopWatch.start();
 fail("Should have thrown an IllegalStateException");
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 assertTrue(splitTime > 0);
 stopWatch.unsplit();
 assertTrue(stopWatch.getTime() > splitTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitWithoutStart() {
 stopWatch.split();
 fail("Should have thrown an IllegalStateException");
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 long suspendedTime = System.currentTimeMillis() - stopWatch.getStartTime();
 long resumedTime = System.currentTimeMillis() - stopWatch.getStartTime();
 assertTrue(suspendedTime > 0 && resumedTime > suspendedTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendWithoutRunning() {
 stopWatch.suspend();
 fail("Should have thrown an IllegalStateException");
 }

 @After
 public void tearDown() {
 stopWatch.reset();
 }

}