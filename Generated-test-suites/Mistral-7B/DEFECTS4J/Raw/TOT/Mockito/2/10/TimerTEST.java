package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

 private Timer timer;

 @Before
 public void setup() {
 timer = new Timer(1000);
 }

 /**
 * Test isCounting() method with valid timer and elapsed time less than duration
 */
 @Test
 public void testIsCountingWithElapsedTimeLessThanDuration() {
 timer.start();
 assertTrue(timer.isCounting());
 }

 /**
 * Test isCounting() method with valid timer and elapsed time equal to duration
 */
 @Test
 public void testIsCountingWithElapsedTimeEqualToDuration() {
 timer.start();
 try {
 Thread.sleep(1001);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertFalse(timer.isCounting());
 }

 /**
 * Test isCounting() method with valid timer and elapsed time greater than duration
 */
 @Test
 public void testIsCountingWithElapsedTimeGreaterThanDuration() {
 timer.start();
 try {
 Thread.sleep(2000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertFalse(timer.isCounting());
 }

 /**
 * Test isCounting() method with null timer
 */
 @Test(expected = AssertionError.class)
 public void testIsCountingWithNullTimer() {
 assertFalse(new Timer(null).isCounting());
 }

 /**
 * Test start() method with valid duration
 */
 @Test
 public void testStartWithValidDuration() {
 timer = new Timer(1000);
 timer.start();
 assertTrue(timer.isCounting());
 }

 /**
 * Test start() method with negative duration
 */
 @Test(expected = IllegalArgumentException.class)
 public void testStartWithNegativeDuration() {
 timer = new Timer(-1000);
 timer.start();
 }

}