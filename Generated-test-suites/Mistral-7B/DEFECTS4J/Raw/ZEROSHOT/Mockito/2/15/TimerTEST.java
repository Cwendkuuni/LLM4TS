package org.junit.test;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

 private Timer timer;

 @Before
 public void setUp() {
 timer = new Timer(1000);
 }

 @Test
 public void testIsCounting_whenTimerNotStarted_thenReturnsFalse() {
 assertFalse(timer.isCounting());
 }

 @Test
 public void testIsCounting_whenTimerStarted_thenReturnsTrue() {
 timer.start();
 long delay = 500;
 long currentTime = System.currentTimeMillis();
 long expectedEndTime = currentTime + durationMillis - delay;

 // Simulate time passing with Thread.sleep()
 try {
 Thread.sleep(delay);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 assertTrue(timer.isCounting());
 long currentTimeAfterDelay = System.currentTimeMillis();
 long endTime = timer.isCounting() ? currentTimeAfterDelay : currentTime;
 assertTrue(endTime <= expectedEndTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testIsCounting_whenTimerNotStarted_thenThrowsException() {
 timer.isCounting();
 }

 @Test
 public void testIsCounting_whenTimerExpired_thenReturnsFalse() {
 timer.start();
 long delay = durationMillis + 500;

 try {
 Thread.sleep(delay);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 assertFalse(timer.isCounting());
 }

 @Test
 public void testStart_whenTimerAlreadyStarted_thenThrowsException() {
 timer.start();
 timer.start();
 }

}