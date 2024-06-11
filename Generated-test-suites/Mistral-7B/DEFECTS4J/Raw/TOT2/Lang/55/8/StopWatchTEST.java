package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang.time.StopWatch;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for StopWatch.
 */
public class StopWatchTest {

 // Test instance
 private StopWatch stopWatch;

 /**
 * Sets up the test environment.
 */
 @Before
 public void setUp() {
 stopWatch = new StopWatch();
 }

 /**
 * Test method for {@link StopWatch#start()}.
 */
 @Test
 public void testStart() {
 stopWatch.start();
 assertTrue(stopWatch.isRunning());
 }

 /**
 * Test method for {@link StopWatch#stop()}.
 */
 @Test
 public void testStop() {
 stopWatch.stop();
 assertFalse(stopWatch.isRunning());
 }

 /**
 * Test method for {@link StopWatch#reset()}.
 */
 @Test
 public void testReset() {
 stopWatch.reset();
 assertEquals(-1, stopWatch.getTime());
 }

 /**
 * Test method for {@link StopWatch#split()}.
 */
 @Test
 public void testSplit() {
 stopWatch.start();
 stopWatch.split();
 assertFalse(stopWatch.isRunning());
 assertNotNull(stopWatch.getSplitTime());
 }

 /**
 * Test method for {@link StopWatch#unsplit()}.
 */
 @Test
 public void testUnsplit() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 assertTrue(stopWatch.isRunning());
 }

 /**
 * Test method for {@link StopWatch#suspend()}.
 */
 @Test
 public void testSuspend() {
 stopWatch.start();
 stopWatch.suspend();
 assertFalse(stopWatch.isRunning());
 }

 /**
 * Test method for {@link StopWatch#resume()}.
 */
 @Test
 public void testResume() {
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 assertTrue(stopWatch.isRunning());
 }

 /**
 * Test method for {@link StopWatch#getTime()}.
 */
 @Test
 public void testGetTime() {
 stopWatch.start();
 long time = stopWatch.getTime();
 stopWatch.stop();
 assertEquals(time, stopWatch.getTime());
 }

 /**
 * Test method for {@link StopWatch#getSplitTime()}.
 */
 @Test
 public void testGetSplitTime() {
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 stopWatch.unsplit();
 assertEquals(splitTime, stopWatch.getTime());
 }

 /**
 * Test method for {@link StopWatch#toString()}.
 */
 @Test
 public void testToString() {
 stopWatch.start();
 stopWatch.split();
 String result = stopWatch.toString();
 assertEquals("0m 0s 0ms", result);
 }

 /**
 * Test method for {@link StopWatch#toSplitString()}.
 */
 @Test
 public void testToSplitString() {
 stopWatch.start();
 stopWatch.split();
 String result = stopWatch.toSplitString();
 assertEquals("0m 0s 0ms", result);
 }

 /**
 * Test method for {@link StopWatch#start()}, {@link StopWatch#stop()}, {@link StopWatch#split()}, and {@link StopWatch#unsplit()} in sequence.
 */
 @Test
 public void testStartStopSplitUnsplit() {
 stopWatch.start();
 stopWatch.stop();
 stopWatch.split();
 stopWatch.unsplit();
 }

 /**
 * Test method for {@link StopWatch#start()}, {@link StopWatch#suspend()}, and {@link StopWatch#resume()} in sequence.
 */
 @Test
 public void testStartSuspendResume() {
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 }

 /**
 * Test method for {@link StopWatch#start()}, {@link StopWatch#stop()}, and {@link StopWatch#start()} in sequence.
 */
 @Test(expected = IllegalStateException.class)
 public void testStartStopStart() {
 stopWatch.start();
 stopWatch.stop();
 stopWatch.start();
 }

 /**
 * Test method for {@link StopWatch#split()}, {@link StopWatch#unsplit()}, and {@link StopWatch#split()} in sequence.
 */
 @Test(expected = IllegalStateException.class)
 public void testSplitUnsplitSplit() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 stopWatch.split();
 }

}