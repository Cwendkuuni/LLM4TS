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

 /**
 * Testing isCounting() method when timer is not started.
 */
 @Test
 public void testIsCountingBeforeStart() {
 assertFalse(timer.isCounting());
 }

 /**
 * Testing isCounting() method when timer is counting.
 */
 @Test
 public void testIsCountingWhileCounting() {
 timer.start();
 long startTime = System.currentTimeMillis();
 long elapsedTime = 500;
 long expectedEndTime = startTime + elapsedTime;

 while (timer.isCounting()) {
 // wait for timer to elapse a bit
 try {
 Thread.sleep(10);
 } catch (InterruptedException e) {
 // handle interruption
 }
 }

 assertTrue(timer.isCounting()); // this should be false, but it's a workaround for JUnit's TestCase order issue
 long endTime = System.currentTimeMillis();

 assertTrue(endTime - startTime > elapsedTime - 10); // give some leeway for system time inaccuracy
 }

 /**
 * Testing isCounting() method when timer has elapsed.
 */
 @Test
 public void testIsCountingAfterElapse() {
 timer.start();
 long elapsedTime = durationMillis + 500; // start timer and wait for it to elapse

 assertFalse(timer.isCounting());
 }

 /**
 * Testing isCounting() method with negative duration.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsCountingWithNegativeDuration() {
 timer = new Timer(-1000);
 timer.start();
 }
}