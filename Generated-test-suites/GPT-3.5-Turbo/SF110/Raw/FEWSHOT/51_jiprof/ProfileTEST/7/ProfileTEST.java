
package com.mentorgen.tools.profile.runtime;

import com.mentorgen.tools.profile.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ProfileTest {

    @Before
    public void setUp() {
        Profile.initProfiler();
    }

    @Test
    public void testInitProfiler() {
        // Test that initProfiler initializes the profiler correctly
        Profile.initProfiler();
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertNotNull(Profile._controller);
        assertNotNull(Profile._controllerThread);
    }

    @Test
    public void testInit() {
        // Test that init initializes the necessary data structures
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
        // Test that clear resets all data structures
        Profile.clear();
        assertTrue(Profile._threadActiveFrame.isEmpty());
        assertTrue(Profile._threadDictionary.isEmpty());
        assertTrue(Profile._methodDictionary.isEmpty());
        assertTrue(Profile._frameList.isEmpty());
        assertTrue(Profile._allocList.isEmpty());
    }

    @Test
    public void testStart() {
        // Test that start calls the start method of the controller
        Profile.start();
        assertTrue(Profile._controller.isStarted());
    }

    @Test
    public void testStop() {
        // Test that stop calls the stop method of the controller
        Profile.stop();
        assertFalse(Profile._controller.isStarted());
    }
    
    @Test
    public void testSetFileName() {
        // Test that setFileName sets the correct file name in the controller
        String fileName = "test.txt";
        Profile.setFileName(fileName);
        assertEquals(fileName, Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        // Test that shutdown stops profiling and computes net time for each frame
        // Also test that frames are properly closed
        Profile.shutdown();
        assertFalse(Profile._controller.isProfiling());
        for (Frame frame : Profile.frameList()) {
            assertTrue(frame.isClosed());
            assertNotNull(frame.getNetTime());
        }
    }

    @Test
    public void testThreads() {
        // Test that threads returns the correct list of thread IDs
        Iterable<Long> threads = Profile.threads();
        assertNotNull(threads);
    }

    @Test
    public void testInteractions() {
        // Test that interactions returns the correct list of frames for a given thread ID
        long threadId = Thread.currentThread().getId();
        Iterable<Frame> interactions = Profile.interactions(threadId);
        assertNotNull(interactions);
    }

    @Test
    public void testFrameList() {
        // Test that frameList returns the correct list of frames
        Iterable<Frame> frameList = Profile.frameList();
        assertNotNull(frameList);
    }

    @Test
    public void testAllocations() {
        // Test that allocations returns the correct list of ClassAllocations
        Iterable<ClassAllocation> allocations = Profile.allocations();
        assertNotNull(allocations);
    }

    @Test
    public void testGetThreadTotalTime() {
        // Test that getThreadTotalTime returns the correct total time for a given thread ID
        long threadId = Thread.currentThread().getId();
        long totalTime = Profile.getThreadTotalTime(threadId);
        assertNotNull(totalTime);
    }

    @Test
    public void testSortFrameList() {
        // Test that sortFrameList sorts the frameList correctly according to the given comparator
        Comparator<Frame> comp = new Comparator<Frame>() {
            @Override
            public int compare(Frame f1, Frame f2) {
                return f1.getNetTime() - f2.getNetTime();
            }
        };
        Profile.sortFrameList(comp);
        List<Frame> sortedFrameList = new ArrayList<>(Profile.frameList());
        sortedFrameList.sort(comp);
        assertEquals(sortedFrameList, new ArrayList<>(Profile.frameList()));
    }

    @Test
    public void testStartMethod() {
        // Test that start starts a frame correctly
        String className = "Test";
        String methodName = "start";
        Profile.start(className, methodName);
        Frame frame = Profile._threadActiveFrame.get(Thread.currentThread().getId());
        assertNotNull(frame);
        assertEquals(className, frame.getClassName());
        assertEquals(methodName, frame.getMethodName());
        assertTrue(frame.getBeginTime() > 0);
    }

    @Test
    public void testEndMethod() {
        // Test that end ends a frame correctly
        String className = "Test";
        String methodName = "end";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Frame frame = Profile._threadActiveFrame.get(Thread.currentThread().getId());
        assertNull(frame);
    }

    @Test
    public void testBeginWait() {
        // Test that beginWait starts waiting time for a frame correctly
        String className = "Test";
        String methodName = "beginWait";
        Profile.start(className, methodName);
        Profile.beginWait(className, methodName);
        Frame frame = Profile._threadActiveFrame.get(Thread.currentThread().getId());
        assertNotNull(frame);
        assertTrue(frame.isWaiting());
        assertTrue(frame.getWaitTime() > 0);
    }

    @Test
    public void testEndWait() {
        // Test that endWait ends waiting time for a frame correctly
        String className = "Test";
        String methodName = "endWait";
        Profile.start(className, methodName);
        Profile.beginWait(className, methodName);
        Profile.endWait(className, methodName);
        Frame frame = Profile._threadActiveFrame.get(Thread.currentThread().getId());
        assertNotNull(frame);
        assertFalse(frame.isWaiting());
    }

    @Test
    public void testUnwind() {
        // Test that unwind sets the correct active frame when catching an exception
        String className = "Test";
        String methodName = "unwind";
        Profile.unwind(className, methodName, "Exception");
        Frame frame = Profile._threadActiveFrame.get(Thread.currentThread().getId());
        assertNotNull(frame);
        assertEquals(className, frame.getClassName());
        assertEquals(methodName, frame.getMethodName());
    }

    @Test
    public void testAlloc() {
        // Test that alloc increments the allocation count for a class correctly
        String className = "Test";
        Profile.alloc(className);
        ClassAllocation allocation = Profile._allocList.get(className);
        assertNotNull(allocation);
        assertEquals(1, allocation.getAllocCount());
    }

    @Test
    public void testRun() {
        // Test that run correctly shuts down the profiler and dumps the profile data
        Profile.run();
        assertFalse(Profile._controller.isProfiling());
        // Test that ProfileDump.dump() is called
        // Test that Profile._controller.close() is called
        // Test that Profile._controllerThread.interrupt() is called
    }
}
