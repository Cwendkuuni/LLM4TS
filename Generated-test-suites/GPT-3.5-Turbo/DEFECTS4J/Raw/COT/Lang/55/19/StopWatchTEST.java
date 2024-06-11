
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {
	
	@Test
	public void testStart() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
		Assert.assertEquals(-1, stopWatch.getStopTime());
	}
	
	@Test
	public void testStop() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		stopWatch.stop();
		Assert.assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
		Assert.assertNotEquals(-1, stopWatch.getStopTime());
	}
	
	@Test
	public void testReset() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		stopWatch.stop();
		stopWatch.reset();
		Assert.assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
		Assert.assertEquals(-1, stopWatch.getStartTime());
		Assert.assertEquals(-1, stopWatch.getStopTime());
	}
	
	@Test
	public void testSplit() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		stopWatch.split();
		Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
		Assert.assertNotEquals(-1, stopWatch.getStopTime());
	}
	
	@Test
	public void testUnsplit() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		stopWatch.split();
		stopWatch.unsplit();
		Assert.assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
		Assert.assertEquals(-1, stopWatch.getStopTime());
	}
	
	@Test
	public void testSuspend() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		stopWatch.suspend();
		Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
		Assert.assertNotEquals(-1, stopWatch.getStopTime());
	}
	
	@Test
	public void testResume() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		stopWatch.suspend();
		stopWatch.resume();
		Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
		Assert.assertEquals(-1, stopWatch.getStopTime());
	}
	
	@Test
	public void testGetTime() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopWatch.stop();
		
		Assert.assertTrue(stopWatch.getTime() >= 1000);
	}
	
	@Test
	public void testGetSplitTime() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopWatch.split();
		Assert.assertTrue(stopWatch.getSplitTime() >= 1000);
	}
	
	@Test
	public void testToString() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopWatch.stop();
		Assert.assertTrue(!stopWatch.toString().isEmpty());
	}
	
	@Test
	public void testToSplitString() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopWatch.split();
		Assert.assertTrue(!stopWatch.toSplitString().isEmpty());
	}
}
