import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000); // initialize with a duration of 1 second
    }

    @Test
    public void testIsCounting_whenTimerIsNotStarted_thenShouldReturnFalse() {
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsRunning_thenShouldReturnTrue() {
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCounting_whenTimerHasNotBeenInitialized_thenShouldThrowException() {
        Assert.assertFalse(new Timer(0).isCounting()); // create a timer with zero duration and test isCounting without starting it
    }

    @Test
    public void testStart_whenTimerIsNotRunning_thenShouldStartCounting() {
        Assert.assertFalse(timer.isCounting());
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_whenTimerIsAlreadyRunning_thenShouldNotChangeState() {
        timer.start();
        Assert.assertTrue(timer.isCounting());
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerHasReachedEnd_thenShouldReturnFalse() {
        timer.start();
        TimeUnit.MILLISECONDS.sleep(1500); // wait for timer to expire
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerHasBeenReset_thenShouldReturnFalseAtFirst() {
        timer.start();
        timer.reset(); // reset the timer
        Assert.assertFalse(timer.isCounting());
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @After
    public void tearDown() {
        timer = null;
    }

}