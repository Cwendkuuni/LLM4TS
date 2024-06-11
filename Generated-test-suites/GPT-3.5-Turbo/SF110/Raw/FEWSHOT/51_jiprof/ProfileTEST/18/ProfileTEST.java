package com.mentorgen.tools.profile.runtime;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProfileTest {

    @Before
    public void setUp() {
        Profile.init();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        // Assert statements
    }

    @Test
    public void testClear() {
        Profile.clear();
        // Assert statements
    }

    @Test
    public void testStart() {
        Profile.start();
        // Assert statements
    }

    @Test
    public void testStop() {
        Profile.stop();
        // Assert statements
    }

    @Test
    public void testSetFileName() {
        Profile.setFileName("testFileName");
        // Assert statements
    }

    @Test
    public void testShutdown() {
        Profile.shutdown();
        // Assert statements
    }

    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        // Assert statements
    }

    @Test
    public void testInteractions() {
        Iterable<Frame> interactions = Profile.interactions(1L);
        // Assert statements
    }

    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        // Assert statements
    }

    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        // Assert statements
    }

    @Test
    public void testGetThreadTotalTime() {
        long threadTotalTime = Profile.getThreadTotalTime(1L);
        // Assert statements
    }

    @Test
    public void testSortFrameList() {
        Profile.sortFrameList(null);
        // Assert statements
    }

    @Test
    public void testStartStringString() {
        Profile.start("className", "methodName");
        // Assert statements
    }

    @Test
    public void testEndStringString() {
        Profile.end("className", "methodName");
        // Assert statements
    }

    @Test
    public void testBeginWait() {
        Profile.beginWait("className", "methodName");
        // Assert statements
    }

    @Test
    public void testEndWait() {
        Profile.endWait("className", "methodName");
        // Assert statements
    }

    @Test
    public void testUnwind() {
        Profile.unwind("className", "methodName", "exception");
        // Assert statements
    }

    @Test
    public void testAlloc() {
        Profile.alloc("className");
        // Assert statements
    }

    @Test
    public void testRun() {
        Profile profile = new Profile();
        profile.run();
        // Assert statements
    }
}