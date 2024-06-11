package org.junit.test; // Replace with your actual package name

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.mockito.internal.util.Timer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

public class TimerTest {

 private Timer timer;

 @Test
 public void testTimerCreation() {
 timer = new Timer(1000);
 assertTrue(timer != null);
 }

 @Test
 public void testIsCounting_whenTimerNotStarted_returnsFalse() {
 timer = new Timer(1000);
 assertFalse(timer.isCounting());
 }

 @Test
 public void testIsCounting_whenTimerStartedAndNotExpired_returnsTrue() {
 timer = new Timer(1000);
 timer.start();
 long currentTime = System.currentTimeMillis();
 Calendar calendar = new GregorianCalendar();
 calendar.setTimeInMillis(currentTime);
 calendar.add(Calendar.MILLISECOND, 500); // Add half duration to current time
 long expectedEndTime = calendar.getTimeInMillis();
 assertTrue(timer.isCounting() && expectedEndTime >= System.currentTimeMillis());
 }

 @Test
 public void testIsCounting_whenTimerStartedAndExpired_returnsFalse() {
 timer = new Timer(1000);
 timer.start();
 long currentTime = System.currentTimeMillis();
 Calendar calendar = new GregorianCalendar();
 calendar.setTimeInMillis(currentTime);
 calendar.add(Calendar.MILLISECOND, 2000); // Add double duration to current time
 assertFalse(timer.isCounting());
 }

 @Test
 public void testIsCounting_whenTimerIsNotStartedAndExpired_returnsFalse() {
 timer = new Timer(1000);
 assertFalse(timer.isCounting());
 }

 @Test
 public void testStart_whenTimerAlreadyStarted_doesNotThrowException() {
 timer = new Timer(1000);
 timer.start();
 timer.start();
 }

 @Test(expected = AssertionError.class)
 public void testStart_whenTimerIsAlreadyExpired_throwsException() {
 timer = new Timer(1000);
 timer.start();
 timer.start();
 long currentTime = System.currentTimeMillis();
 Calendar calendar = new GregorianCalendar();
 calendar.setTimeInMillis(currentTime);
 calendar.add(Calendar.MILLISECOND, 3000); // Add triple duration to current time
}

}