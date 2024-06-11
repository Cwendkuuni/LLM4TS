package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {

 private Timer timer;

 @Before
 public void setup() {
 timer = new Timer(1000);
 }

 /**
 * Test isCounting() method when timer is not started
 */
 @Test
 public void testIsCountingNotStarted() {
 timer = new Timer(1000);
 assertFalse(timer.isCounting());
 }

 /**
 * Test isCounting() method when timer is counting
 */
 @Test
 public void testIsCountingStarted() {
 timer.start();
 long startTime = System.currentTimeMillis();
 long elapsedTime = 500;
 Thread.sleep(elapsedTime);
 assertTrue(timer.isCounting());
 long currentTime = System.currentTimeMillis();
 assertEquals(durationMillis - elapsedTime, currentTime - startTime);
 }

 /**
 * Test isCounting() method when duration time has been elapsed
 */
 @Test
 public void testIsCountingElapsed() {
 timer.start();
 Thread.sleep(durationMillis + 1000);
 assertFalse(timer.isCounting());
 }

 /**
 * Test isCounting() method with negative duration
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsCountingNegativeDuration() {
 timer = new Timer(-1000);
 timer.start();
 }

}