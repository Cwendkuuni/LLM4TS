
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StopWatchTest {

	private StopWatch stopWatch;
	
	@Before
	public void setUp() {
		stopWatch = new StopWatch();
	}
	
	@Test
	public void testStart() {
		stopWatch.start();
		assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
		assertEquals(-1, stopWatch.getStopTime());
		assertNotNull(stopWatch.getStartTime());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testStartWhileRunning() {
		stopWatch.start();
		assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
		stopWatch.start();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testStartAfterStop() {
		stopWatch.start();
		assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
		stopWatch.stop();
		stopWatch.start();
	}
	
	@Test
	public void testStop() {
		stopWatch.start();
		stopWatch.stop();
		assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
		assertNotNull(stopWatch.getStartTime());
		assertNotNull(stopWatch.getStopTime());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testStopWhileNotRunning() {
		stopWatch.stop();
	}
	
	@Test
	public void testReset() {
		stopWatch.start();
		stopWatch.reset();
		assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
		assertEquals(-1, stopWatch.getSplitState());
		assertEquals(-1, stopWatch.getStartTime());
		assertEquals(-1, stopWatch.getStopTime());
	}
	
	@Test
	public void testResetWhileRunning() {
		stopWatch.start();
		stopWatch.reset();
		assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
		stopWatch.start();
		assertNotNull(stopWatch.getStartTime());
	}
	
	@Test
	public void testSplit() {
		stopWatch.start();
		stopWatch.split();
		assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
		assertNotNull(stopWatch.getStartTime());
		assertNotNull(stopWatch.getStopTime());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSplitWhileNotRunning() {
		stopWatch.split();
	}
	
	@Test
	public void testUnsplit() {
		stopWatch.start();
		stopWatch.split();
		stopWatch.unsplit();
		assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
		assertNotEquals(-1, stopWatch.getStartTime());
		assertEquals(-1, stopWatch.getStopTime());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testUnsplitWithoutSplit() {
		stopWatch.unsplit();
	}
	
	@Test
	public void testSuspend() {
		stopWatch.start();
		stopWatch.suspend();
		assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
		assertNotNull(stopWatch.getStopTime());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSuspendWhileNotRunning() {
		stopWatch.suspend();
	}
	
	@Test
	public void testResume() {
		stopWatch.start();
		stopWatch.suspend();
		stopWatch.resume();
		assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
		assertNotNull(stopWatch.getStartTime());
		assertNotNull(stopWatch.getStopTime());
		assertNotEquals(-1, stopWatch.getStartTime());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testResumeWhileNotSuspended() {
		stopWatch.resume();
	}
	
	@Test
	public void testGetTime() {
		stopWatch.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopWatch.stop();
		long elapsedTime = stopWatch.getTime();
		assertTrue(elapsedTime >= 2000);
	}
	
	@Test
	public void testGetTimeBeforeStart() {
		long elapsedTime = stopWatch.getTime();
		assertEquals(0, elapsedTime);
	}
	
	@Test
	public void testGetSplitTime() {
		stopWatch.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopWatch.split();
		long splitTime = stopWatch.getSplitTime();
		assertTrue(splitTime >= 2000);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetSplitTimeWithoutSplit() {
		stopWatch.getSplitTime();
	}
}

