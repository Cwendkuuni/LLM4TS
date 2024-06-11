package org.junit.test; //Replace with your actual package name

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TimerTest {

 private Timer timer;

 @Test
 public void testTimerInitialization() {
 timer = new Timer(1000); //1000 millis = 1 second
 //Test that the timer is not counting before start
 assertFalse(timer.isCounting());
 }

 @Test
 public void testTimerStart() {
 timer = new Timer(1000);
 timer.start();
 //Verify that the timer is now counting
 assertTrue(timer.isCounting());
 }

 @Test
 public void testTimerCountDown() throws InterruptedException {
 timer = new Timer(1000);
 timer.start();

 //Wait for the timer to finish counting down
 TimeUnit.MILLISECONDS.sleep(1500); //1500 millis = 1.5 seconds

 //Verify that the timer is no longer counting
 assertFalse(timer.isCounting());
 }

 @Test(expected = IllegalStateException.class)
 public void testTimerStartTwice() {
 timer = new Timer(1000);
 timer.start();
 timer.start(); //Should throw an IllegalStateException
 }

}