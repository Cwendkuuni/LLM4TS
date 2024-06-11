package org.junit.platform.commons.annotation.BeforeEach; import static org.junit.Assert.*;

import org.junit.Test;

public class TimerTest {

 private Timer timer;

 @BeforeEach
 public void setUp() {
 timer = new Timer(1000);
 }

 // Test case for isCounting() method
 @Test
 public void testIsCounting() {
 timer.start();
 assertTrue(timer.isCounting());
 }

 // Test case for isCounting() when timer is not started
 @Test
 public void testIsCountingWhenNotStarted() {
 assertFalse(timer.isCounting());
 }

 // Test case for isCounting() when time is up
 @Test
 public void testIsCountingWhenTimeIsUp() {
 timer.start();
 try {
 Thread.sleep(1010);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertFalse(timer.isCounting());
 }

 // Test case for constructor with negative durationMillis
 @Test(expected = IllegalArgumentException.class)
 public void testTimerWithNegativeDurationMillis() {
 new Timer(-1000);
 }

 // Test case for constructor with zero durationMillis
 @Test
 public void testTimerWithZeroDurationMillis() {
 new Timer(0);
 }

}