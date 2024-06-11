package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

 private Timer timer;

 @Before
 public void setUp() {
 timer = new Timer(1000);
 }

 @Test
 public void testIsCountingWhenTimerIsRunning() {
 // Start the timer and check if it is counting
 timer.start();
 assertTrue(timer.isCounting());
 }

 @Test
 public void testIsCountingWhenTimerHasElapsed() {
 // Let the timer run for some time and check if it is still counting
 try {
 Thread.sleep(2000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 // The timer should no longer be counting since it has elapsed
 assertFalse(timer.isCounting());
 }

 @Test
 public void testIsCountingWhenTimerIsNotRunning() {
 // Check if timer is not counting before starting it
 assertFalse(timer.isCounting());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsCountingWhenTimerHasAlreadyElapsed() {
 // Start a timer that has already elapsed
 timer = new Timer(-1000);
 timer.start();
 }

}