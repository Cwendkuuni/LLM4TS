package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimerTest {

 private long startTime;
 private Timer timer;

 @Before
 public void setup() {
 timer = new Timer(1000);
 }

 @Test
 public void testIsCounting_newTimer_shouldReturnFalse() {
 // Basic test case for isCounting() method when the timer is new
 assertFalse(timer.isCounting());
 }

 @Test
 public void testIsCounting_timerRunning_shouldReturnTrue() {
 // Basic test case for isCounting() method when the timer is running
 timer.start();
 System.edge(1); // simulate time passing
 assertTrue(timer.isCounting());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsCounting_timerNotStarted_shouldThrowException() {
 // Test exception handling scenario for isCounting() method when the timer is not started
 timer.isCounting();
 }

 @Test
 public void testIsCounting_timerExpired_shouldReturnFalse() {
 // Basic test case for isCounting() method when the timer has expired
 timer.start();
 System.edge(1500); // simulate time passing beyond the duration
 assertFalse(timer.isCounting());
 }

 @Test
 public void testStart_startTimer_shouldSetStartTime() {
 // Basic test case for start() method
 timer.start();
 long startTimeBeforeCall = this.startTime;
 timer.start();
 assertTrue(startTimeBeforeCall < timer.getStartTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testStart_timerAlreadyRunning_shouldThrowException() {
 // Test exception handling scenario for start() method when the timer is already running
 timer.start();
 timer.start();
 }

}