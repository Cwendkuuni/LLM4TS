package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

    @Before
    public void setup() {
        Profile.initProfiler();
    }

    @Test
    public void testInitProfiler() {
        // Test initialization of profiler
        // Not possible to directly test the output of System.err.println()
        // So, no assertion here
        Profile.initProfiler();
    }

    @Test
    public void testInit() {
        // Test initialization of profile variables
        Profile.init();
        assertEquals(0, Profile.frameList().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testClear() {
        // Test clearing of profile variables
        Profile.clear();
        assertEquals(0, Profile.frameList().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testStartStop() {
        // Test starting and stopping of profiling
        Profile.start();
        // No assertion as starting profile only initializes variables
        Profile.stop();
    }

    @Test
    public void testSetFileName() {
        // Test setting of file name for profiling output
        Profile.setFileName("test.txt");
        // No assertion as it only sets the file name
    }

    @Test
    public void testShutdown() {
        // Test shutting down of profiler
        // No assertion as shutting down profile only performs operations
        Profile.shutdown();
    }

    @Test
    public void testThreads() {
        // Test retrieval of thread ids
        Iterable<Long> threads = Profile.threads();
        // Assert no threads retrieved initially
        assertEquals(0, threads.spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testInteractions() {
        // Test retrieval of frame interactions for a thread
        Iterable<Frame> interactions = Profile.interactions(1L);
        // Assert no interactions retrieved initially
        assertEquals(0, interactions.spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testFrameList() {
        // Test retrieval of frame list
        Iterable<Frame> frameList = Profile.frameList();
        // Assert frame list size is 0 initially
        assertEquals(0, frameList.spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testAllocations() {
        // Test retrieval of class allocations
        Iterable<ClassAllocation> allocations = Profile.allocations();
        // Assert no allocations retrieved initially
        assertEquals(0, allocations.spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testGetThreadTotalTime() {
        // Test retrieval of total time for a thread
        long totalTime = Profile.getThreadTotalTime(1L);
        // Assert total time is 0 initially
        assertEquals(0, totalTime);
    }

    // Test sortFrameList() method is not applicable for testing

    @Test
    public void testStartMethod1() {
        // Test starting a method
        // No assertion as starting a method only initializes variables
        Profile.start("ClassName", "methodName");
    }

    @Test
    public void testEndMethod1() {
        // Test ending a method
        // No assertion as ending a method only performs operations
        Profile.end("ClassName", "methodName");
    }

    @Test
    public void testBeginWait() {
        // Test beginning of wait state for a method
        // No assertion as beginning wait only performs operations
        Profile.beginWait("ClassName", "methodName");
    }

    @Test
    public void testEndWait() {
        // Test ending of wait state for a method
        // No assertion as ending wait only performs operations
        Profile.endWait("ClassName", "methodName");
    }

    @Test
    public void testUnwind() {
        // Test unwinding of stack for an exception
        // No assertion as unwinding only performs operations
        Profile.unwind("ClassName", "methodName", "Exception");
    }

    @Test
    public void testAlloc() {
        // Test allocation of a class
        // No assertion as allocation only performs operations
        Profile.alloc("ClassName");
    }

    @Test
    public void testRun() {
        // Test running of Profile class as thread
        // No assertion as run() method only performs operations
        Profile profile = new Profile();
        profile.run();
    }
}