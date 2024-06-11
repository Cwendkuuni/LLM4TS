package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

 private Timer timer;

 @Before
 public void setUp() {
 timer = new Timer(1000);
 }

 @Test
 public void testIsCounting_TimerIsRunning() {
 timer.start();
 long startTime = System.currentTimeMillis();
 long delay = 500;
 long elapsedTime;

 try {
 Thread.sleep(delay);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 elapsedTime = System.currentTimeMillis() - startTime;
 assertTrue(timer.isCounting());
 assertFalse(timer.isCountingAfterExpiration(elapsedTime));
 }

 @Test
 public void testIsCounting_TimerHasNotBeenStarted() {
 assertFalse(timer.isCounting());
 }

 @Test
 public void testIsCountingAfterExpiration() {
 timer.start();
 long delay = durationMillis + 100;
 long elapsedTime = System.currentTimeMillis() - startTime;

 assertFalse(timer.isCounting());
 assertFalse(timer.isCountingAfterExpiration(elapsedTime));
 }

 @Test(expected = IllegalStateException.class)
 public void testIsCounting_AssertionErrorThrown() {
 timer = new Timer(-1);
 timer.isCounting();
 }

}