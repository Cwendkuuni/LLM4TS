package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.time.StopWatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StopWatchTest {

 private StopWatch stopWatch;

 @Before
 public void setup() {
 stopWatch = new StopWatch();
 }

 // Test case for method: start()
 @Test
 public void testStart() {
 stopWatch.start();
 // Basic functionality check
 assertTrue(stopWatch.isRunning());
 }

 // Test case for method: stop()
 @Test
 public void testStop() {
 stopWatch.start();
 long initialTime = stopWatch.getTime();
 stopWatch.stop();
 // Basic functionality check
 assertEquals(initialTime, stopWatch.getTime());
 }

 // Test case for method: reset()
 @Test
 public void testReset() {
 stopWatch.start();
 stopWatch.stop();
 stopWatch.reset();
 // Basic functionality check
 assertEquals(0, stopWatch.getTime());
 }

 // Test case for method: split()
 @Test
 public void testSplit() {
 stopWatch.start();
 stopWatch.split();
 // Basic functionality check
 assertTrue(stopWatch.isSplit());
 }

 // Test case for method: unsplit()
 @Test
 public void testUnsplit() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 // Basic functionality check
 assertFalse(stopWatch.isSplit());
 }

 // Test case for method: suspend()
 @Test
 public void testSuspend() {
 stopWatch.start();
 stopWatch.suspend();
 // Basic functionality check
 assertFalse(stopWatch.isRunning());
 }

 // Test case for method: resume()
 @Test
 public void testResume() {
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 // Basic functionality check
 assertTrue(stopWatch.isRunning());
 }

 // Test case for method: getTime()
 @Test
 public void testGetTime() {
 stopWatch.start();
 stopWatch.stop();
 long time = stopWatch.getTime();
 // Basic functionality check
 assertNotEquals(-1, time);
 }

 // Test case for method: getSplitTime()
 @Test(expected = IllegalStateException.class)
 public void testGetSplitTimeBeforeSplit() {
 stopWatch.getSplitTime();
 }

 @Test
 public void testGetSplitTimeAfterSplit() throws IllegalStateException, InterruptedException {
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 // Basic functionality check
 assertNotEquals(-1, splitTime);
 }

 // Test case for method: toString()
 @Test
 public void testToString() {
 stopWatch.start();
 stopWatch.stop();
 String toString = stopWatch.toString();
 SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
 try {
 long millis = Long.parseLong(toString.split(":")[2]);
 String seconds = toString.split(":")[1];
 long secondsMillis = Long.parseLong(seconds) * 1000 + millis;
 String expected = formatter.format(System.currentTimeMillis() - stopWatch.getTime());
 assertEquals(expected, toString);
 } catch (NumberFormatException | ParseException e) {
 fail("toString failed");
 }
 }

 // Test case for method: toSplitString()
 @Test
 public void testToSplitString() throws IllegalStateException {
 stopWatch.start();
 stopWatch.split();
 String toSplitString = stopWatch.toSplitString();
 SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
 try {
 long millis = Long.parseLong(toSplitString.split(":")[2]);
 String seconds = toSplitString.split(":")[1];
 long secondsMillis = Long.parseLong(seconds) * 1000 + millis;
 String expected = formatter.format(stopWatch.getTime());
 assertEquals(expected, toSplitString);
 } catch (NumberFormatException | ParseException e) {
 fail("toSplitString failed");
 }
 }

 // Test case for exception handling: start() after start()
 @Test(expected = IllegalStateException.class)
 public void testStartAfterStart() {
 stopWatch.start();
 stopWatch.start();
 }

 // Test case for exception handling: split() after split()
 @Test(expected = IllegalStateException.class)
 public void testSplitAfterSplit() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 // Test case for exception handling: unsplit() before split()
 @Test(expected = IllegalStateException.class)
 public void testUnsplitBeforeSplit() {
 stopWatch.unsplit();
 }

 // Test case for exception handling: resume() before suspend()
 @Test(expected = IllegalStateException.class)
 public void testResumeBeforeSuspend() {
 stopWatch.resume();
 }
}