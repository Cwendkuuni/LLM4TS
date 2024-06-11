package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.mockito.internal.util.Timer;

public class TimerTest {

 private Timer timer;

 @Test
 public void testTimerConstruction() {
 timer = new Timer(1000);
 assertTrue(timer.isCounting() == false);
 }

 @Test
 public void testTimerStart() {
 timer = new Timer(1000);
 timer.start();
 assertTrue(timer.isCounting() == true);
 }

 @Test
 public void testTimerIsCountingTrueWhenWithinDuration() {
 timer = new Timer(1000);
 timer.start();
 long elapsedTime = 500;
 long currentTime = System.currentTimeMillis();
 long expectedEndTime = currentTime + 500;

 // simulate elapsed time
 System.setCurrentTimeMillis(currentTime + elapsedTime);

 assertTrue(timer.isCounting() == true);
 assertTrue(System.currentTimeMillis() - timer.getStartTime() <= timer.getDurationMillis());
 assertTrue(expectedEndTime >= System.currentTimeMillis());
 }

 @Test
 public void testTimerIsCountingFalseWhenElapsedDurationGreater() {
 timer = new Timer(1000);
 timer.start();
 long elapsedTime = 2000;
 long currentTime = System.currentTimeMillis();
 long expectedEndTime = currentTime + 1000;

 // simulate elapsed time
 System.setCurrentTimeMillis(currentTime + elapsedTime);

 assertFalse(timer.isCounting() == true);
 assertFalse(System.currentTimeMillis() - timer.getStartTime() <= timer.getDurationMillis());
 assertFalse(expectedEndTime >= System.currentTimeMillis());
 }

 @Test(expected = AssertionError.class)
 public void testTimerStartWithNegativeDuration() {
 timer = new Timer(-500);
 timer.start();
 }

 @Test
 public void testTimerGetters() {
 timer = new Timer(2000);
 timer.start();

 long elapsedTime = 500;
 long currentTime = System.currentTimeMillis();
 long expectedEndTime = currentTime + 2000;

 // simulate elapsed time
 System.setCurrentTimeMillis(currentTime + elapsedTime);

 assertTrue(timer.getDurationMillis() == 2000);
 assertTrue(timer.getIsCounting() == true);
 assertTrue(timer.getStartTime() > 0);
 }

}