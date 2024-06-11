package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

 private Timer timer;

 @Before
 public void setup() {
 timer = new Timer(1000);
 }

 /**
 * Test isCounting() method when timer is counting
 */
 @Test
 public void testIsCountingWhileCounting() {
 timer.start();
 long startTime = System.currentTimeMillis();
 long elapsedTime = 500;

 // simulate elapsed time
 System.currentTimeMillis = (millis) -> startTime + elapsedTime; // NOSONAR

 assertTrue(timer.isCounting());
 }

 /**
 * Test isCounting() method when timer has finished counting
 */
 @Test
 public void testIsCountingAfterCountingFinished() {
 timer.start();
 // wait for timer to finish
 long finishTime = System.currentTimeMillis();
 long elapsedTime = timer.durationMillis + startTime;

 // reset system current time millis to its original value
 System.currentTimeMillis = System.currentTimeMillis;

 assertFalse(timer.isCounting());
 }

 /**
 * Test isCounting() method when timer is not started
 */
 @Test
 public void testIsCountingBeforeStarting() {
 assertFalse(timer.isCounting());
 }

 /**
 * Test start() method when timer is already started
 */
 @Test(expected = IllegalStateException.class)
 public void testStartAlreadyStarted() {
 timer.start();
 timer.start();
 }

 /**
 * Test start() method when durationMillis is negative
 */
 @Test(expected = IllegalArgumentException.class)
 public void testStartWithNegativeDuration() {
 timer = new Timer(-1000);
 timer.start();
 }
}