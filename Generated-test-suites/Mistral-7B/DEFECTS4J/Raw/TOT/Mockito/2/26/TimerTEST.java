package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

 private Timer timer;

 @Test
 public void testIsCounting_WhenTimerIsRunning_ThenReturnsTrue() {
 timer = new Timer(1000);
 timer.start();
 Assert.assertTrue("Timer should be counting", timer.isCounting());
 }

 @Test
 public void testIsCounting_WhenTimerHasElapsed_ThenReturnsFalse() {
 timer = new Timer(1000);
 timer.start();
 try {
 Thread.sleep(1500); // Let the timer elapse by 1.5 seconds
 } catch (InterruptedException e) {
 // Do nothing
 }
 Assert.assertFalse("Timer should no longer be counting", timer.isCounting());
 }

 @Test
 public void testIsCounting_WhenTimerHasNotStarted_ThenReturnsFalse() {
 timer = new Timer(1000);
 Assert.assertFalse("Timer should not be counting before start", timer.isCounting());
 }

 @Test(expected = AssertionError.class)
 public void testIsCounting_WhenStartTimeIsInvalid_ThenThrowsAssertionError() {
 timer = new Timer(1000);
 // Set start time to an invalid value
 timer.startTime = 500;
 timer.isCounting();
 }

 @Test
 public void testStart_WhenCalledMultipleTimes_ThenSetsStartTimeCorrectly() {
 timer = new Timer(1000);
 timer.start();
 timer.start();
 timer.start();
 Assert.assertEquals("Incorrect start time after multiple starts", 0, timer.startTime % 1000);
 }

}