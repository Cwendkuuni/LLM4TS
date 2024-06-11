package org.mockito.internal.util;

import org.junit.Assert;import org.junit.Before;import org.junit.Test;

public class TimerTest {

 private Timer timer;

 @Before
 public void setup() {
 timer = new Timer(1000);
 }

 /**
 * Test isCounting() method when timer is counting.
 */
 @Test
 public void testIsCountingWhenTimerIsCounting() {
 // Start the timer
 timer.start();
 // Wait for a short while to let the timer count down a bit
 try {
 Thread.sleep(500);
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 // Check if the timer is still counting
 Assert.assertTrue(timer.isCounting());
 }

 /**
 * Test isCounting() method when timer has finished counting.
 */
 @Test
 public void testIsCountingWhenTimerHasFinishedCounting() {
 // Let the timer count down completely
 try {
 Thread.sleep(1500);
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 // Check if the timer is not counting anymore
 Assert.assertFalse(timer.isCounting());
 }

 /**
 * Test isCounting() method when timer has been reset.
 */
 @Test
 public void testIsCountingWhenTimerHasBeenReset() {
 // Let the timer count down completely
 try {
 Thread.sleep(1500);
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 // Reset the timer
 timer.start();
 // Check if the timer is counting again
 Assert.assertTrue(timer.isCounting());
 }

 /**
 * Test isCounting() method when timer duration is 0.
 */
 @Test(expected = AssertionError.class)
 public void testIsCountingWhenTimerDurationIsZero() {
 // Create a timer with a duration of 0 milliseconds
 Timer timerWithZeroDuration = new Timer(0);
 // Check if the timer is still counting
 timerWithZeroDuration.isCounting();
 }

}