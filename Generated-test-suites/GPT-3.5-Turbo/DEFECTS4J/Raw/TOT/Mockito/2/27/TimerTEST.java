package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000); // Setting the duration to 1 second
    }

    @Test
    public void testIsCounting_WhenTimerIsNotStarted_ReturnsFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerIsStartedAndCountingDown_ReturnsTrue() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_WhenTimerExceedsDuration_ReturnsFalse() throws InterruptedException {
        timer.start();
        Thread.sleep(1100); // Waiting for 1.1 seconds to exceed the duration
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_WhenTimerIsNotStarted_AndAssertEnabled_ThrowsAssertionError() {
        assertFailOnAssertDisabled();
        timer.isCounting();
    }

    private void assertFailOnAssertDisabled() {
        // This method is used to ensure that assertion failures are reported when assertions are disabled
        boolean assertEnabled = false;
        assert assertEnabled = true;
        if (!assertEnabled) {
            fail("Assertion is disabled");
        }
    }

    @Test
    public void testStart_SetsStartTimeToCurrentTimeMillis() {
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }

}