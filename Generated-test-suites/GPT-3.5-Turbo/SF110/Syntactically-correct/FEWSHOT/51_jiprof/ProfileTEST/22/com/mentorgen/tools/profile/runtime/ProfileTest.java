
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

public class ProfileTest {

    @Before
    public void setUp() {
        Profile.initProfiler();
        Profile.start();
    }

    @Test
    public void testInitProfiler() {
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testInit() {
        Profile.init();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.clear();
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testStartAndStop() {
        Profile.stop();
        assertFalse(Controller._profile);
        Profile.start();
        assertTrue(Controller._profile);
    }

    @Test
    public void testSetFileName() {
        Profile.setFileName("test.txt");
        assertEquals("test.txt", Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.shutdown();
        assertFalse(Controller._profile);
        assertEquals(0, Profile._threadDictionary.size());
    }

    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        assertNotNull(threads);
        assertFalse(threads.iterator().hasNext());
    }

    @Test
    public void testInteractions() {
        Iterable<Frame> interactions = Profile.interactions(123);
        assertNotNull(interactions);
        assertFalse(interactions.iterator().hasNext());
    }

    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        assertNotNull(frameList);
        assertFalse(frameList.iterator().hasNext());
    }

    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        assertNotNull(allocations);
        assertFalse(allocations.iterator().hasNext());
    }

    @Test
    public void testGetThreadTotalTime() {
        long totalTime = Profile.getThreadTotalTime(123);
        assertEquals(0, totalTime);
    }

    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = (f1, f2) -> f1.getClassName().compareTo(f2.getClassName());
        Profile.sortFrameList(comp);
        // TODO: check if frameList is correctly sorted
    }

    @Test
    public void testStartMethod() {
        Profile.start("com.example.Class", "method");
        // TODO: verify if the frame is added to frameList
    }

    @Test
    public void testEndMethod() {
        Profile.end("com.example.Class", "method");
        // TODO: verify if the frame's end time is set correctly
    }

    @Test
    public void testBeginWait() {
        Profile.beginWait("com.example.Class", "method");
        // TODO: verify if the frame's begin wait time is set correctly
    }

    @Test
    public void testEndWait() {
        Profile.endWait("com.example.Class", "method");
        // TODO: verify if the frame's end wait time is set correctly
    }

    @Test
    public void testUnwind() {
        Profile.unwind("com.example.Class", "method", "Exception");
        // TODO: verify if the threadActiveFrame is updated correctly
    }

    @Test
    public void testAlloc() {
        Profile.alloc("com.example.Class");
        // TODO: verify if the allocation count is incremented correctly
    }

    @Test
    public void testRun() {
        Profile profile = new Profile();
        profile.run();
        assertFalse(Controller._profile);
        assertEquals(0, Profile._threadDictionary.size());
    }

}
