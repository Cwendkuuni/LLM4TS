package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;

import java.util.Date;

public class StopWatchTest {

 // Basic test cases for each method

 @org.junit.Test
 public void testStart() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 assertFalse(stopWatch.isRunning());
 }

 @org.junit.Test
 public void testStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long time = stopWatch.getTime();
 assertTrue(stopWatch.isStopped());
 assertEquals(time, 0); // time should be 0 since start and stop were called in quick succession
 }

 @org.junit.Test
 public void testReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.reset();
 assertFalse(stopWatch.isRunning());
 assertFalse(stopWatch.isStopped());
 assertFalse(stopWatch.isSuspended());
 }

 @org.junit.Test
 public void testSplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 assertTrue(stopWatch.isSplit());
 assertEquals(splitTime, 0); // time should be 0 since split was called immediately after start
 }

 @org.junit.Test
 public void testUnsplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 assertFalse(stopWatch.isSplit());
 }

 @org.junit.Test
 public void testSuspend() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 long currentTime = System.currentTimeMillis();
 stopWatch.resume();
 long timeElapsed = stopWatch.getTime();
 assertTrue(stopWatch.isRunning());
 assertTrue(stopWatch.isResumed());
 assertEquals(timeElapsed, currentTime - stopWatch.getStartTime());
 }

 @org.junit.Test
 public void testToString() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 String result = stopWatch.toString();
 assertTrue(result.matches("^[0-9]+:[0-9][0-9]:[0-9][0-9]:[0-9][0-9][0-9]\\.[0-9]+$"));
 }

 // Testing edge cases and exception handling

 @org.junit.Test(expected = IllegalStateException.class)
 public void testStartTwiceWithoutReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testUnsplitWithoutSplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.unsplit();
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testResumeWithoutSuspend() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.resume();
 }

 @org.junit.Test
 public void testGetTimeBeforeStart() {
 StopWatch stopWatch = new StopWatch();
 long timeBeforeStart = System.currentTimeMillis();
 long timeAfterStart = stopWatch.getTime();
 stopWatch.start();
 long timeAfterStartAndStop = stopWatch.getTime();
 assertEquals(timeAfterStartAndStop, timeAfterStart + timeBeforeStart);
 }

 @org.junit.Test
 public void testToStringAfterSplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 String result = stopWatch.toString();
 assertTrue(result.contains("Split"));
 }

 @org.junit.Test
 public void testToStringAfterResume() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 String result = stopWatch.toString();
 assertTrue(result.contains("Resumed"));
 }

}