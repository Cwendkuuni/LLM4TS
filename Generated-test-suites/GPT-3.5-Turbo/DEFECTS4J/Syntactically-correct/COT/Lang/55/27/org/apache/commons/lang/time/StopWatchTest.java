import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleStart() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        stopWatch.resume();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopWithoutRunning() {
        stopWatch.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutRunning() {
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeWithoutSplit() {
        stopWatch.getSplitTime();
    }

    @Test
    public void testStartStop() {
        stopWatch.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();

        long time = stopWatch.getTime();
        assertTrue(time >= 100);
    }

    @Test
    public void testReset() {
        stopWatch.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();

        stopWatch.reset();

        assertEquals(0, stopWatch.getTime());
        assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        assertEquals(StopWatch.STATE_UNSPLIT, stopWatch.getSplitState());
    }

    @Test
    public void testSplitUnsplit() {
        stopWatch.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        long splitTime1 = stopWatch.getSplitTime();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        long splitTime2 = stopWatch.getSplitTime();

        stopWatch.unsplit();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();
        long splitTime3 = stopWatch.getSplitTime();

        assertTrue(splitTime1 < splitTime2);
        assertEquals(splitTime2, splitTime3);
    }

    @Test
    public void testSuspendResume() {
        stopWatch.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.suspend();
        long totalTime = stopWatch.getTime();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();
        long resumedTime = stopWatch.getTime();

        assertTrue(totalTime == resumedTime);
    }
    
    @Test
    public void testToString() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();

        String timeString = stopWatch.toString();
        assertTrue(timeString.matches("\\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.split();

        String splitTimeString = stopWatch.toSplitString();
        assertTrue(splitTimeString.matches("\\d{2}:\\d{2}:\\d{2}"));
    }
    
    private int getRunningState() {
        try {
            Field runningStateField = StopWatch.class.getDeclaredField("runningState");
            runningStateField.setAccessible(true);
            return (int) runningStateField.get(stopWatch);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int getSplitState() {
        try {
            Field splitStateField = StopWatch.class.getDeclaredField("splitState");
            splitStateField.setAccessible(true);
            return (int) splitStateField.get(stopWatch);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

}